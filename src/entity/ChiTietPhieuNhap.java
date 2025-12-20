package entity;

public class ChiTietPhieuNhap {
    private String maPhieu;
    private String maThuoc;
    private int soLuong;
    private double donGia;

    public ChiTietPhieuNhap() {}

    public ChiTietPhieuNhap(String maPhieu, String maThuoc, int soLuong, double donGia) {
        this.maPhieu = maPhieu;
        this.maThuoc = maThuoc;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getters & Setters
    public String getMaPhieu() { return maPhieu; }
    public void setMaPhieu(String maPhieu) { this.maPhieu = maPhieu; }

    public String getMaThuoc() { return maThuoc; }
    public void setMaThuoc(String maThuoc) { this.maThuoc = maThuoc; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "maPhieu='" + maPhieu + '\'' +
                ", maThuoc='" + maThuoc + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                '}';
    }
}