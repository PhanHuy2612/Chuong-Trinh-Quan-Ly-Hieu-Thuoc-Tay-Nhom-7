package enums;

public enum DonViTinh {
    VIEN("Viên"),
    HOP("Hộp"),
    TUYP("Tuýp"),
    CHAI("Chai"),
    GOI("Gói");

    private final String moTa;

    DonViTinh(String moTa) {
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
