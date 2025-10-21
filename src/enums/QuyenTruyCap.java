package enums;

public enum QuyenTruyCap {
    DUOCSI("Dược Sĩ"),
    QUANLY("Quản Lý");

    private final String moTa;

    QuyenTruyCap(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return moTa;
    }
}
