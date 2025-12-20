package enums;

public enum PhanLoaiThuoc {
    RX("Thuốc kê đơn"),
    OTC("Thuốc không kê đơn"),
    TPCN("Thực phẩm chức năng"),
    VITAMIN("Vitamin & khoáng chất");

    private final String moTa;

    PhanLoaiThuoc(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
