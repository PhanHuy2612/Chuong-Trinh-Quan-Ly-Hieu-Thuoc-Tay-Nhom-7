package entity;

import java.time.LocalTime;
import java.util.Objects;

public class CaLam {
    private String maCa;
    private String tenCa;
    private LocalTime thoiGianBatDau;
    private LocalTime thoiGianKetThuc;

    public CaLam() {
    }

    public CaLam(String maCa, String tenCa, LocalTime thoiGianBatDau, LocalTime thoiGianKetThuc) {
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

    public LocalTime getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(LocalTime thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public LocalTime getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(LocalTime thoiGianKetThuc) {
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
