package dao;

import connectDB.ConnectDB;
import entity.DonTraHang;
import entity.HoaDon;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DonTraHang_DAO {

    /* ======================= GET ALL ======================= */
    public List<DonTraHang> getAllDonTraHang() {
        List<DonTraHang> list = new ArrayList<>();

        String sql = """
            SELECT dth.maDonTra, dth.ngayTra, dth.lyDoTra, dth.trangThai,
                   hd.maHoaDon, hd.ngayLap
            FROM DonTraHang dth
            LEFT JOIN HoaDon hd ON dth.maHD = hd.maHoaDon
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = buildHoaDon(rs);

                DonTraHang dth = new DonTraHang(
                        rs.getString("maDonTra"),
                        rs.getDate("ngayTra") != null ? rs.getDate("ngayTra").toLocalDate() : null,
                        rs.getString("lyDoTra"),
                        hd,
                        rs.getBoolean("trangThai")
                );

                list.add(dth);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách đơn trả hàng", e);
        }

        return list;
    }

    /* ======================= ADD ======================= */
    public boolean themDonTraHang(DonTraHang dth) {
        String sql = """
            INSERT INTO DonTraHang
            (maDonTra, ngayTra, lyDoTra, maHD, trangThai)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dth.getMaDonTra());
            ps.setDate(2, dth.getNgayTra() != null ? Date.valueOf(dth.getNgayTra()) : null);
            ps.setString(3, dth.getLyDoTra());
            ps.setString(4, dth.getHoaDon() != null ? dth.getHoaDon().getMaHD() : null);
            ps.setBoolean(5, dth.isTrangThai());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm đơn trả hàng", e);
        }
    }

    /* ======================= UPDATE ======================= */
    public boolean capNhatDonTraHang(DonTraHang dth) {
        String sql = """
            UPDATE DonTraHang
            SET ngayTra = ?, lyDoTra = ?, maHD = ?, trangThai = ?
            WHERE maDonTra = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, dth.getNgayTra() != null ? Date.valueOf(dth.getNgayTra()) : null);
            ps.setString(2, dth.getLyDoTra());
            ps.setString(3, dth.getHoaDon() != null ? dth.getHoaDon().getMaHD() : null);
            ps.setBoolean(4, dth.isTrangThai());
            ps.setString(5, dth.getMaDonTra());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật đơn trả hàng", e);
        }
    }

    /* ======================= DELETE ======================= */
    public boolean xoaDonTraHang(String maDonTra) {
        String sql = "DELETE FROM DonTraHang WHERE maDonTra = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDonTra);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa đơn trả hàng", e);
        }
    }

    /* ======================= FIND BY ID ======================= */
    public DonTraHang timDonTraHangTheoMa(String maDonTra) {
        String sql = """
            SELECT dth.maDonTra, dth.ngayTra, dth.lyDoTra, dth.trangThai,
                   hd.maHoaDon, hd.ngayLap
            FROM DonTraHang dth
            LEFT JOIN HoaDon hd ON dth.maHD = hd.maHoaDon
            WHERE dth.maDonTra = ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maDonTra);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HoaDon hd = buildHoaDon(rs);

                    return new DonTraHang(
                            rs.getString("maDonTra"),
                            rs.getDate("ngayTra") != null ? rs.getDate("ngayTra").toLocalDate() : null,
                            rs.getString("lyDoTra"),
                            hd,
                            rs.getBoolean("trangThai")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm đơn trả hàng theo mã", e);
        }

        return null;
    }

    /* ======================= SEARCH ======================= */
    public List<DonTraHang> timKiemDonTraHang(String keyword) {
        List<DonTraHang> list = new ArrayList<>();

        String sql = """
            SELECT dth.maDonTra, dth.ngayTra, dth.lyDoTra, dth.trangThai,
                   hd.maHoaDon, hd.ngayLap
            FROM DonTraHang dth
            LEFT JOIN HoaDon hd ON dth.maHD = hd.maHoaDon
            WHERE dth.maDonTra LIKE ? OR hd.maHoaDon LIKE ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String k = "%" + keyword + "%";
            ps.setString(1, k);
            ps.setString(2, k);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = buildHoaDon(rs);

                    DonTraHang dth = new DonTraHang(
                            rs.getString("maDonTra"),
                            rs.getDate("ngayTra") != null ? rs.getDate("ngayTra").toLocalDate() : null,
                            rs.getString("lyDoTra"),
                            hd,
                            rs.getBoolean("trangThai")
                    );
                    list.add(dth);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi tìm kiếm đơn trả hàng", e);
        }

        return list;
    }

    /* ======================= COUNT ======================= */
    public int demTongDonTraHang() {
        String sql = "SELECT COUNT(*) FROM DonTraHang";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm đơn trả hàng", e);
        }
    }

    /* ======================= HELPER ======================= */
    private HoaDon buildHoaDon(ResultSet rs) throws SQLException {
        String maHD = rs.getString("maHoaDon");
        if (maHD == null) return null;

        HoaDon hd = new HoaDon();
        hd.setMaHD(maHD);

        Date ngayLap = rs.getDate("ngayLap");
        if (ngayLap != null) {
            hd.setNgayLap(ngayLap.toLocalDate());
        }

        return hd;
    }
}
