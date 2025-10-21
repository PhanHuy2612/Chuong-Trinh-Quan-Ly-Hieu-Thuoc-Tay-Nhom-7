package entity;

import java.util.Objects;

public class CaLam {
    private String maCa;
    private String tenCa;
    private String thoiGianBatDau;
    private String thoiGianKetThuc;

    public CaLam() {
    }

    public CaLam(String maCa, String tenCa, String thoiGianBatDau, String thoiGianKetThuc) {
        this.maCa = maCa;
        this.tenCa = tenCa;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public String getMaCa() {
        return maCa;
    }

    public void setMaCa(String maCa) {
        this.maCa = maCa;
    }

    public String getTenCa() {
        return tenCa;
    }

    public void setTenCa(String tenCa) {
        this.tenCa = tenCa;
    }

    public String getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(String thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public String getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(String thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    @Override
    public String toString() {
        return "CaLam{" +
                "maCa='" + maCa + '\'' +
                ", tenCa='" + tenCa + '\'' +
                ", thoiGianBatDau='" + thoiGianBatDau + '\'' +
                ", thoiGianKetThuc='" + thoiGianKetThuc + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CaLam caLam = (CaLam) o;
        return Objects.equals(maCa, caLam.maCa);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maCa);
    }


}
