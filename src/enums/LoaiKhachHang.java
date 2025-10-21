package enums;

public enum LoaiKhachHang {
    VANG_LAI("Vãng lai"),
    THUONG("Thường"),
    VIP("VIP");
    private final String moTa;

    LoaiKhachHang(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return moTa;
    }
}
