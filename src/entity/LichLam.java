package entity;

import enums.TrangThaiCaLam;

import java.time.LocalDate;
import java.util.Objects;

public class LichLam {

    private int maLichLam;
    private LocalDate ngayLam;
    private CaLam caLam;
    private NhanVien nhanVien;
    private TrangThaiCaLam trangThaiCaLam;
    private String ghiChu;

    public LichLam() {}

    public LichLam(LocalDate ngayLam, CaLam caLam, NhanVien nhanVien,
                   TrangThaiCaLam trangThaiCaLam, String ghiChu) {
        this.ngayLam = ngayLam;
        this.caLam = caLam;
        this.nhanVien = nhanVien;
        this.trangThaiCaLam = trangThaiCaLam;
        this.ghiChu = ghiChu;
    }

    public Integer getMaLichLam() {
        return maLichLam;
    }

    public void setMaLichLam(Integer maLichLam) {
        this.maLichLam = maLichLam;
    }

    public LocalDate getNgayLam() {
        return ngayLam;
    }

    public void setNgayLam(LocalDate ngayLam) {
        this.ngayLam = ngayLam;
    }

    public CaLam getCaLam() {
        return caLam;
    }

    public void setCaLam(CaLam caLam) {
        this.caLam = caLam;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public TrangThaiCaLam getTrangThaiCaLam() {
        return trangThaiCaLam;
    }

    public void setTrangThaiCaLam(TrangThaiCaLam trangThaiCaLam) {
        this.trangThaiCaLam = trangThaiCaLam;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return "LichLam{" +
                "maLichLam=" + maLichLam +
                ", ngayLam=" + ngayLam +
                ", caLam=" + caLam +
                ", nhanVien=" + nhanVien +
                ", trangThaiCaLam=" + trangThaiCaLam +
                ", ghiChu='" + ghiChu + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LichLam)) return false;
        LichLam lichLam = (LichLam) o;
        return Objects.equals(maLichLam, lichLam.maLichLam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maLichLam);
    }
}
