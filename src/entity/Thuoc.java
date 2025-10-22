package entity;

import enums.DonViTinh;
import enums.TrangThaiTonKho;

import java.time.LocalDate;
import java.util.Objects;

public class Thuoc {
    private String maThuoc;
    private String tenThuoc;
    private double giaBan;
    private double giaNhap;
    private int soLuong;
    private LocalDate hanSuDung;
    private TrangThaiTonKho trangThaiTonKho;        // CON_HANG, HET_HANG, SAP_HET_HANG
    private DonViTinh donViTinh;
    private NhaCungCap nhaCungCap;
    private KhoHang khoHang;       // VIEN, CHAI, HOP, GOI

    public Thuoc() {
    }

    public Thuoc(String maThuoc, String tenThuoc, double giaBan, double giaNhap, int soLuong, LocalDate hanSuDung, TrangThaiTonKho trangThaiTonKho, DonViTinh donViTinh, NhaCungCap nhaCungCap, KhoHang khoHang) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.giaBan = giaBan;
        this.giaNhap = giaNhap;
        this.soLuong = soLuong;
        this.hanSuDung = hanSuDung;
        this.trangThaiTonKho = trangThaiTonKho;
        this.donViTinh = donViTinh;
        this.nhaCungCap = nhaCungCap;
        this.khoHang = khoHang;
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

    public TrangThaiTonKho getTrangThaiTonKho() {
        return trangThaiTonKho;
    }

    public void setTrangThaiTonKho(TrangThaiTonKho trangThaiTonKho) {
        this.trangThaiTonKho = trangThaiTonKho;
    }

    public DonViTinh getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(DonViTinh donViTinh) {
        this.donViTinh = donViTinh;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public KhoHang getKhoHang() {
        return khoHang;
    }

    public void setKhoHang(KhoHang khoHang) {
        this.khoHang = khoHang;
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
                ", nhaCungCap=" + nhaCungCap +
                ", khoHang=" + khoHang +
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
