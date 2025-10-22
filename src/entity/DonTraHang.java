package entity;

import java.time.LocalDate;
import java.util.Objects;

public class DonTraHang {
    private String maDonTra;
    private LocalDate ngayTra;
    private String lyDoTra;
    private HoaDon hoaDon;
    private boolean trangThai;  // true: Đã xử lý, false: Chưa xử lý

    public DonTraHang() {
    }

    public DonTraHang(String maDonTra, LocalDate ngayTra, String lyDoTra, HoaDon hoaDon, boolean trangThai) {
        this.maDonTra = maDonTra;
        this.ngayTra = ngayTra;
        this.lyDoTra = lyDoTra;
        this.hoaDon = hoaDon;
        this.trangThai = trangThai;
    }

    public String getMaDonTra() {
        return maDonTra;
    }

    public void setMaDonTra(String maDonTra) {
        this.maDonTra = maDonTra;
    }

    public LocalDate getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(LocalDate ngayTra) {
        this.ngayTra = ngayTra;
    }

    public String getLyDoTra() {
        return lyDoTra;
    }

    public void setLyDoTra(String lyDoTra) {
        this.lyDoTra = lyDoTra;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "DonTraHang{" +
                "maDonTra='" + maDonTra + '\'' +
                ", ngayTra=" + ngayTra +
                ", lyDoTra='" + lyDoTra + '\'' +
                ", hoaDon=" + hoaDon +
                ", trangThai=" + trangThai +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DonTraHang that = (DonTraHang) o;
        return Objects.equals(maDonTra, that.maDonTra);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maDonTra);
    }
}
