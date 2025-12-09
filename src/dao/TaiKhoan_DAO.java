package dao;

import connectDB.ConnectDB;
import entity.TaiKhoan;
import enums.PhanQuyen;
import enums.TrangThaiTaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaiKhoan_DAO {

    public TaiKhoan selectByUsername(String username) {
        TaiKhoan tk = null;

        try {
            // BẮT BUỘC GỌI CONNECT() TRƯỚC
            if (ConnectDB.getConnection() == null || ConnectDB.getConnection().isClosed()) {
                ConnectDB.getInstance().connect();
            }

            String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tk = new TaiKhoan(
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"),
                        PhanQuyen.valueOf(rs.getString("quyenTruyCap")),
                        TrangThaiTaiKhoan.valueOf(rs.getString("trangThai"))
                );
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tk;
    }
}
