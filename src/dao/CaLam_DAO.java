package dao;

import connectDB.ConnectDB;
import entity.CaLam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaLam_DAO {

    private Connection ensureConnection() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            System.out.println("🔁 Kết nối lại SQL Server trong CaLam_DAO...");
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        return con;
    }

    public List<CaLam> getAllCaLam() {
        List<CaLam> list = new ArrayList<>();
        String sql = """
            SELECT maCaLam, tenCaLam, ngayLamViec, gioBatDau, gioKetThuc, viTriLamViec, 
                   soLuongNhanVienCan, ghiChu 
            FROM CaLam
        """;

        try (Connection con = ensureConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new CaLam(
                        rs.getString("maCaLam"),
                        rs.getString("tenCaLam"),
                        rs.getDate("ngayLamViec").toLocalDate(),
                        rs.getTime("gioBatDau").toLocalTime(),
                        rs.getTime("gioKetThuc").toLocalTime(),
                        rs.getString("viTriLamViec"),
                        rs.getInt("soLuongNhanVienCan"),
                        rs.getString("ghiChu")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách ca làm: " + e.getMessage(), e);
        }
        return list;
    }
    public boolean themCaLam(CaLam ca) {
        String sql = """
            INSERT INTO CaLam (maCaLam, tenCaLam, ngayLamViec, gioBatDau, gioKetThuc, 
                               viTriLamViec, soLuongNhanVienCan, ghiChu)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = ensureConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ca.getMaCaLam());
            ps.setString(2, ca.getTenCaLam());
            ps.setDate(3, Date.valueOf(ca.getNgayLamViec()));
            ps.setTime(4, Time.valueOf(ca.getGioBatDau()));
            ps.setTime(5, Time.valueOf(ca.getGioKetThuc()));
            ps.setString(6, ca.getViTriLamViec());
            ps.setInt(7, ca.getSoLuongNhanVienCan());
            ps.setString(8, ca.getGhiChu());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm ca làm: " + e.getMessage(), e);
        }
    }
    public boolean capNhatCaLam(CaLam ca) {
        String sql = """
            UPDATE CaLam SET tenCaLam=?, ngayLamViec=?, gioBatDau=?, gioKetThuc=?, 
                             viTriLamViec=?, soLuongNhanVienCan=?, ghiChu=? 
            WHERE maCaLam=?
        """;

        try (Connection con = ensureConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ca.getTenCaLam());
            ps.setDate(2, Date.valueOf(ca.getNgayLamViec()));
            ps.setTime(3, Time.valueOf(ca.getGioBatDau()));
            ps.setTime(4, Time.valueOf(ca.getGioKetThuc()));
            ps.setString(5, ca.getViTriLamViec());
            ps.setInt(6, ca.getSoLuongNhanVienCan());
            ps.setString(7, ca.getGhiChu());
            ps.setString(8, ca.getMaCaLam());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật ca làm: " + e.getMessage(), e);
        }
    }
    public boolean xoaCaLam(String maCaLam) {
        String sql = "DELETE FROM CaLam WHERE maCaLam = ?";

        try (Connection con = ensureConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maCaLam);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa ca làm: " + e.getMessage(), e);
        }
    }

    public CaLam timCaLamTheoMa(String maCaLam) {
        String sql = """
            SELECT maCaLam, tenCaLam, ngayLamViec, gioBatDau, gioKetThuc, 
                   viTriLamViec, soLuongNhanVienCan, ghiChu
            FROM CaLam WHERE maCaLam = ?
        """;

        try (Connection con = ensureConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maCaLam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CaLam(
                            rs.getString("maCaLam"),
                            rs.getString("tenCaLam"),
                            rs.getDate("ngayLamViec").toLocalDate(),
                            rs.getTime("gioBatDau").toLocalTime(),
                            rs.getTime("gioKetThuc").toLocalTime(),
                            rs.getString("viTriLamViec"),
                            rs.getInt("soLuongNhanVienCan"),
                            rs.getString("ghiChu")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm ca làm: " + e.getMessage(), e);
        }
        return null;
    }
    public List<CaLam> timKiemCaLam(String keyword) {
        List<CaLam> list = new ArrayList<>();
        String sql = """
            SELECT * FROM CaLam 
            WHERE maCaLam LIKE ? OR tenCaLam LIKE ? OR viTriLamViec LIKE ?
        """;

        try (Connection con = ensureConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CaLam(
                            rs.getString("maCaLam"),
                            rs.getString("tenCaLam"),
                            rs.getDate("ngayLamViec").toLocalDate(),
                            rs.getTime("gioBatDau").toLocalTime(),
                            rs.getTime("gioKetThuc").toLocalTime(),
                            rs.getString("viTriLamViec"),
                            rs.getInt("soLuongNhanVienCan"),
                            rs.getString("ghiChu")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm kiếm ca làm: " + e.getMessage(), e);
        }
        return list;
    }
    public int demTongCaLam() {
        String sql = "SELECT COUNT(*) FROM CaLam";

        try (Connection con = ensureConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm ca làm: " + e.getMessage(), e);
        }
        return 0;
    }
    public String sinhMaCaLam() {
        String sql = "SELECT MAX(maCaLam) FROM CaLam";

        try (Connection con = ensureConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String maxMa = rs.getString(1);

                if (maxMa != null) {
                    int so = Integer.parseInt(maxMa.replaceAll("\\D", ""));
                    return String.format("CA%03d", so + 1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi sinh mã ca làm: " + e.getMessage(), e);
        }

        return "CA001";
    }
}
