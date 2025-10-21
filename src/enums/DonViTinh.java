package enums;

public enum DonViTinh {
    VIEN("Viên"),
    HOP("Hộp"),
    VI("Vỉ"),
    CHAI("Chai");

    private final String moTa;

    DonViTinh(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return moTa;
    }
}
