package enums;

public enum TrangThaiTaiKhoan {
    DANG_HOAT_DONG("Đang hoạt động"),
    KHOA("Khóa");

    private final String moTa;

    TrangThaiTaiKhoan(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return moTa;
    }

}
