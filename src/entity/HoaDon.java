package entity;

import enums.PhuongThucThanhToan;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoaDon {
    private String maHD;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private KhuyenMai khuyenMai;
    private LocalDate ngayLap;
    private PhuongThucThanhToan phuongThucThanhToan;
    private List<ChiTietHoaDon> danhSachCTHD;

    public HoaDon() {
        this.danhSachCTHD = new ArrayList<>();
    }

    public HoaDon(String maHD, NhanVien nhanVien, KhachHang khachHang, KhuyenMai khuyenMai, LocalDate ngayLap, PhuongThucThanhToan phuongThucThanhToan) {
        this.maHD = maHD;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.khuyenMai = khuyenMai;
        this.ngayLap = ngayLap;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this();
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
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

    public List<ChiTietHoaDon> getDanhSachCTHD() {
        return danhSachCTHD;
    }

    public void setDanhSachCTHD(List<ChiTietHoaDon> danhSachCTHD) {
        this.danhSachCTHD = danhSachCTHD;
    }

    public boolean themChiTiet(ChiTietHoaDon cthd) {
        // Thêm các kiểm tra nghiệp vụ nếu cần
        return this.danhSachCTHD.add(cthd);
    }

    public double tinhTongTien() {
        double tongTienHang = 0;
        for (ChiTietHoaDon cthd : danhSachCTHD) {
            tongTienHang += cthd.tinhThanhTien();
        }
        return tongTienHang;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                // Chỉ in ra mã của các đối tượng
                ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNV() : "N/A") +
                ", khachHang=" + (khachHang != null ? khachHang.getMaKH() : "N/A") +
                ", khuyenMai=" + (khuyenMai != null ? khuyenMai.getMaKM() : "N/A") +
                ", ngayLap=" + ngayLap +
                ", phuongThucThanhToan=" + phuongThucThanhToan +
                ", soLuongCTHD=" + danhSachCTHD.size() + // In số lượng CTHD
                ", tongTien=" + tinhTongTien() + // In tổng tiền
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHD, hoaDon.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maHD);
    }
}
