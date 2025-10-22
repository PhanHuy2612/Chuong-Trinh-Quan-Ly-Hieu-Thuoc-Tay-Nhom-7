package enums;

public enum PhanQuyen {
    DUOC_SI("Dược Sĩ"),
    QUAN_LY("Quản Lý");

    private final String moTa;

    PhanQuyen(String moTa) {
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
