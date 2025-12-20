package entity;

import enums.DonViTinh;
import enums.TrangThaiTonKho;
import enums.PhanLoaiThuoc;

import java.time.LocalDate;
import java.util.Objects;

public class Thuoc {

    private String maThuoc;
    private String tenThuoc;
    private double giaBan;
    private double giaNhap;
    private LocalDate hanSuDung;

    private String maKho;
    private String maNhaCungCap;

    private DonViTinh donViNhapKho;
    private DonViTinh donViBan;
    private String tiLeDonVi;

    private int soLuong;
    private TrangThaiTonKho trangThaiTonKho;
    private int tonKhoToiThieu;
    private int tonKhoToiDa;

    private String loaiThuoc;
    private String maLoai;

    private PhanLoaiThuoc phanLoai;

    public Thuoc() {
        this.phanLoai = PhanLoaiThuoc.OTC;
    }

    public Thuoc(String maThuoc, String tenThuoc, double giaBan, double giaNhap,
                 LocalDate hanSuDung, String maKho, String maNhaCungCap,
                 DonViTinh donViNhapKho, DonViTinh donViBan, String tiLeDonVi,
                 int soLuong, TrangThaiTonKho trangThaiTonKho,
                 int tonKhoToiThieu, int tonKhoToiDa,
                 String loaiThuoc, String maLoai,
                 PhanLoaiThuoc phanLoai) {

        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.giaBan = giaBan;
        this.giaNhap = giaNhap;
        this.hanSuDung = hanSuDung;
        this.maKho = maKho;
        this.maNhaCungCap = maNhaCungCap;
        this.donViNhapKho = donViNhapKho;
        this.donViBan = donViBan;
        this.tiLeDonVi = tiLeDonVi;
        this.soLuong = soLuong;
        this.trangThaiTonKho = trangThaiTonKho;
        this.tonKhoToiThieu = tonKhoToiThieu;
        this.tonKhoToiDa = tonKhoToiDa;
        this.loaiThuoc = loaiThuoc;
        this.maLoai = maLoai;
        this.phanLoai = phanLoai;
    }

    // ===== GETTER / SETTER =====

    public String getMaThuoc() { return maThuoc; }
    public void setMaThuoc(String maThuoc) { this.maThuoc = maThuoc; }

    public String getTenThuoc() { return tenThuoc; }
    public void setTenThuoc(String tenThuoc) { this.tenThuoc = tenThuoc; }

    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }

    public double getGiaNhap() { return giaNhap; }
    public void setGiaNhap(double giaNhap) { this.giaNhap = giaNhap; }

    public LocalDate getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(LocalDate hanSuDung) { this.hanSuDung = hanSuDung; }

    public String getMaKho() { return maKho; }
    public void setMaKho(String maKho) { this.maKho = maKho; }

    public String getMaNhaCungCap() { return maNhaCungCap; }
    public void setMaNhaCungCap(String maNhaCungCap) { this.maNhaCungCap = maNhaCungCap; }

    public DonViTinh getDonViNhapKho() { return donViNhapKho; }
    public void setDonViNhapKho(DonViTinh donViNhapKho) { this.donViNhapKho = donViNhapKho; }

    public DonViTinh getDonViBan() { return donViBan; }
    public void setDonViBan(DonViTinh donViBan) { this.donViBan = donViBan; }

    public String getTiLeDonVi() { return tiLeDonVi; }
    public void setTiLeDonVi(String tiLeDonVi) { this.tiLeDonVi = tiLeDonVi; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public TrangThaiTonKho getTrangThaiTonKho() { return trangThaiTonKho; }
    public void setTrangThaiTonKho(TrangThaiTonKho trangThaiTonKho) { this.trangThaiTonKho = trangThaiTonKho; }

    public int getTonKhoToiThieu() { return tonKhoToiThieu; }
    public void setTonKhoToiThieu(int tonKhoToiThieu) { this.tonKhoToiThieu = tonKhoToiThieu; }

    public int getTonKhoToiDa() { return tonKhoToiDa; }
    public void setTonKhoToiDa(int tonKhoToiDa) { this.tonKhoToiDa = tonKhoToiDa; }

    public String getLoaiThuoc() { return loaiThuoc; }
    public void setLoaiThuoc(String loaiThuoc) { this.loaiThuoc = loaiThuoc; }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public PhanLoaiThuoc getPhanLoai() { return phanLoai; }
    public void setPhanLoai(PhanLoaiThuoc phanLoai) { this.phanLoai = phanLoai; }

    // ===== equals / hashCode =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Thuoc)) return false;
        Thuoc thuoc = (Thuoc) o;
        return Objects.equals(maThuoc, thuoc.maThuoc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maThuoc);
    }

    @Override
    public String toString() {
        return String.format(
                "%s | %s | %s | SL: %d %s | %s",
                maThuoc,
                tenThuoc,
                phanLoai != null ? phanLoai.getMoTa() : "Chưa phân loại",
                soLuong,
                donViNhapKho.getMoTa(),
                trangThaiTonKho
        );
    }
}
