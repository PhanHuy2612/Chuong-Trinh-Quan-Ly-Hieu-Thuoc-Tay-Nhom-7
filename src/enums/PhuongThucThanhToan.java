package enums;

public enum PhuongThucThanhToan {
    TIEN_MAT("Tiền mặt"),
    CHUYEN_KHOAN("Chuyển khoản");

    private final String displayName;

    PhuongThucThanhToan(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
