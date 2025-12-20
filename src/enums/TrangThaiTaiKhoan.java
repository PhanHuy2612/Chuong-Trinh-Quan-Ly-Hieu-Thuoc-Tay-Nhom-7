package enums;

public enum TrangThaiTaiKhoan {
    DANGHOATDONG("Đang hoạt động"),
    KHOA("Khóa");

    private final String moTa;

    TrangThaiTaiKhoan(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }

    @Override
    public String toString() {
        return moTa;
    }

}
