package dao;

import connectDB.ConnectDB;
import entity.Thuoc;
import enums.DonViTinh;
import enums.PhanLoaiThuoc;
import enums.TrangThaiTonKho;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Thuoc_DAO {

    public List<Thuoc> getAllTbThuoc() {
        List<Thuoc> ds = new ArrayList<>();

        String sql = """
            SELECT 
                maThuoc, tenThuoc, giaBan, giaNhap, hanSuDung,
                maKho, maNhaCungCap,
                DonViNhapKho, DonViBan, TiLeDonVi,
                SoLuong, trangThaiTonKho,
                tonKhoToiThieu, tonKhoToiDa,
                loaiThuoc, maLoai, phanLoai
            FROM Thuoc
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi getAllTbThuoc: " + e.getMessage());
            e.printStackTrace();
        }
        return ds;
    }

    // ==================== TÌM THEO MÃ ====================
    public Thuoc getByMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return null;

        String sql = "SELECT * FROM Thuoc WHERE maThuoc = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maThuoc);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi getByMaThuoc: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ==================== TÌM KIẾM ====================
    public List<Thuoc> timKiemThuoc(String keyword) {
        List<Thuoc> ds = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return getAllTbThuoc();

        String sql = "SELECT * FROM Thuoc WHERE maThuoc LIKE ? OR tenThuoc LIKE ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String key = "%" + keyword.trim() + "%";
            ps.setString(1, key);
            ps.setString(2, key);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi timKiemThuoc: " + e.getMessage());
            e.printStackTrace();
        }
        return ds;
    }

    // ==================== THÊM THUỐC ====================
    public boolean themThuoc(Thuoc t) {
        if (t == null) return false;

        String sql = """
            INSERT INTO Thuoc (
                maThuoc, tenThuoc, giaBan, giaNhap, hanSuDung,
                maKho, maNhaCungCap,
                DonViNhapKho, DonViBan, TiLeDonVi,
                SoLuong, trangThaiTonKho,
                tonKhoToiThieu, tonKhoToiDa,
                loaiThuoc, maLoai, phanLoai
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            setParams(ps, t, true);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi themThuoc: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean capNhatThuoc(Thuoc t) {
        if (t == null || t.getMaThuoc() == null) return false;

        String sql = """
            UPDATE Thuoc SET
                tenThuoc = ?, giaBan = ?, giaNhap = ?, hanSuDung = ?,
                maKho = ?, maNhaCungCap = ?,
                DonViNhapKho = ?, DonViBan = ?, TiLeDonVi = ?,
                SoLuong = ?, trangThaiTonKho = ?,
                tonKhoToiThieu = ?, tonKhoToiDa = ?,
                loaiThuoc = ?, maLoai = ?, phanLoai = ?
            WHERE maThuoc = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            setParams(ps, t, false);
            ps.setString(17, t.getMaThuoc());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi capNhatThuoc: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSoLuongThuoc(Thuoc thuoc) {
        if (thuoc == null || thuoc.getMaThuoc() == null || thuoc.getMaThuoc().trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE Thuoc SET SoLuong = ? WHERE maThuoc = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, thuoc.getSoLuong());
            ps.setString(2, thuoc.getMaThuoc());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                // Tự động cập nhật trạng thái tồn kho
                capNhatTrangThaiTonKho(thuoc.getMaThuoc());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi updateSoLuongThuoc cho mã " + thuoc.getMaThuoc() + ": " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }


    private void capNhatTrangThaiTonKho(String maThuoc) {
        String sql = """
            UPDATE Thuoc
            SET trangThaiTonKho = CASE
                WHEN SoLuong = 0 THEN 'HET_HANG'
                WHEN SoLuong <= tonKhoToiThieu AND SoLuong > 0 THEN 'SAP_HET'
                ELSE 'CON_HANG'
            END
            WHERE maThuoc = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maThuoc);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật trạng thái tồn kho cho thuốc " + maThuoc + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== XÓA ====================
    public boolean xoaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) return false;

        String sql = "DELETE FROM Thuoc WHERE maThuoc = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maThuoc);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi xoaThuoc: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ==================== MAP RESULTSET ====================
    private Thuoc mapRow(ResultSet rs) throws SQLException {
        Thuoc t = new Thuoc();
        t.setMaThuoc(rs.getString("maThuoc"));
        t.setTenThuoc(rs.getString("tenThuoc"));
        t.setGiaBan(rs.getDouble("giaBan"));
        t.setGiaNhap(rs.getDouble("giaNhap"));

        Date hsd = rs.getDate("hanSuDung");
        t.setHanSuDung(hsd != null ? hsd.toLocalDate() : null);

        t.setMaKho(rs.getString("maKho"));
        t.setMaNhaCungCap(rs.getString("maNhaCungCap"));

        String dvNhap = rs.getString("DonViNhapKho");
        t.setDonViNhapKho(dvNhap != null ? DonViTinh.valueOf(dvNhap) : null);

        String dvBan = rs.getString("DonViBan");
        t.setDonViBan(dvBan != null ? DonViTinh.valueOf(dvBan) : null);

        t.setTiLeDonVi(rs.getString("TiLeDonVi"));
        t.setSoLuong(rs.getInt("SoLuong"));

        String tt = rs.getString("trangThaiTonKho");
        t.setTrangThaiTonKho(tt != null ? TrangThaiTonKho.fromString(tt) : TrangThaiTonKho.CON_HANG);

        t.setTonKhoToiThieu(rs.getInt("tonKhoToiThieu"));
        t.setTonKhoToiDa(rs.getInt("tonKhoToiDa"));
        t.setLoaiThuoc(rs.getString("loaiThuoc"));
        t.setMaLoai(rs.getString("maLoai"));

        String pl = rs.getString("phanLoai");
        t.setPhanLoai(pl != null ? PhanLoaiThuoc.valueOf(pl) : PhanLoaiThuoc.OTC);

        return t;
    }

    // ==================== SET PARAM ====================
    private void setParams(PreparedStatement ps, Thuoc t, boolean insert) throws SQLException {
        int i = 1;

        if (insert) ps.setString(i++, t.getMaThuoc());

        ps.setString(i++, t.getTenThuoc());
        ps.setDouble(i++, t.getGiaBan());
        ps.setDouble(i++, t.getGiaNhap());

        LocalDate hsd = t.getHanSuDung();
        ps.setDate(i++, hsd != null ? Date.valueOf(hsd) : null);

        ps.setString(i++, t.getMaKho());
        ps.setString(i++, t.getMaNhaCungCap());

        ps.setString(i++, t.getDonViNhapKho() != null ? t.getDonViNhapKho().name() : null);
        ps.setString(i++, t.getDonViBan() != null ? t.getDonViBan().name() : null);
        ps.setString(i++, t.getTiLeDonVi());

        ps.setInt(i++, t.getSoLuong());
        ps.setString(i++, t.getTrangThaiTonKho() != null ? t.getTrangThaiTonKho().getDisplayName() : "CON_HANG");

        ps.setInt(i++, t.getTonKhoToiThieu());
        ps.setInt(i++, t.getTonKhoToiDa());

        ps.setString(i++, t.getLoaiThuoc());
        ps.setString(i++, t.getMaLoai());
        ps.setString(i++, t.getPhanLoai() != null ? t.getPhanLoai().name() : "OTC");
    }
}