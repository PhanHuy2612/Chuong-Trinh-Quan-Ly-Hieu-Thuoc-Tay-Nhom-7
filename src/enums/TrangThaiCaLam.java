package enums;

public enum TrangThaiCaLam {
    CHUA_BAT_DAU("Chưa bắt đầu"),
    DANG_LAM("Đang làm"),
    VANG_MAT("Vắng mặt"),
    HOAN_THANH("Hoàn thành");

    private final String moTa;

    TrangThaiCaLam(String moTa) {
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
