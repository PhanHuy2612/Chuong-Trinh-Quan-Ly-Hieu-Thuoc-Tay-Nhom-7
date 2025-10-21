package entity;

import enums.PhuongThucThanhToan;
import java.time.LocalDate;
import java.util.Objects;

public class HoaDon {
    private String maHD;
    private String maNV;
    private String maKH;
    private String maKM;
    private LocalDate ngayLap;
    private PhuongThucThanhToan phuongThucThanhToan;

    public HoaDon(String maHD, String maNV, String maKH, String maKM, LocalDate ngayLap, PhuongThucThanhToan phuongThucThanhToan) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
        this.ngayLap = ngayLap;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public PhuongThucThanhToan getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public double tinhTongTien(double tienHang, double tienGiam) {
        return tienHang - tienGiam;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", maNV='" + maNV + '\'' +
                ", maKH='" + maKH + '\'' +
                ", maKM='" + maKM + '\'' +
                ", ngayLap=" + ngayLap +
                ", phuongThucThanhToan=" + phuongThucThanhToan +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoaDon hoaDon)) return false;
        return Objects.equals(getMaHD(), hoaDon.getMaHD()) && Objects.equals(getMaNV(), hoaDon.getMaNV()) && Objects.equals(getMaKH(), hoaDon.getMaKH()) && Objects.equals(getMaKM(), hoaDon.getMaKM()) && Objects.equals(getNgayLap(), hoaDon.getNgayLap()) && getPhuongThucThanhToan() == hoaDon.getPhuongThucThanhToan();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaHD(), getMaNV(), getMaKH(), getMaKM(), getNgayLap(), getPhuongThucThanhToan());
    }

    public static void main(String[] args) {
        HoaDon hoaDon = new HoaDon("HD001", "NV001", "KH001", "KM001", LocalDate.now(), PhuongThucThanhToan.TIEN_MAT);
        System.out.println(hoaDon);
    }
}
