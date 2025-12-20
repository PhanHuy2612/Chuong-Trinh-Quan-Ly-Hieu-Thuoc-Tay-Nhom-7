package entity;

import java.math.BigDecimal;
import java.util.Objects;

public class ChiTietHoaDon {

    private HoaDon hoaDon;
    private Thuoc thuoc;

    private int soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;

    public ChiTietHoaDon() {
        this.soLuong = 0;
        this.donGia = BigDecimal.ZERO;
        this.thanhTien = BigDecimal.ZERO;
    }

    public ChiTietHoaDon(HoaDon hoaDon, Thuoc thuoc, int soLuong, BigDecimal donGia) {
        this();
        this.hoaDon = hoaDon;
        this.thuoc = thuoc;
        this.soLuong = Math.max(0, soLuong);
        this.donGia = donGia != null ? donGia : BigDecimal.ZERO;
        tinhThanhTien();
    }

    /* ================= GETTER / SETTER ================= */

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public Thuoc getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = Math.max(0, soLuong);
        tinhThanhTien();
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia != null ? donGia : BigDecimal.ZERO;
        tinhThanhTien();
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien != null ? thanhTien : BigDecimal.ZERO;
    }

    /* ================= LOGIC ================= */

    /**
     * Thành tiền = đơn giá × số lượng
     */
    public BigDecimal tinhThanhTien() {
        this.thanhTien = donGia
                .multiply(BigDecimal.valueOf(soLuong))
                .max(BigDecimal.ZERO);
        return this.thanhTien;
    }

    /* ================= OVERRIDE ================= */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChiTietHoaDon)) return false;
        ChiTietHoaDon that = (ChiTietHoaDon) o;
        return Objects.equals(hoaDon, that.hoaDon) &&
                Objects.equals(thuoc, that.thuoc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hoaDon, thuoc);
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "maHD='" + (hoaDon != null ? hoaDon.getMaHD() : "N/A") + '\'' +
                ", maThuoc='" + (thuoc != null ? thuoc.getMaThuoc() : "N/A") + '\'' +
                ", tenThuoc='" + (thuoc != null ? thuoc.getTenThuoc() : "N/A") + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                '}';
    }
}
