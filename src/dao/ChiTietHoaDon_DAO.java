package dao;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.Thuoc;
import enums.DonViTinh;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDon_DAO {

    private final Thuoc_DAO thuocDAO = new Thuoc_DAO();

    public List<ChiTietHoaDon> getChiTietByMaHD(String maHD) {
        List<ChiTietHoaDon> ds = new ArrayList<>();

        String sql = """
        SELECT 
            cthd.maThuoc,
            cthd.soLuong,
            cthd.donGia,
            t.tenThuoc,
            t.donViBan
        FROM ChiTietHoaDon cthd
        JOIN Thuoc t ON cthd.maThuoc = t.maThuoc
        WHERE cthd.maHoaDon = ?
    """;

        // ❗ KHÔNG close connection
        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHD);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    // ===== Chi tiết hóa đơn =====
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setSoLuong(rs.getInt("soLuong"));
                    ct.setDonGia(rs.getBigDecimal("donGia"));

                    // ===== Thuốc =====
                    Thuoc thuoc = new Thuoc();
                    thuoc.setMaThuoc(rs.getString("maThuoc"));
                    thuoc.setTenThuoc(rs.getString("tenThuoc"));

                    // ENUM DonViTinh
                    try {
                        thuoc.setDonViBan(
                                DonViTinh.valueOf(rs.getString("donViBan"))
                        );
                    } catch (Exception e) {
                        // fallback nếu DB null / sai
                        thuoc.setDonViBan(DonViTinh.VIEN);
                    }

                    ct.setThuoc(thuoc);
                    ds.add(ct);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi getChiTietByMaHD: " + e.getMessage());
            e.printStackTrace();
        }

        return ds;
    }


    /* ================= THÊM CHI TIẾT HÓA ĐƠN ================= */
    public boolean addChiTietHoaDon(ChiTietHoaDon ct) {

        String sql = """
            INSERT INTO ChiTietHoaDon
            (maHoaDon, maThuoc, donGia, soLuong, thanhTien)
            VALUES (?, ?, ?, ?, ?)
        """;

        Connection conn = ConnectDB.getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getHoaDon().getMaHD());
            ps.setString(2, ct.getThuoc().getMaThuoc());

            BigDecimal donGia = ct.getDonGia()
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal thanhTien = donGia
                    .multiply(BigDecimal.valueOf(ct.getSoLuong()))
                    .setScale(2, RoundingMode.HALF_UP);

            ps.setBigDecimal(3, donGia);
            ps.setInt(4, ct.getSoLuong());
            ps.setBigDecimal(5, thanhTien);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Lỗi addChiTietHoaDon: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /* ================= CẬP NHẬT CHI TIẾT ================= */
    public boolean capNhatChiTiet(ChiTietHoaDon ct) {
        String sql = """
            UPDATE ChiTietHoaDon
            SET soLuong = ?, donGia = ?, thanhTien = ?
            WHERE maHoaDon = ? AND maThuoc = ?
        """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ct.getSoLuong());
            ps.setBigDecimal(2, ct.getDonGia());

            ps.setBigDecimal(3, ct.tinhThanhTien());

            ps.setString(4, ct.getHoaDon().getMaHD());
            ps.setString(5, ct.getThuoc().getMaThuoc());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi capNhatChiTiet: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
