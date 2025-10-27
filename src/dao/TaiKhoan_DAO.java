package dao;

import connectDB.ConnectDB;
import entity.TaiKhoan;
import enums.PhanQuyen;
import enums.TrangThaiTaiKhoan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoan_DAO {

    // Lấy tất cả tài khoản
    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                TaiKhoan tk = mapResultSetToTaiKhoan(rs);
                danhSach.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // Tìm tài khoản theo tên đăng nhập
    public TaiKhoan getTaiKhoanTheoTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTaiKhoan(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Đăng nhập
    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ? AND trangThai = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);
            pstmt.setString(3, TrangThaiTaiKhoan.DANG_HOAT_DONG.name());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTaiKhoan(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm tài khoản mới
    public boolean themTaiKhoan(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan (tenDangNhap, matKhau, phanQuyen, trangThai) VALUES (?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tk.getTenDangNhap());
            pstmt.setString(2, tk.getMatKhau());
            pstmt.setString(3, tk.getQuyenTruyCap().name());
            pstmt.setString(4, tk.getTrangThai().name());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật tài khoản
    public boolean capNhatTaiKhoan(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET matKhau = ?, phanQuyen = ?, trangThai = ? WHERE tenDangNhap = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tk.getMatKhau());
            pstmt.setString(2, tk.getQuyenTruyCap().name());
            pstmt.setString(3, tk.getTrangThai().name());
            pstmt.setString(4, tk.getTenDangNhap());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Đổi mật khẩu
    public boolean doiMatKhau(String tenDangNhap, String matKhauMoi) {
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE tenDangNhap = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, matKhauMoi);
            pstmt.setString(2, tenDangNhap);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật trạng thái tài khoản
    public boolean capNhatTrangThai(String tenDangNhap, TrangThaiTaiKhoan trangThai) {
        String sql = "UPDATE TaiKhoan SET trangThai = ? WHERE tenDangNhap = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, trangThai.name());
            pstmt.setString(2, tenDangNhap);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa tài khoản (soft delete - chuyển trạng thái)
    public boolean xoaTaiKhoan(String tenDangNhap) {
        return capNhatTrangThai(tenDangNhap, TrangThaiTaiKhoan.KHOA);
    }

    // Lấy danh sách tài khoản theo phân quyền
    public List<TaiKhoan> getTaiKhoanTheoQuyen(PhanQuyen phanQuyen) {
        List<TaiKhoan> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan WHERE phanQuyen = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, phanQuyen.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                danhSach.add(mapResultSetToTaiKhoan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // Lấy danh sách tài khoản theo trạng thái
    public List<TaiKhoan> getTaiKhoanTheoTrangThai(TrangThaiTaiKhoan trangThai) {
        List<TaiKhoan> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan WHERE trangThai = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, trangThai.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                danhSach.add(mapResultSetToTaiKhoan(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }

    // Kiểm tra tên đăng nhập đã tồn tại
    public boolean kiemTraTenDangNhapTonTai(String tenDangNhap) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE tenDangNhap = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật phân quyền
    public boolean capNhatPhanQuyen(String tenDangNhap, PhanQuyen phanQuyen) {
        String sql = "UPDATE TaiKhoan SET phanQuyen = ? WHERE tenDangNhap = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, phanQuyen.name());
            pstmt.setString(2, tenDangNhap);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Helper: Map ResultSet to TaiKhoan
    private TaiKhoan mapResultSetToTaiKhoan(ResultSet rs) throws SQLException {
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(rs.getString("tenDangNhap"));
        tk.setMatKhau(rs.getString("matKhau"));

        String phanQuyen = rs.getString("phanQuyen");
        if (phanQuyen != null) {
            tk.setQuyenTruyCap(PhanQuyen.valueOf(phanQuyen));
        }

        String trangThai = rs.getString("trangThai");
        if (trangThai != null) {
            tk.setTrangThai(TrangThaiTaiKhoan.valueOf(trangThai));
        }

        return tk;
    }
}