package dao;

import connectDB.ConnectDB;
import entity.KhoHang;
import enums.TrangThaiTonKho;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KhoHang_DAO {

    /**
     * Lấy danh sách tất cả kho hàng
     */
    public List<KhoHang> getAllKhoHang() {
        List<KhoHang> dsKho = new ArrayList<>();
        String sql = "SELECT maKho, ngayNhap, email, soLuong, trangThai FROM KhoHang";
        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhoHang kho = new KhoHang(
                        rs.getString("maKho"),
                        rs.getObject("ngayNhap", LocalDate.class),
                        rs.getString("email"),
                        rs.getInt("soLuong"),
                        TrangThaiTonKho.valueOf(rs.getString("trangThai"))
                );
                dsKho.add(kho);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKho;
    }

    /**
     * Thêm kho hàng mới
     */
    public boolean themKhoHang(KhoHang kho) {
        String sql = "INSERT INTO KhoHang (maKho, ngayNhap, email, soLuong, trangThai) VALUES (?, ?, ?, ?, ?)";
        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kho.getMaKho());
            ps.setObject(2, kho.getNgayNhap());
            ps.setString(3, kho.getEmail());
            ps.setInt(4, kho.getSoLuong());
            ps.setString(5, kho.getTrangThai().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin kho hàng
     */
    public boolean capNhatKhoHang(KhoHang kho) {
        String sql = "UPDATE KhoHang SET ngayNhap = ?, email = ?, soLuong = ?, trangThai = ? WHERE maKho = ?";
        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, kho.getNgayNhap());
            ps.setString(2, kho.getEmail());
            ps.setInt(3, kho.getSoLuong());
            ps.setString(4, kho.getTrangThai().name());
            ps.setString(5, kho.getMaKho());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa kho hàng theo mã
     */
    public boolean xoaKhoHang(String maKho) {
        String sql = "DELETE FROM KhoHang WHERE maKho = ?";
        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKho);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy thông tin kho hàng theo mã
     */
    public KhoHang getKhoHangTheoMa(String maKho) {
        String sql = "SELECT * FROM KhoHang WHERE maKho = ?";
        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKho);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhoHang(
                            rs.getString("maKho"),
                            rs.getObject("ngayNhap", LocalDate.class),
                            rs.getString("email"),
                            rs.getInt("soLuong"),
                            TrangThaiTonKho.valueOf(rs.getString("trangThai"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách mã kho (dùng cho ComboBox)
     */
    public List<String> getDanhSachMaKho() {
        List<String> dsMa = new ArrayList<>();
        String sql = "SELECT maKho FROM KhoHang";
        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsMa.add(rs.getString("maKho"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsMa;
    }

    /**
     * Cập nhật trạng thái tồn kho tự động dựa trên số lượng mới
     */
    public boolean capNhatTrangThaiTuDong(String maKho, int soLuongMoi) {
        TrangThaiTonKho trangThai = (soLuongMoi == 0)
                ? TrangThaiTonKho.HET_HANG
                : (soLuongMoi <= 50 ? TrangThaiTonKho.SAP_HET_HANG : TrangThaiTonKho.CON_HANG);

        String sql = "UPDATE KhoHang SET soLuong = ?, trangThai = ? WHERE maKho = ?";
        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, soLuongMoi);
            ps.setString(2, trangThai.name());
            ps.setString(3, maKho);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getTenKhoByMa(String maKho) {
        String sql = "SELECT tenKho FROM KhoHang WHERE maKho = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKho);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tenKho");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maKho; // fallback: trả về mã nếu không tìm thấy tên
    }
}
