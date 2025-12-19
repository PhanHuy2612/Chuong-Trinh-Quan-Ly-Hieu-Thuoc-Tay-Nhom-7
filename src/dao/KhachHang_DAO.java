package dao;

import connectDB.ConnectDB;
import entity.KhachHang;
import enums.LoaiKhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHang_DAO {

    public KhachHang_DAO() {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new IllegalStateException("Không thể kết nối CSDL", e);
        }
    }

    /* ======================= LẤY TẤT CẢ KHÁCH HÀNG ======================= */
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> ds = new ArrayList<>();

        String sql = """
            SELECT maKhachHang, hoTen, gioiTinh, loaiKhachHang,
                   diemTichLuy, soDienThoai, diemDoiThuong
            FROM KhachHang
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(mapResultSetToKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /* ======================= LẤY THEO MÃ KH ======================= */
    public KhachHang getByMaKH(String maKH) {
        if (maKH == null || maKH.isBlank()) return null;

        String sql = """
            SELECT maKhachHang, hoTen, gioiTinh, loaiKhachHang,
                   diemTichLuy, soDienThoai, diemDoiThuong
            FROM KhachHang
            WHERE maKhachHang = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ======================= LẤY THEO SĐT ======================= */
    public KhachHang getBySoDienThoai(String sdt) {
        if (sdt == null || sdt.isBlank()) return null;

        String sql = """
            SELECT maKhachHang, hoTen, gioiTinh, loaiKhachHang,
                   diemTichLuy, soDienThoai, diemDoiThuong
            FROM KhachHang
            WHERE soDienThoai = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ======================= THÊM KHÁCH HÀNG ======================= */
    public boolean addKhachHang(KhachHang kh) {
        String sql = """
            INSERT INTO KhachHang
            (maKhachHang, hoTen, gioiTinh, loaiKhachHang,
             diemTichLuy, soDienThoai, diemDoiThuong)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setBoolean(3, kh.isGioiTinh());
            ps.setString(4, kh.getLoaiKhachHang().name());
            ps.setInt(5, kh.getDiemTichLuy());
            ps.setString(6, kh.getSoDienThoai());
            ps.setInt(7, kh.getDiemDoiThuong());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ======================= CẬP NHẬT KHÁCH HÀNG ======================= */
    public boolean updateKhachHang(KhachHang kh) {
        String sql = """
            UPDATE KhachHang
            SET hoTen = ?, gioiTinh = ?, loaiKhachHang = ?,
                diemTichLuy = ?, soDienThoai = ?, diemDoiThuong = ?
            WHERE maKhachHang = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, kh.getTenKH());
            ps.setBoolean(2, kh.isGioiTinh());
            ps.setString(3, kh.getLoaiKhachHang().name());
            ps.setInt(4, kh.getDiemTichLuy());
            ps.setString(5, kh.getSoDienThoai());
            ps.setInt(6, kh.getDiemDoiThuong());
            ps.setString(7, kh.getMaKH());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ======================= XÓA KHÁCH HÀNG ======================= */
    public boolean deleteKhachHang(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKhachHang = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ======================= TÌM KIẾM ======================= */
    public List<KhachHang> searchKhachHang(String keyword) {
        if (keyword == null || keyword.isBlank()) return getAllKhachHang();

        List<KhachHang> ds = new ArrayList<>();

        String sql = """
            SELECT maKhachHang, hoTen, gioiTinh, loaiKhachHang,
                   diemTichLuy, soDienThoai, diemDoiThuong
            FROM KhachHang
            WHERE hoTen LIKE ? OR soDienThoai LIKE ? OR maKhachHang LIKE ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String k = "%" + keyword.trim() + "%";
            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapResultSetToKhachHang(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /* ======================= CẬP NHẬT ĐIỂM ======================= */
    public boolean updateDiem(KhachHang kh) {
        String sql = """
            UPDATE KhachHang
            SET diemTichLuy = ?, diemDoiThuong = ?, loaiKhachHang = ?
            WHERE maKhachHang = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, kh.getDiemTichLuy());
            ps.setInt(2, kh.getDiemDoiThuong());
            ps.setString(3, kh.getLoaiKhachHang().name());
            ps.setString(4, kh.getMaKH());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getMaxMaKH() {
        String sql = "SELECT MAX(maKhachHang) FROM KhachHang WHERE maKhachHang LIKE 'KHT%'";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String max = rs.getString(1);
                if (max != null && max.startsWith("KHT")) {
                    return max;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Không có mã KHT nào → bắt đầu từ KHT0001
    }

    private KhachHang mapResultSetToKhachHang(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();

        kh.setMaKH(rs.getString("maKhachHang"));
        kh.setTenKH(rs.getString("hoTen"));
        kh.setGioiTinh(rs.getBoolean("gioiTinh"));
        kh.setSoDienThoai(rs.getString("soDienThoai"));

        // set điểm trực tiếp (không kích hoạt logic cộng điểm)
        kh.setDiemTichLuy(rs.getInt("diemTichLuy"));
        kh.setDiemDoiThuong(rs.getInt("diemDoiThuong"));

        String loai = rs.getString("loaiKhachHang");
        try {
            kh.setLoaiKhachHang(LoaiKhachHang.valueOf(loai));
        } catch (Exception e) {
            kh.setLoaiKhachHang(LoaiKhachHang.BINHTHUONG);
        }

        return kh;
    }
}
