package entity;

import enums.LoaiKhachHang;

public class ChiTietHoaDon {
    private String maHD;
    private String maSP;
    private LoaiKhachHang loaiKhachHang;
    private double tienGiam;
    private String soLuong;
    private double donGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(String maHD, String maSP, LoaiKhachHang loaiKhachHang, double tienGiam, String soLuong, double donGia) {
        this.maHD = maHD;
        this.maSP = maSP;
        this.loaiKhachHang = loaiKhachHang;
        this.tienGiam = tienGiam;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
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

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }


}
