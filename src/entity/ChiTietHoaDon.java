package entity;

import enums.LoaiKhachHang;

public class ChiTietHoaDon {
    private Thuoc thuoc;
    private LoaiKhachHang loaiKhachHang;
    private double tienGiam;
    private int soLuong;
    private double donGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(Thuoc thuoc, LoaiKhachHang loaiKhachHang, double tienGiam, int soLuong, double donGia) {
        this.thuoc = thuoc;
        this.loaiKhachHang = loaiKhachHang;
        this.tienGiam = tienGiam;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public Thuoc getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public LoaiKhachHang getLoaiKhachHang() {
        return loaiKhachHang;
    }

    public void setLoaiKhachHang(LoaiKhachHang loaiKhachHang) {
        this.loaiKhachHang = loaiKhachHang;
    }

    public double getTienGiam() {
        return tienGiam;
    }

    public void setTienGiam(double tienGiam) {
        this.tienGiam = tienGiam;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double tinhThanhTien() {
        return donGia * soLuong - tienGiam;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "maSP='" + (thuoc != null ? thuoc.getMaThuoc() : "N/A") + '\'' +
                ", loaiKhachHang=" + loaiKhachHang +
                ", tienGiam=" + tienGiam +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                '}';
    }
}
