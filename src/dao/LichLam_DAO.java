package dao;

import connectDB.ConnectDB;
import entity.CaLam;
import entity.LichLam;
import entity.NhanVien;
import enums.TrangThaiCaLam;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LichLam_DAO {

    private final CaLam_DAO caLamDAO = new CaLam_DAO();
    private final NhanVien_DAO nhanVienDAO = NhanVien_DAO.getInstance();

    /**
     * Lấy toàn bộ lịch làm, JOIN với CaLam và NhanVien để có tên đầy đủ
     */
    public List<LichLam> getAllLichLam() {
        List<LichLam> list = new ArrayList<>();
        String sql = """
    SELECT 
        ll.maLichLam, ll.ngayLam, ll.maCaLam, ll.maNhanVien, 
        ll.trangThaiCaLam, ll.ghiChu,
        cl.tenCaLam,
        cl.gioBatDau,
        cl.gioKetThuc,
        cl.viTriLamViec,
        nv.tenNhanVien
    FROM LichLam ll
    LEFT JOIN CaLam cl ON ll.maCaLam = cl.maCaLam
    LEFT JOIN NhanVien nv ON ll.maNhanVien = nv.maNhanVien
    """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LichLam lich = mapResultSetToLichLam(rs);
                list.add(lich);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách lịch làm: " + e.getMessage(), e);
        }
        return list;
    }

    public boolean themLichLam(LichLam lich) {
        String sql = """
        INSERT INTO LichLam 
        (ngayLam, maCaLam, maNhanVien, trangThaiCaLam, ghiChu)
        VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(lich.getNgayLam()));
            ps.setString(2, lich.getCaLam().getMaCaLam());
            ps.setString(3, lich.getNhanVien().getMaNV());
            ps.setString(4, lich.getTrangThaiCaLam().name());
            ps.setString(5, lich.getGhiChu());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm lịch làm: " + e.getMessage());
            return false;
        }
    }


    public boolean capNhatLichLam(LichLam lich) {
        String sql = """
            UPDATE LichLam SET trangThaiCaLam = ?, ghiChu = ? 
            WHERE maLichLam = ?
            """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, lich.getTrangThaiCaLam() != null ? lich.getTrangThaiCaLam().name() : null);
            ps.setString(2, lich.getGhiChu());
            ps.setInt(3, lich.getMaLichLam());


            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật lịch làm: " + e.getMessage(), e);
        }
    }

    public boolean xoaLichLam(int maLichLam) {
        String sql = "DELETE FROM LichLam WHERE maLichLam = ?";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, maLichLam);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa lịch làm: " + e.getMessage(), e);
        }
    }


    public List<LichLam> timLichLamTheoCaLam(String maCaLam) {
        List<LichLam> list = new ArrayList<>();
        String sql = """
            SELECT 
                ll.maLichLam, ll.ngayLam, ll.maCaLam, ll.maNhanVien, 
                ll.trangThaiCaLam, ll.ghiChu,
                cl.tenCaLam,
                nv.tenNhanVien
            FROM LichLam ll
            LEFT JOIN CaLam cl ON ll.maCaLam = cl.maCaLam
            LEFT JOIN NhanVien nv ON ll.maNhanVien = nv.maNhanVien
            WHERE ll.maCaLam = ?
            """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maCaLam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToLichLam(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm lịch làm theo ca: " + e.getMessage(), e);
        }
        return list;
    }

    public List<LichLam> timLichLamTheoNhanVien(String maNV) {
        List<LichLam> list = new ArrayList<>();
        String sql = """
            SELECT 
                ll.maLichLam, ll.ngayLam, ll.maCaLam, ll.maNhanVien, 
                ll.trangThaiCaLam, ll.ghiChu,
                cl.tenCaLam,
                nv.tenNhanVien
            FROM LichLam ll
            LEFT JOIN CaLam cl ON ll.maCaLam = cl.maCaLam
            LEFT JOIN NhanVien nv ON ll.maNhanVien = nv.maNhanVien
            WHERE ll.maNhanVien = ?
            """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToLichLam(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm lịch làm theo NV: " + e.getMessage(), e);
        }
        return list;
    }

    public int countCaSapToi() {
        String sql = "SELECT COUNT(*) FROM LichLam l JOIN CaLam c ON l.maCaLam = c.maCaLam WHERE c.ngayLamViec = ?";
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(tomorrow));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm ca sắp tới: " + e.getMessage(), e);
        }
        return 0;
    }

    public int countNhanVienPhanCong() {
        String sql = "SELECT COUNT(*) FROM LichLam";
        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm lượt phân công: " + e.getMessage(), e);
        }
        return 0;
    }


    public boolean xoaTheoMaCa(String maCaLam) {
        String sql = "DELETE FROM LichLam WHERE maCaLam = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maCaLam);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa lịch theo ca: " + e.getMessage());
            return false;
        }
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM LichLam";
        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.err.println("Lỗi đếm lịch làm: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Map ResultSet sang LichLam, đã có tên ca và tên nhân viên từ JOIN
     */
    private LichLam mapResultSetToLichLam(ResultSet rs) throws SQLException {

        LichLam lich = new LichLam();

        lich.setMaLichLam(rs.getInt("maLichLam"));
        lich.setNgayLam(rs.getDate("ngayLam").toLocalDate());
        lich.setGhiChu(rs.getString("ghiChu"));

        String trangThai = rs.getString("trangThaiCaLam");
        lich.setTrangThaiCaLam(
                trangThai != null
                        ? TrangThaiCaLam.valueOf(trangThai)
                        : TrangThaiCaLam.CHUA_BAT_DAU
        );

        CaLam ca = new CaLam();
        ca.setMaCaLam(rs.getString("maCaLam"));
        ca.setTenCaLam(rs.getString("tenCaLam"));

        Time gioBatDau = rs.getTime("gioBatDau");
        Time gioKetThuc = rs.getTime("gioKetThuc");

        ca.setGioBatDau(gioBatDau != null ? gioBatDau.toLocalTime() : null);
        ca.setGioKetThuc(gioKetThuc != null ? gioKetThuc.toLocalTime() : null);
        ca.setViTriLamViec(rs.getString("viTriLamViec"));

        lich.setCaLam(ca);

        NhanVien nv = new NhanVien();
        nv.setMaNV(rs.getString("maNhanVien"));
        nv.setTenNV(rs.getString("tenNhanVien"));

        lich.setNhanVien(nv);
        return lich;
    }



}