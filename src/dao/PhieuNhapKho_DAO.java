package dao;

import connectDB.ConnectDB;
import entity.PhieuNhapKho;
import entity.ChiTietPhieuNhap;
import entity.Thuoc;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapKho_DAO {

    private final Thuoc_DAO thuocDAO = new Thuoc_DAO();
    private final NhaCungCap_DAO nccDAO = new NhaCungCap_DAO();
    private final NhanVien_DAO nhanVienDAO = NhanVien_DAO.getInstance();

    // Lấy toàn bộ lịch sử nhập kho + chi tiết
    public List<Object[]> getLichSuNhapKho() {
        List<Object[]> ds = new ArrayList<>();
        String sql = """
                SELECT 
                    t.tenThuoc,
                    'Nhập kho' as loai,
                    ct.soLuong,
                    p.lyDo,
                    p.ngayNhap,
                    nv.tenNhanVien
                FROM PhieuNhapKho p
                JOIN ChiTietPhieuNhap ct ON p.maPhieu = ct.maPhieu
                JOIN Thuoc t ON ct.maThuoc = t.maThuoc
                JOIN NhanVien nv ON p.maNhanVien = nv.maNhanVien
                ORDER BY p.ngayNhap DESC
                """;

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getString("tenThuoc"),
                        rs.getString("loai"),
                        rs.getInt("soLuong"),
                        rs.getString("lyDo"),
                        rs.getTimestamp("ngayNhap").toLocalDateTime(),
                        rs.getString("tenNhanVien")
                };
                ds.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}