package entity;

import enums.TrangThaiTonKho;
import java.time.LocalDate;
import java.util.Objects;

public class KhoHang {
    private String maKho;
    private LocalDate ngayNhap;
    private String email;
    private int soLuong;
    private TrangThaiTonKho trangThai;

    public KhoHang() {}

    public KhoHang(String maKho, LocalDate ngayNhap, String email, int soLuong, TrangThaiTonKho trangThai) {
        this.maKho = maKho;
        this.ngayNhap = ngayNhap;
        this.email = email;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
    }

    public String getMaKho() {
        return maKho;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDate ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public TrangThaiTonKho getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiTonKho trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KhoHang)) return false;
        KhoHang khoHang = (KhoHang) o;
        return Objects.equals(maKho, khoHang.maKho);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKho);
    }

    @Override
    public String toString() {
        return "KhoHang{" +
                "maKho='" + maKho + '\'' +
                ", ngayNhap=" + ngayNhap +
                ", email='" + email + '\'' +
                ", soLuong=" + soLuong +
                ", trangThai=" + trangThai +
                '}';
    }
}
