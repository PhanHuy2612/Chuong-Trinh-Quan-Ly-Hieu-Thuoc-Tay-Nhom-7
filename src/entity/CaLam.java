package entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CaLam {
    private String maCaLam;
    private String tenCaLam;
    private LocalDate ngayLamViec;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private String viTriLamViec;
    private int soLuongNhanVienCan;
    private String ghiChu;
    private List<NhanVien> nhanVienPhanCong;

    public CaLam() {
        nhanVienPhanCong = new ArrayList<>();
    }

    public CaLam(String maCaLam, String tenCaLam, LocalDate ngayLamViec,
                 LocalTime gioBatDau, LocalTime gioKetThuc,
                 String viTriLamViec, int soLuongNhanVienCan, String ghiChu) {
        this.maCaLam = maCaLam;
        this.tenCaLam = tenCaLam;
        this.ngayLamViec = ngayLamViec;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.viTriLamViec = viTriLamViec;
        this.soLuongNhanVienCan = soLuongNhanVienCan;
        this.ghiChu = ghiChu;
        this.nhanVienPhanCong = new ArrayList<>();
    }

    // Getters & Setters
    public String getMaCaLam() {
        return maCaLam;
    }

    public void setMaCaLam(String maCaLam) {
        this.maCaLam = maCaLam;
    }

    public String getTenCaLam() {
        return tenCaLam;
    }

    public void setTenCaLam(String tenCaLam) {
        this.tenCaLam = tenCaLam;
    }

    public LocalDate getNgayLamViec() {
        return ngayLamViec;
    }

    public void setNgayLamViec(LocalDate ngayLamViec) {
        this.ngayLamViec = ngayLamViec;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public String getViTriLamViec() {
        return viTriLamViec;
    }

    public void setViTriLamViec(String viTriLamViec) {
        this.viTriLamViec = viTriLamViec;
    }

    public int getSoLuongNhanVienCan() {
        return soLuongNhanVienCan;
    }

    public void setSoLuongNhanVienCan(int soLuongNhanVienCan) {
        this.soLuongNhanVienCan = soLuongNhanVienCan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public List<NhanVien> getNhanVienPhanCong() {
        return nhanVienPhanCong;
    }

    public void setNhanVienPhanCong(List<NhanVien> nhanVienPhanCong) {
        this.nhanVienPhanCong = nhanVienPhanCong;
    }

    public void themNhanVien(NhanVien nv) {
        if (!nhanVienPhanCong.contains(nv)) {
            nhanVienPhanCong.add(nv);
        }
    }

    public void xoaNhanVien(NhanVien nv) {
        nhanVienPhanCong.remove(nv);
    }

    @Override
    public String toString() {
        return tenCaLam + " (" + gioBatDau + " - " + gioKetThuc + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaLam)) return false;
        CaLam caLam = (CaLam) o;
        return Objects.equals(maCaLam, caLam.maCaLam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maCaLam);
    }
}
