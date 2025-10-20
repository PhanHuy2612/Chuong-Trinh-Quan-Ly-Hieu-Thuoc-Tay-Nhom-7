package enums;

public enum TrangThaiTaiKhoan {
    DANG_HOAT_DONG("Đang hoạt động"),
    KHOA("Khóa");

    private final String moTa;

    TrangThaiTaiKhoan(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }

}
