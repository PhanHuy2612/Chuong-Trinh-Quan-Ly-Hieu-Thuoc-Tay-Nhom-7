package entity;

import enums.DonViTinh;

import java.time.LocalDate;
import java.util.Objects;

public class Thuoc {
    private String maThuoc;
    private String tenThuoc;
    private double giaBan;
    private double giaNhap;
    private int soLuong;
    private LocalDate hanSuDung;
    private boolean trangThaiTonKho;        // TONKHO, HETHANG, BINHTHUONG
    private DonViTinh donViTinh;            // VIEN, CHAI, HOP, GOI

    public Thuoc() {
    }

    public Thuoc(String maThuoc, String tenThuoc, double giaBan, double giaNhap, int soLuong, LocalDate hanSuDung, boolean trangThaiTonKho, DonViTinh donViTinh) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.giaBan = giaBan;
        this.giaNhap = giaNhap;
        this.soLuong = soLuong;
        this.hanSuDung = hanSuDung;
        this.trangThaiTonKho = trangThaiTonKho;
        this.donViTinh = donViTinh;
    }

    public String getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }

    public boolean isTrangThaiTonKho() {
        return trangThaiTonKho;
    }

    public void setTrangThaiTonKho(boolean trangThaiTonKho) {
        this.trangThaiTonKho = trangThaiTonKho;
    }

    public DonViTinh getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(DonViTinh donViTinh) {
        this.donViTinh = donViTinh;
    }

    @Override
    public String toString() {
        return "Thuoc{" +
                "maThuoc='" + maThuoc + '\'' +
                ", tenThuoc='" + tenThuoc + '\'' +
                ", giaBan=" + giaBan +
                ", giaNhap=" + giaNhap +
                ", soLuong=" + soLuong +
                ", hanSuDung=" + hanSuDung +
                ", trangThaiTonKho=" + trangThaiTonKho +
                ", donViTinh=" + donViTinh +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Thuoc thuoc = (Thuoc) o;
        return Objects.equals(maThuoc, thuoc.maThuoc);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maThuoc);
    }
}
