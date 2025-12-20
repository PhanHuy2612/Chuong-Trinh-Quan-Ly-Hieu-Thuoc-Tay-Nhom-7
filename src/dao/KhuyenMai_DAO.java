package dao;

import connectDB.ConnectDB;
import entity.KhuyenMai;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMai_DAO {

    private final Connection conn;

    public KhuyenMai_DAO() {
        conn = ConnectDB.getInstance().getConnection();
    }

    /**
     * Lấy khuyến mãi theo mã
     */
    public KhuyenMai getByMaKM(String maKM) {
        String sql = """
            SELECT maKhuyenMai, tenKhuyenMai, phanTramGiam,
                   thoiGianBatDau, thoiGianKetThuc,
                   maNhanVien, ngayTao, tienGiamToiDa, loaiThuocApDung
            FROM KhuyenMai
            WHERE maKhuyenMai = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhuyenMai(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy tất cả khuyến mãi
     */
    public List<KhuyenMai> getAllKhuyenMai() {
        List<KhuyenMai> list = new ArrayList<>();

        String sql = """
        SELECT maKhuyenMai, tenKhuyenMai, phanTramGiam,
               thoiGianBatDau, thoiGianKetThuc,
               maNhanVien, ngayTao, tienGiamToiDa, loaiThuocApDung
        FROM KhuyenMai
        ORDER BY thoiGianBatDau DESC
    """;

        try (Connection conn = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToKhuyenMai(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    /**
     * Lấy danh sách khuyến mãi ĐANG HOẠT ĐỘNG (ngày hiện tại nằm trong khoảng)
     * Rất quan trọng để áp dụng khi bán thuốc
     */
    public List<KhuyenMai> getKhuyenMaiDangHoatDong() {
        List<KhuyenMai> list = new ArrayList<>();

        String sql = """
            SELECT maKhuyenMai, tenKhuyenMai, phanTramGiam,
                   thoiGianBatDau, thoiGianKetThuc,
                   maNhanVien, ngayTao, tienGiamToiDa, loaiThuocApDung
            FROM KhuyenMai
            WHERE GETDATE() BETWEEN thoiGianBatDau AND thoiGianKetThuc
            ORDER BY phanTramGiam DESC
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToKhuyenMai(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Hàm hỗ trợ map ResultSet → KhuyenMai (tránh lặp code)
     */
    private KhuyenMai mapResultSetToKhuyenMai(ResultSet rs) throws SQLException {
        KhuyenMai km = new KhuyenMai();

        km.setMaKM(rs.getString("maKhuyenMai"));
        km.setTenKM(rs.getString("tenKhuyenMai"));
        km.setPhanTramGiam(rs.getDouble("phanTramGiam"));

        // Ngày bắt đầu / kết thúc
        Date bdSql = rs.getDate("thoiGianBatDau");
        Date ktSql = rs.getDate("thoiGianKetThuc");
        if (bdSql != null) km.setNgayBatDau(bdSql.toLocalDate());
        if (ktSql != null) km.setNgayKetThuc(ktSql.toLocalDate());

        // Các cột mới
        km.setMaNhanVien(rs.getString("maNhanVien"));

        Timestamp ngayTaoTs = rs.getTimestamp("ngayTao");
        if (ngayTaoTs != null) {
            km.setNgayTao(ngayTaoTs.toLocalDateTime());
        }

        Object tienGiamObj = rs.getObject("tienGiamToiDa");
        if (tienGiamObj != null) {
            km.setTienGiamToiDa(new BigDecimal(tienGiamObj.toString()));
        } else {
            km.setTienGiamToiDa(null);
        }

        km.setLoaiThuocApDung(rs.getString("loaiThuocApDung"));

        return km;
    }
}