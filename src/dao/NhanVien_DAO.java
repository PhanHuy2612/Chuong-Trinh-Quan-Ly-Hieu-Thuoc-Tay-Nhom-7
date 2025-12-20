package dao;

import connectDB.ConnectDB;
import entity.NhanVien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO {

    private static final NhanVien_DAO instance = new NhanVien_DAO();
    public static NhanVien_DAO getInstance() {
        return instance;
    }
    NhanVien_DAO() {}
    public NhanVien getByMaNV(String maNV) {
        String sql = "SELECT maNhanVien, tenNhanVien, gioiTinh, ngaySinh, soDienThoai, hinhAnh_NV, diaChi, email FROM NhanVien WHERE maNhanVien = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNhanVien(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy nhân viên theo mã: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public String getTenNhanVienBySoDienThoai(String soDienThoai) {
        String sql = "SELECT tenNhanVien FROM NhanVien WHERE soDienThoai = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, soDienThoai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tenNhanVien");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi load tên nhân viên theo số điện thoại: " + e.getMessage());
        }
        return "Nhân viên (" + soDienThoai + ")";
    }

    public NhanVien timNhanVienTheoTen(String tenNV) {
        String sql = "SELECT * FROM NhanVien WHERE tenNhanVien = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNhanVien(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm nhân viên theo tên: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(mapResultSetToNhanVien(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
        return ds;
    }
    public List<NhanVien> getNhanVienKhongPhaiQuanLy() {
        List<NhanVien> list = new ArrayList<>();

        String sql = """
        SELECT nv.maNhanVien, nv.tenNhanVien, nv.gioiTinh, nv.ngaySinh,
               nv.soDienThoai, nv.hinhAnh_NV, nv.diaChi, nv.email
        FROM NhanVien nv
        LEFT JOIN TaiKhoan tk ON nv.maNhanVien = tk.maNhanVien
        WHERE tk.quyenTruyCap IS NULL
           OR tk.quyenTruyCap <> 'QUANLY'
    """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToNhanVien(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy nhân viên không phải quản lý", e);
        }

        return list;
    }


    public int demTongNhanVien() {
        String sql = "SELECT COUNT(*) FROM NhanVien";
        try (Connection con = ConnectDB.getInstance().getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            System.err.println("Lỗi đếm tổng nhân viên: " + e.getMessage());
        }
        return 0;
    }

    private NhanVien mapResultSetToNhanVien(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setMaNV(rs.getString("maNhanVien"));
        nv.setTenNV(rs.getString("tenNhanVien"));

        String gioiTinhDB = rs.getString("gioiTinh");
        boolean gioiTinh = false;
        if (gioiTinhDB != null) {
            gioiTinh = gioiTinhDB.equalsIgnoreCase("Nam") ||
                    gioiTinhDB.equals("1") ||
                    gioiTinhDB.equalsIgnoreCase("true");
        }
        nv.setGioiTinh(gioiTinh);

        Date date = rs.getDate("ngaySinh");
        if (date != null) {
            nv.setNgaySinh(date.toLocalDate());
        }

        nv.setSoDienThoai(rs.getString("soDienThoai"));
        nv.setHinhAnh(rs.getString("hinhAnh_NV"));
        nv.setDiaChi(rs.getString("diaChi"));

        try {
            nv.setEmail(rs.getString("email"));
        } catch (SQLException e) {
            System.err.println("Cảnh báo: Cột 'email' không tồn tại. Bỏ qua.");
            nv.setEmail(null);
        }

        return nv;
    }
    public NhanVien getBySoDienThoai(String soDienThoai) {
        String sql = """
        SELECT maNhanVien, tenNhanVien, gioiTinh, ngaySinh,
               soDienThoai, hinhAnh_NV, diaChi, email
        FROM NhanVien
        WHERE soDienThoai = ?
    """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, soDienThoai);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNhanVien(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lấy nhân viên theo số điện thoại: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

}