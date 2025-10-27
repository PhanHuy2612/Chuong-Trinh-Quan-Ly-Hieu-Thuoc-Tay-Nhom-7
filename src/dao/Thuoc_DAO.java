package dao;

import connectDB.ConnectDB;
import entity.*;
import enums.DonViTinh;
import enums.TrangThaiTonKho;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Thuoc_DAO {

    public List<Thuoc> getAllThuoc() {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT * FROM Thuoc t " +
                "LEFT JOIN NhaCungCap ncc ON t.maNCC = ncc.maNCC " +
                "LEFT JOIN KhoHang kh ON t.maKho = kh.maKho";

        try (Connection con = ConnectDB.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap(
                        rs.getString("maNCC"),
                        rs.getString("tenNCC"),
                        rs.getString("diaChi"),
                        rs.getString("soDienThoai"),
                        rs.getString("email")
                );

                KhoHang kho = new KhoHang(
                        rs.getString("maKho"),
                        rs.getString("tenKho"),
                        rs.getString("viTri")
                );

                Thuoc thuoc = new Thuoc(
                        rs.getString("maThuoc"),
                        rs.getString("tenThuoc"),
                        rs.getDouble("giaBan"),
                        rs.getDouble("giaNhap"),
                        rs.getInt("soLuong"),
                        rs.getDate("hanSuDung").toLocalDate(),
                        TrangThaiTonKho.valueOf(rs.getString("trangThaiTonKho")),
                        DonViTinh.valueOf(rs.getString("donViTinh")),
                        ncc,
                        kho
                );
                list.add(thuoc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean themThuoc(Thuoc thuoc) {
        String sql = "INSERT INTO Thuoc (maThuoc, tenThuoc, giaBan, giaNhap, soLuong, hanSuDung, trangThaiTonKho, donViTinh, maNCC, maKho) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, thuoc.getMaThuoc());
            pstmt.setString(2, thuoc.getTenThuoc());
            pstmt.setDouble(3, thuoc.getGiaBan());
            pstmt.setDouble(4, thuoc.getGiaNhap());
            pstmt.setInt(5, thuoc.getSoLuong());
            pstmt.setDate(6, Date.valueOf(thuoc.getHanSuDung()));
            pstmt.setString(7, thuoc.getTrangThaiTonKho().name());
            pstmt.setString(8, thuoc.getDonViTinh().name());
            pstmt.setString(9, thuoc.getNhaCungCap().getMaNCC());
            pstmt.setString(10, thuoc.getKhoHang().getMaKho());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}