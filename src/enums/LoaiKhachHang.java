package enums;

public enum LoaiKhachHang {
    VANGLAI("Vãng lai"),
    BINHTHUONG("Thường"),
    VIP("VIP");
    private final String ten;

    LoaiKhachHang(String ten) {
        this.ten = ten;
    }

    public String getTen() {
        return ten;
    }

    @Override
    public String toString() {
        return ten;
    }
}
