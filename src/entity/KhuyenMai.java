package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class KhuyenMai {

    private String maKM;
    private String tenKM;
    private double phanTramGiam;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private String maNhanVien;
    private LocalDateTime ngayTao;
    private BigDecimal tienGiamToiDa;
    private String loaiThuocApDung;

    public KhuyenMai() {
    }

    public KhuyenMai(String maKM, String tenKM, double phanTramGiam,
                     LocalDate ngayBatDau, LocalDate ngayKetThuc,
                     String maNhanVien, LocalDateTime ngayTao,
                     BigDecimal tienGiamToiDa, String loaiThuocApDung) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.phanTramGiam = phanTramGiam;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.maNhanVien = maNhanVien;
        this.ngayTao = ngayTao;
        this.tienGiamToiDa = tienGiamToiDa;
        this.loaiThuocApDung = loaiThuocApDung;
    }

    // ================== GET / SET ==================

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getTenKM() {
        return tenKM;
    }

    public void setTenKM(String tenKM) {
        this.tenKM = tenKM;
    }

    public double getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(double phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public BigDecimal getTienGiamToiDa() {
        return tienGiamToiDa;
    }

    public void setTienGiamToiDa(BigDecimal tienGiamToiDa) {
        this.tienGiamToiDa = tienGiamToiDa;
    }

    public String getLoaiThuocApDung() {
        return loaiThuocApDung;
    }

    public void setLoaiThuocApDung(String loaiThuocApDung) {
        this.loaiThuocApDung = loaiThuocApDung;
    }

    // ================== BUSINESS METHOD ==================

    /**
     * Kiểm tra khuyến mãi có đang diễn ra không (dựa trên ngày hiện tại)
     */
    public boolean dangHoatDong() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(ngayBatDau) && !today.isAfter(ngayKetThuc);
    }

    /**
     * Kiểm tra khuyến mãi có áp dụng cho phân loại thuốc này không
     * @param phanLoaiThuoc ví dụ: "OTC", "RX", "TPCN", "VITAMIN"
     * @return true nếu áp dụng
     */
    public boolean apDungChoPhanLoai(String phanLoaiThuoc) {
        if (loaiThuocApDung == null || loaiThuocApDung.trim().isEmpty()) {
            return true; // NULL = áp dụng toàn bộ
        }
        String[] dsLoai = loaiThuocApDung.split(",");
        for (String loai : dsLoai) {
            if (loai.trim().equalsIgnoreCase(phanLoaiThuoc)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "KhuyenMai{" +
                "maKM='" + maKM + '\'' +
                ", tenKM='" + tenKM + '\'' +
                ", phanTramGiam=" + phanTramGiam +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayKetThuc=" + ngayKetThuc +
                ", maNhanVien='" + maNhanVien + '\'' +
                ", ngayTao=" + ngayTao +
                ", tienGiamToiDa=" + tienGiamToiDa +
                ", loaiThuocApDung='" + loaiThuocApDung + '\'' +
                ", dangHoatDong=" + dangHoatDong() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhuyenMai khuyenMai = (KhuyenMai) o;
        return Objects.equals(maKM, khuyenMai.maKM);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKM);
    }
}