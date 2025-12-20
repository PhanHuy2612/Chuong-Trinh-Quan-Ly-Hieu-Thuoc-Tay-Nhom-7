package entity;

import java.time.LocalDate;
import java.util.Objects;

public class DiemTichLuy {

    private String maDiem;
    private String maKH;
    private LocalDate ngayTich;
    private LocalDate ngayHetHan;
    private boolean daSuDung;

    public DiemTichLuy() {}

    public DiemTichLuy(String maDiem, String maKH,
                       LocalDate ngayTich, LocalDate ngayHetHan,
                       boolean daSuDung) {
        this.maDiem = maDiem;
        this.maKH = maKH;
        this.ngayTich = ngayTich;
        this.ngayHetHan = ngayHetHan;
        this.daSuDung = daSuDung;
    }


    public String getMaDiem() {
        return maDiem;
    }

    public void setMaDiem(String maDiem) {
        this.maDiem = maDiem;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public LocalDate getNgayTich() {
        return ngayTich;
    }

    public void setNgayTich(LocalDate ngayTich) {
        this.ngayTich = ngayTich;
    }

    public LocalDate getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(LocalDate ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public boolean isDaSuDung() {
        return daSuDung;
    }

    public void setDaSuDung(boolean daSuDung) {
        this.daSuDung = daSuDung;
    }


    public boolean conHan() {
        return ngayHetHan != null && !ngayHetHan.isBefore(LocalDate.now());
    }

    public boolean coTheSuDung() {
        return !daSuDung && conHan();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiemTichLuy)) return false;
        DiemTichLuy that = (DiemTichLuy) o;
        return Objects.equals(maDiem, that.maDiem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDiem);
    }

    @Override
    public String toString() {
        return "DiemTichLuy{" +
                "maDiem='" + maDiem + '\'' +
                ", maKH='" + maKH + '\'' +
                ", ngayTich=" + ngayTich +
                ", ngayHetHan=" + ngayHetHan +
                ", daSuDung=" + daSuDung +
                '}';
    }
}
