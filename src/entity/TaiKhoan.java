// src/entity/TaiKhoan.java
package entity;

import enums.PhanQuyen;
import enums.TrangThaiTaiKhoan;

import java.util.Objects;

public class TaiKhoan {
    private String tenDangNhap;
    private String matKhau;
    private PhanQuyen phanQuyen;
    private TrangThaiTaiKhoan trangThai;
    private String maNhanVien;
    private String email;

    public TaiKhoan() {}

    public TaiKhoan(String tenDangNhap, String matKhau, PhanQuyen phanQuyen, TrangThaiTaiKhoan trangThai, String maNhanVien, String email) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.phanQuyen = phanQuyen;
        this.trangThai = trangThai;
        this.maNhanVien = maNhanVien;
        this.email = email;
    }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public PhanQuyen getPhanQuyen() { return phanQuyen; }
    public void setPhanQuyen(PhanQuyen phanQuyen) { this.phanQuyen = phanQuyen; }
    public TrangThaiTaiKhoan getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiTaiKhoan trangThai) { this.trangThai = trangThai; }
    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaiKhoan)) return false;
        TaiKhoan taiKhoan = (TaiKhoan) o;
        return Objects.equals(tenDangNhap, taiKhoan.tenDangNhap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenDangNhap);
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "tenDangNhap='" + tenDangNhap + '\'' +
                ", phanQuyen=" + phanQuyen +
                ", trangThai=" + trangThai +
                ", maNhanVien='" + maNhanVien + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}