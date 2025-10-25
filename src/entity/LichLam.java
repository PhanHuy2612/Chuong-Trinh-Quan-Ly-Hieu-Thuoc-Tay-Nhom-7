package entity;

import java.time.LocalDate;
import java.util.Objects;

public class LichLam {
    private String maLichLam;
    private LocalDate ngayLam;
    private CaLam caLam;
    private NhanVien  nhanVien;

    public LichLam() {
    }

    public LichLam(String maLichLam, LocalDate ngayLam, CaLam caLam, NhanVien nhanVien) {
        this.maLichLam = maLichLam;
        this.ngayLam = ngayLam;
        this.caLam = caLam;
        this.nhanVien = nhanVien;
    }

    public String getMaLichLam() {
        return maLichLam;
    }

    public void setMaLichLam(String maLichLam) {
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

    @Override
    public String toString() {
        return "LichLam{" +
                "maLichLam='" + maLichLam + '\'' +
                ", ngayLam='" + ngayLam + '\'' +
                ", caLam=" + caLam +
                ", nhanVien=" + nhanVien +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LichLam lichLam = (LichLam) o;
        return Objects.equals(maLichLam, lichLam.maLichLam);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maLichLam);
    }
}
