// src/dao/TaiKhoan_DAO.java
package dao;

import connectDB.ConnectDB;
import entity.TaiKhoan;
import enums.PhanQuyen;
import enums.TrangThaiTaiKhoan;

import java.sql.*;

public class TaiKhoan_DAO {

    // 1. Đăng nhập (đã có)
    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ?";
        return getTaiKhoanByQuery(sql, tenDangNhap, matKhau);
    }

    // 2. Tìm tài khoản theo tên đăng nhập (dùng cho quên mật khẩu)
    public TaiKhoan timTheoTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";
        return getTaiKhoanByQuery(sql, tenDangNhap);
    }

    // 3. Đổi mật khẩu (rất quan trọng cho chức năng quên mật khẩu)
    public boolean doiMatKhau(String tenDangNhap, String matKhauMoi) {
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE tenDangNhap = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, matKhauMoi);
            ps.setString(2, tenDangNhap);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Lấy email của tài khoản (nếu bạn có cột email)
    public String layEmailTheoTenDangNhap(String tenDangNhap) {
        String sql = "SELECT email FROM TaiKhoan WHERE tenDangNhap = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 5. Kiểm tra tài khoản có tồn tại và đang hoạt động không
    public boolean tonTaiVaHoatDong(String tenDangNhap) {
        String sql = "SELECT trangThaiTaiKhoan FROM TaiKhoan WHERE tenDangNhap = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String trangThai = rs.getString("trangThaiTaiKhoan");
                    return "DANGHOATDONG".equals(trangThai);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // =================================================================
    // Phương thức hỗ trợ chung - tránh lặp code
    // =================================================================
    private TaiKhoan getTaiKhoanByQuery(String sql, Object... params) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setTenDangNhap(rs.getString("tenDangNhap"));
                    tk.setMatKhau(rs.getString("matKhau"));
                    tk.setMaNhanVien(rs.getString("maNhanVien"));
                    tk.setEmail(rs.getString("email")); // nếu có cột email

                    // Trang thái
                    String tt = rs.getString("trangThaiTaiKhoan");
                    tk.setTrangThai(TrangThaiTaiKhoan.valueOf(tt != null ? tt : "KHOA"));

                    // Phân quyền
                    String quyen = rs.getString("quyenTruyCap");
                    tk.setPhanQuyen("QUANLY".equals(quyen) ? PhanQuyen.QUAN_LY : PhanQuyen.DUOC_SI);

                    return tk;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public TaiKhoan getTaiKhoanByTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setTenDangNhap(rs.getString("tenDangNhap"));
                    tk.setMatKhau(rs.getString("matKhau"));
                    tk.setMaNhanVien(rs.getString("maNhanVien"));
                    tk.setTrangThai(TrangThaiTaiKhoan.valueOf(rs.getString("trangThaiTaiKhoan")));

                    String quyenDB = rs.getString("quyenTruyCap");
                    tk.setPhanQuyen("QUANLY".equals(quyenDB) ? PhanQuyen.QUAN_LY : PhanQuyen.DUOC_SI);

                    return tk;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}