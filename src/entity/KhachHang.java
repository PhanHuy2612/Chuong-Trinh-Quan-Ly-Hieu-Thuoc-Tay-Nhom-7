package entity;

import enums.LoaiKhachHang;

public class KhachHang {

    private String maKH;
    private String tenKH;
    private String soDienThoai;
    private boolean gioiTinh;
    private int diemTichLuy;
    private int diemDoiThuong;
    private LoaiKhachHang loaiKhachHang;

    public void congDiem(int diem) {
        if (diem <= 0) return;

        this.diemTichLuy += diem;
        this.diemDoiThuong += diem;
        capNhatLoaiKH();
    }

    /**
     * Trừ điểm khi đổi thưởng
     * @return true nếu trừ thành công
     */
    public boolean truDiemDoiThuong(int diem) {
        if (diem <= 0 || diemDoiThuong < diem) return false;
        diemDoiThuong -= diem;
        return true;
    }

    /**
     * Tự động cập nhật loại khách
     */
    private void capNhatLoaiKH() {
        this.loaiKhachHang =
                diemTichLuy >= 1000
                        ? LoaiKhachHang.VIP
                        : LoaiKhachHang.BINHTHUONG;
    }


    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
        capNhatLoaiKH();
    }

    public int getDiemDoiThuong() {
        return diemDoiThuong;
    }

    public void setDiemDoiThuong(int diemDoiThuong) {
        this.diemDoiThuong = diemDoiThuong;
    }

    public LoaiKhachHang getLoaiKhachHang() {
        return loaiKhachHang;
    }

    public void setLoaiKhachHang(LoaiKhachHang loaiKhachHang) {
        this.loaiKhachHang = loaiKhachHang;
    }

    public String getTenGioiTinh() {
        return gioiTinh ? "Nam" : "Nữ";
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", tenKH='" + tenKH + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", gioiTinh=" + getTenGioiTinh() +
                ", diemTichLuy=" + diemTichLuy +
                ", diemDoiThuong=" + diemDoiThuong +
                ", loaiKhachHang=" + loaiKhachHang +
                '}';
    }
}
