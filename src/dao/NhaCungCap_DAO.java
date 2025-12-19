package dao;

import connectDB.ConnectDB;
import entity.NhaCungCap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho entity NhaCungCap
 * Bảng: NhaCungCap(maNhaCungCap, diaChi, email, soDienThoai, tenNhaCungCap)
 */
public class NhaCungCap_DAO {

    /* ==================== 1. LẤY TẤT CẢ ==================== */
    public List<NhaCungCap> getAllNhaCungCap() {
        List<NhaCungCap> ds = new ArrayList<>();
        String sql = "SELECT maNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email FROM NhaCungCap";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhaCungCap ncc = new NhaCungCap(
                        rs.getString("maNhaCungCap"),
                        rs.getString("tenNhaCungCap"),
                        rs.getString("diaChi"),
                        rs.getString("soDienThoai"),
                        rs.getString("email")
                );
                ds.add(ncc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /* ==================== 2. THÊM ==================== */
    public boolean themNhaCungCap(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap (maNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ncc.getMaNCC());          // maNhaCungCap
            ps.setString(2, ncc.getTenNCC());         // tenNhaCungCap
            ps.setString(3, ncc.getDiaChi());         // diaChi
            ps.setString(4, ncc.getSoDienThoai());    // soDienThoai
            ps.setString(5, ncc.getEmail());          // email

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ==================== 3. CẬP NHẬT ==================== */
    public boolean capNhatNhaCungCap(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET tenNhaCungCap = ?, diaChi = ?, "
                + "soDienThoai = ?, email = ? WHERE maNhaCungCap = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getDiaChi());
            ps.setString(3, ncc.getSoDienThoai());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getMaNCC());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ==================== 4. XÓA ==================== */
    public boolean xoaNhaCungCap(String maNCC) {
        String sql = "DELETE FROM NhaCungCap WHERE maNhaCungCap = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNCC);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ==================== 5. TÌM THEO MÃ ==================== */
    public NhaCungCap getNhaCungCapByMa(String maNCC) {
        String sql = "SELECT * FROM NhaCungCap WHERE maNhaCungCap = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNCC);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhaCungCap(
                            rs.getString("maNhaCungCap"),
                            rs.getString("tenNhaCungCap"),
                            rs.getString("diaChi"),
                            rs.getString("soDienThoai"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ==================== 6. KIỂM TRA TỒN TẠI ==================== */
    public boolean tonTaiMaNCC(String maNCC) {
        String sql = "SELECT 1 FROM NhaCungCap WHERE maNhaCungCap = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNCC);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ==================== 7. DANH SÁCH MÃ (ComboBox) ==================== */
    public List<String> getDanhSachMaNCC() {
        List<String> ds = new ArrayList<>();
        String sql = "SELECT maNhaCungCap FROM NhaCungCap";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(rs.getString("maNhaCungCap"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /* ==================== 8. LẤY TÊN THEO MÃ ==================== */
    public String getTenNCCByMa(String maNCC) {
        String sql = "SELECT tenNhaCungCap FROM NhaCungCap WHERE maNhaCungCap = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maNCC);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tenNhaCungCap");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}