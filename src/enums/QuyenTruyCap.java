package enums;

public enum QuyenTruyCap {
    DUOC_SI("Dược Sĩ"),
    QUAN_LY("Quản Lý");

    private final String moTa;

    QuyenTruyCap(String moTa) {
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
