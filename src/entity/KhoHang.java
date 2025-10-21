package entity;

import java.util.Objects;

public class KhoHang {
    private String maKho;
    private String tenKho;
    private String diaChi;

    public KhoHang() {
    }

    public KhoHang(String maKho, String tenKho, String diaChi) {
        this.maKho = maKho;
        this.tenKho = tenKho;
        this.diaChi = diaChi;
    }

    public String getMaKho() {
        return maKho;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public String getTenKho() {
        return tenKho;
    }

    public void setTenKho(String tenKho) {
        this.tenKho = tenKho;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        KhoHang khoHang = (KhoHang) o;
        return Objects.equals(maKho, khoHang.maKho);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKho);
    }

    @Override
    public String toString() {
        return "KhoHang{" +
                "maKho='" + maKho + '\'' +
                ", tenKho='" + tenKho + '\'' +
                ", diaChi='" + diaChi + '\'' +
                '}';
    }
}
