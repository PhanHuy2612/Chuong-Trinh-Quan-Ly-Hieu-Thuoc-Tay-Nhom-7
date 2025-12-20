package entity;

import java.time.LocalDateTime;

public class PhieuNhapKho {
    private String maPhieu;
    private LocalDateTime ngayNhap;
    private String maNhanVien;
    private String maNhaCungCap;
    private String lyDo;

    public PhieuNhapKho() {}

    public PhieuNhapKho(String maPhieu, LocalDateTime ngayNhap, String maNhanVien, String maNhaCungCap, String lyDo) {
        this.maPhieu = maPhieu;
        this.ngayNhap = ngayNhap;
        this.maNhanVien = maNhanVien;
        this.maNhaCungCap = maNhaCungCap;
        this.lyDo = lyDo;
    }

    // Getters & Setters
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }

    public LocalDateTime getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(LocalDateTime ngayNhap) { this.ngayNhap = ngayNhap; }

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getMaNhaCungCap() { return maNhaCungCap; }
    public void setMaNhaCungCap(String maNhaCungCap) { this.maNhaCungCap = maNhaCungCap; }

    public String getLyDo() { return lyDo; }
    public void setLyDo(String lyDo) { this.lyDo = lyDo; }

    @Override
    public String toString() {
        return "PhieuNhapKho{" +
                "maPhieu='" + maPhieu + '\'' +
                ", ngayNhap=" + ngayNhap +
                ", lyDo='" + lyDo + '\'' +
                '}';
    }
}