package entity;

import enums.PhuongThucThanhToan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoaDon {

    private String maHD;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private LocalDate ngayLap;
    private PhuongThucThanhToan phuongThucThanhToan;

    /** Thuế VAT (ví dụ: 0.08 = 8%) */
    private BigDecimal thueVAT;

    /** Tổng tiền phải thanh toán */
    private BigDecimal tongTien;

    /** Tổng tiền giảm của HÓA ĐƠN */
    private BigDecimal tienGiam;

    private List<ChiTietHoaDon> danhSachCTHD;

    /* ================= CONSTRUCTOR ================= */

    public HoaDon() {
        this.danhSachCTHD = new ArrayList<>();
        this.thueVAT = BigDecimal.ZERO;
        this.tongTien = BigDecimal.ZERO;
        this.tienGiam = BigDecimal.ZERO;
    }

    public HoaDon(String maHD,
                  KhachHang khachHang,
                  LocalDate ngayLap,
                  PhuongThucThanhToan phuongThucThanhToan,
                  BigDecimal thueVAT,
                  NhanVien nhanVien) {
        this();
        this.maHD = maHD;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.thueVAT = thueVAT != null ? thueVAT : BigDecimal.ZERO;
        this.nhanVien = nhanVien;
    }

    /* ================= GET / SET ================= */

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
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

    public BigDecimal getThueVAT() {
        return thueVAT;
    }

    public void setThueVAT(BigDecimal thueVAT) {
        this.thueVAT = thueVAT != null ? thueVAT : BigDecimal.ZERO;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien != null ? tongTien : BigDecimal.ZERO;
    }

    public BigDecimal getTienGiam() {
        return tienGiam;
    }

    public void setTienGiam(BigDecimal tienGiam) {
        this.tienGiam = tienGiam != null ? tienGiam : BigDecimal.ZERO;
    }

    public List<ChiTietHoaDon> getDanhSachCTHD() {
        return danhSachCTHD;
    }

    public void setDanhSachCTHD(List<ChiTietHoaDon> danhSachCTHD) {
        this.danhSachCTHD = danhSachCTHD != null
                ? danhSachCTHD
                : new ArrayList<>();
    }

    public boolean themChiTiet(ChiTietHoaDon cthd) {
        return this.danhSachCTHD.add(cthd);
    }

    /* ================= TÍNH TOÁN ================= */

    /** Tổng tiền hàng (chưa giảm, chưa VAT) */
    public BigDecimal tinhTienHang() {
        return danhSachCTHD.stream()
                .map(ct -> ct.getDonGia()
                        .multiply(BigDecimal.valueOf(ct.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Tiền giảm của hóa đơn
     * ❗ KHÔNG tự tính từ ChiTietHoaDon
     */
    public BigDecimal tinhTienGiam() {
        return tienGiam != null ? tienGiam : BigDecimal.ZERO;
    }

    /** VAT = (tiền hàng - tiền giảm) × thuế */
    public BigDecimal tinhTienVAT() {
        BigDecimal tienSauGiam = tinhTienHang().subtract(tinhTienGiam());
        return tienSauGiam.multiply(thueVAT);
    }

    /** Tổng thanh toán cuối cùng */
    public BigDecimal tinhTongThanhToan() {
        BigDecimal tong = tinhTienHang()
                .subtract(tinhTienGiam())
                .add(tinhTienVAT());

        this.tongTien = tong.max(BigDecimal.ZERO);
        return this.tongTien;
    }

    /* ================= OVERRIDE ================= */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoaDon)) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHD, hoaDon.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHD);
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", khachHang=" + (khachHang != null ? khachHang.getMaKH() : "N/A") +
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNV() : "N/A") +
                ", ngayLap=" + ngayLap +
                ", phuongThucThanhToan=" + phuongThucThanhToan +
                ", tienHang=" + tinhTienHang() +
                ", tienGiam=" + tienGiam +
                ", thueVAT=" + thueVAT +
                ", tongTien=" + tongTien +
                ", soLuongCTHD=" + danhSachCTHD.size() +
                '}';
    }
}
