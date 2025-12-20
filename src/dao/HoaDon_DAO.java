package dao;

import connectDB.ConnectDB;
import entity.*;
import enums.PhuongThucThanhToan;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO {

    private final NhanVien_DAO nhanVienDAO = NhanVien_DAO.getInstance();
    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    private final ChiTietHoaDon_DAO chiTietDAO = new ChiTietHoaDon_DAO();

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> ds = new ArrayList<>();

        String sql = """
        SELECT 
            hd.maHoaDon, hd.ngayLap, hd.phuongThucThanhToan,
            hd.thueVAT, hd.tienGiam, hd.tongTien,

            nv.maNhanVien, nv.tenNhanVien,
            kh.maKhachHang, kh.hoTen
        FROM HoaDon hd
        LEFT JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien
        LEFT JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang
    """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(mapResultSetToHoaDon(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }


    public boolean addHoaDon(HoaDon hd) {
        String sql = """
        INSERT INTO HoaDon
        (maHoaDon, maNhanVien, maKhachHang,
         ngayLap, phuongThucThanhToan,
         thueVAT, tienGiam, tongTien)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHD());
            ps.setString(2,
                    hd.getNhanVien() != null
                            ? hd.getNhanVien().getMaNV()
                            : null
            );

            ps.setString(3,
                    hd.getKhachHang() != null
                            ? hd.getKhachHang().getMaKH()
                            : "KHVL0000"
            );

            ps.setDate(4,
                    hd.getNgayLap() != null
                            ? Date.valueOf(hd.getNgayLap())
                            : Date.valueOf(LocalDate.now())
            );

            ps.setString(5, hd.getPhuongThucThanhToan().name());
            ps.setBigDecimal(6, hd.getThueVAT());

            // ✅ RÀNG BUỘC KHÔNG BAO GIỜ NULL
            ps.setBigDecimal(7,
                    hd.getTienGiam() != null ? hd.getTienGiam() : BigDecimal.ZERO);

            ps.setBigDecimal(8, hd.getTongTien());

            if (ps.executeUpdate() <= 0)
                return false;

            for (ChiTietHoaDon ct : hd.getDanhSachCTHD()) {
                ct.setHoaDon(hd);
                if (!chiTietDAO.addChiTietHoaDon(ct))
                    return false;
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    public boolean capNhatHoaDon(HoaDon hd) {

        hd.tinhTongThanhToan();

        String sql = """
            UPDATE HoaDon
            SET maNhanVien = ?, maKhachHang = ?,
                ngayLap = ?, phuongThucThanhToan = ?,
                thueVAT = ?, tienGiam = ?, tongTien = ?
            WHERE maHoaDon = ?
        """;

        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,
                    hd.getNhanVien() != null
                            ? hd.getNhanVien().getMaNV()
                            : null
            );

            ps.setString(2,
                    hd.getKhachHang() != null
                            ? hd.getKhachHang().getMaKH()
                            : "KHVL0000"
            );

            ps.setDate(3,
                    hd.getNgayLap() != null
                            ? Date.valueOf(hd.getNgayLap())
                            : Date.valueOf(LocalDate.now())
            );

            ps.setString(4, hd.getPhuongThucThanhToan().name());
            ps.setBigDecimal(5, hd.getThueVAT());
            ps.setBigDecimal(6, hd.getTienGiam());
            ps.setBigDecimal(7, hd.getTongTien());
            ps.setString(8, hd.getMaHD());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean xoaHoaDon(String maHD) {
        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";
        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public BigDecimal getTongChiTieu(String maKH) {

        String sql = """
            SELECT COALESCE(SUM(tongTien), 0)
            FROM HoaDon
            WHERE maKhachHang = ?
        """;

        Connection con = ConnectDB.getConnection();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maKH);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getBigDecimal(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    private HoaDon mapResultSetToHoaDon(ResultSet rs) throws SQLException {

        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("maHoaDon"));

        Date ngayLap = rs.getDate("ngayLap");
        if (ngayLap != null)
            hd.setNgayLap(ngayLap.toLocalDate());

        String pttt = rs.getString("phuongThucThanhToan");
        if (pttt != null)
            hd.setPhuongThucThanhToan(PhuongThucThanhToan.valueOf(pttt));

        hd.setThueVAT(rs.getBigDecimal("thueVAT"));
        hd.setTienGiam(rs.getBigDecimal("tienGiam"));
        hd.setTongTien(rs.getBigDecimal("tongTien"));

        String maNV = rs.getString("maNhanVien");
        if (maNV != null) {
            NhanVien nv = new NhanVien();
            nv.setMaNV(maNV);
            nv.setTenNV(rs.getString("tenNhanVien"));
            hd.setNhanVien(nv);
        }

        String maKH = rs.getString("maKhachHang");
        if (maKH != null) {
            KhachHang kh = new KhachHang();
            kh.setMaKH(maKH);
            kh.setTenKH(rs.getString("hoTen"));
            hd.setKhachHang(kh);
        }

        hd.setDanhSachCTHD(new ArrayList<>());

        return hd;
    }

}
