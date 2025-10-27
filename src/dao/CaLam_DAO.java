package dao;

import connectDB.ConnectDB;
import entity.CaLam;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CaLam_DAO {

    public List<CaLam> getAllCaLam() {
        List<CaLam> list = new ArrayList<>();
        String sql = "SELECT maCa, tenCa, thoiGianBatDau, thoiGianKetThuc FROM CaLam";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CaLam ca = new CaLam(
                        rs.getString("maCa"),
                        rs.getString("tenCa"),
                        rs.getTime("thoiGianBatDau").toLocalTime(),
                        rs.getTime("thoiGianKetThuc").toLocalTime()
                );
                list.add(ca);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách ca làm: " + e.getMessage(), e);
        }
        return list;
    }

    public boolean themCaLam(CaLam ca) {
        String sql = "INSERT INTO CaLam (maCa, tenCa, thoiGianBatDau, thoiGianKetThuc) VALUES (?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, ca.getMaCa());
            pstmt.setString(2, ca.getTenCa());
            pstmt.setTime(3, Time.valueOf(ca.getThoiGianBatDau()));
            pstmt.setTime(4, Time.valueOf(ca.getThoiGianKetThuc()));

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm ca làm: " + e.getMessage(), e);
        }
    }

    public boolean capNhatCaLam(CaLam ca) {
        String sql = "UPDATE CaLam SET tenCa = ?, thoiGianBatDau = ?, thoiGianKetThuc = ? WHERE maCa = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, ca.getTenCa());
            pstmt.setTime(2, Time.valueOf(ca.getThoiGianBatDau()));
            pstmt.setTime(3, Time.valueOf(ca.getThoiGianKetThuc()));
            pstmt.setString(4, ca.getMaCa());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật ca làm: " + e.getMessage(), e);
        }
    }

    public boolean xoaCaLam(String maCa) {
        String sql = "DELETE FROM CaLam WHERE maCa = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, maCa);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa ca làm: " + e.getMessage(), e);
        }
    }

    public CaLam timCaLamTheoMa(String maCa) {
        String sql = "SELECT maCa, tenCa, thoiGianBatDau, thoiGianKetThuc FROM CaLam WHERE maCa = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, maCa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CaLam(
                            rs.getString("maCa"),
                            rs.getString("tenCa"),
                            rs.getTime("thoiGianBatDau").toLocalTime(),
                            rs.getTime("thoiGianKetThuc").toLocalTime()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm ca làm theo mã: " + e.getMessage(), e);
        }
        return null;
    }

    public List<CaLam> timKiemCaLam(String keyword) {
        List<CaLam> list = new ArrayList<>();
        String sql = "SELECT maCa, tenCa, thoiGianBatDau, thoiGianKetThuc FROM CaLam WHERE tenCa LIKE ? OR maCa LIKE ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CaLam ca = new CaLam(
                            rs.getString("maCa"),
                            rs.getString("tenCa"),
                            rs.getTime("thoiGianBatDau").toLocalTime(),
                            rs.getTime("thoiGianKetThuc").toLocalTime()
                    );
                    list.add(ca);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm kiếm ca làm: " + e.getMessage(), e);
        }
        return list;
    }

    public int demTongCaLam() {
        String sql = "SELECT COUNT(*) FROM CaLam";
        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm tổng số ca làm: " + e.getMessage(), e);
        }
        return 0;
    }
}