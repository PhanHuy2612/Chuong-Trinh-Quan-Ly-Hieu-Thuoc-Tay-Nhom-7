package entity;

import enums.LoaiKhachHang;

public class ChiTietHoaDon {
    private String maHD;
    private String maSP;
    private LoaiKhachHang loaiKhachHang;
    private double tienGiam;

    public ChiTietHoaDon(String maHD, String maSP, LoaiKhachHang loaiKhachHang, double tienGiam) {
        this.maHD = maHD;
        this.maSP = maSP;
        this.loaiKhachHang = loaiKhachHang;
        this.tienGiam = tienGiam;
    }
    public  ChiTietHoaDon(){
        this("", "", LoaiKhachHang.VANG_LAI, 0);
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

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "maHD='" + maHD + '\'' +
                ", maSP='" + maSP + '\'' +
                ", loaiKhachHang=" + loaiKhachHang +
                ", tienGiam=" + tienGiam +
                '}';
    }
}
