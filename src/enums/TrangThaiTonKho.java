package enums;

public enum TrangThaiTonKho {
    CON_HANG("Còn hàng"),
    HET_HANG("Hết hàng"),
    SAP_HET_HANG("Sắp hết hàng");

    private final String moTa;

    TrangThaiTonKho(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return moTa;
    }
}
