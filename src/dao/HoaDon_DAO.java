// src/dao/HoaDon_DAO.java
package dao;

import connectDB.ConnectDB;
import entity.*;
import enums.PhuongThucThanhToan;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HoaDon_DAO {

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon hd " +
                "LEFT JOIN NhanVien nv ON hd.maNV = nv.maNV " +
                "LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH " +
                "LEFT JOIN KhuyenMai km ON hd.maKM = km.maKM";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("soDienThoai"),
                        rs.getString("email"),
                        rs.getString("chucVu"),
                        rs.getDouble("luongCoBan"),
                        rs.getDate("ngayVaoLam").toLocalDate(),
                        rs.getString("trangThai"),
                        rs.getString("matKhau")
                );

                // Similar for KhachHang and KhuyenMai...

                HoaDon hd = new HoaDon(
                        rs.getString("maHD"),
                        nv,
                        null, // KhachHang
                        null, // KhuyenMai
                        rs.getDate("ngayLap").toLocalDate(),
                        PhuongThucThanhToan.valueOf(rs.getString("phuongThucThanhToan"))
                );
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
