package enums;

// Trong enum TrangThaiTonKho.java - Thêm method fromString đầy đủ
public enum TrangThaiTonKho {
    CON_HANG("CÒN_HÀNG"),
    TON_KHO("TỒN_KHO"),
    HET_HANG("HẾT_HÀNG"),
    SAP_HET_HANG("SẮP_HẾT_HÀNG");
    private final String displayName;

    TrangThaiTonKho(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TrangThaiTonKho fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return TON_KHO;
        }
        for (TrangThaiTonKho b : TrangThaiTonKho.values()) {
            if (b.displayName.equalsIgnoreCase(text.trim())) {
                return b;
            }
        }
        return TON_KHO;
    }
}