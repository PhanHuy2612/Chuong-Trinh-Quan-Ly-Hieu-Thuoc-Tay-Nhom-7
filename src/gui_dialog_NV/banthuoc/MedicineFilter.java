// gui_dialog_NV/banthuoc/MedicineFilter.java
package gui_dialog_NV.banthuoc;

import entity.Thuoc;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MedicineFilter {

    /* ================== PROPERTIES ================== */

    private final StringProperty keyword = new SimpleStringProperty("");
    private final StringProperty loaiThuoc = new SimpleStringProperty("Loại thuốc");
    private final StringProperty nhaCungCap = new SimpleStringProperty("Nhà cung cấp");
    private final StringProperty kho = new SimpleStringProperty("Kho thuốc");

    private final BooleanProperty onlyConHang = new SimpleBooleanProperty(false);
    private final BooleanProperty onlySapHet = new SimpleBooleanProperty(false);

    /* ================== DATA ================== */

    private final ObservableList<Thuoc> sourceList = FXCollections.observableArrayList();
    private final ObservableList<Thuoc> filteredList = FXCollections.observableArrayList();

    public MedicineFilter() {
        keyword.addListener((obs, o, n) -> applyFilter());
        loaiThuoc.addListener((obs, o, n) -> applyFilter());
        nhaCungCap.addListener((obs, o, n) -> applyFilter());
        kho.addListener((obs, o, n) -> applyFilter());
        onlyConHang.addListener((obs, o, n) -> applyFilter());
        onlySapHet.addListener((obs, o, n) -> applyFilter());
    }

    /* ================== CORE ================== */

    public void setSourceList(List<Thuoc> list) {
        sourceList.setAll(list);
        applyFilter();
    }

    public void applyFilter() {
        filteredList.setAll(
                sourceList.stream()
                        .filter(this::matchKeyword)
                        .filter(this::matchLoai)
                        .filter(this::matchNCC)
                        .filter(this::matchKho)
                        .filter(this::matchTrangThai)
                        .toList()
        );
    }

    /* ================== RESET ================== */

    public void reset() {
        keyword.set("");
        loaiThuoc.set("Loại thuốc");
        nhaCungCap.set("Nhà cung cấp");
        kho.set("Kho thuốc");
        onlyConHang.set(false);
        onlySapHet.set(false);
        applyFilter();
    }

    /* ================== CONDITIONS ================== */

    private boolean matchKeyword(Thuoc t) {
        if (keyword.get().isBlank()) return true;
        return t.getTenThuoc().toLowerCase()
                .contains(keyword.get().toLowerCase());
    }

    private boolean matchLoai(Thuoc t) {
        String v = loaiThuoc.get();
        if (v == null || v.equals("Loại thuốc") || v.equals("Tất cả")) return true;
        return v.equals(t.getLoaiThuoc());
    }

    private boolean matchNCC(Thuoc t) {
        String v = nhaCungCap.get();
        if (v == null || v.equals("Nhà cung cấp") || v.equals("Tất cả")) return true;
        return v.equals(t.getMaNhaCungCap());
    }

    private boolean matchKho(Thuoc t) {
        String v = kho.get();
        if (v == null || v.equals("Kho thuốc") || v.equals("Tất cả")) return true;
        return v.equals(t.getMaKho());
    }

    private boolean matchTrangThai(Thuoc t) {
        if (onlyConHang.get() && t.getSoLuong() <= 0) return false;
        if (onlySapHet.get() && t.getSoLuong() > 10) return false;
        return true;
    }

    /* ================== GETTERS ================== */

    public ObservableList<Thuoc> getFilteredList() {
        return filteredList;
    }

    public StringProperty keywordProperty() { return keyword; }
    public StringProperty loaiThuocProperty() { return loaiThuoc; }
    public StringProperty nhaCungCapProperty() { return nhaCungCap; }
    public StringProperty khoProperty() { return kho; }
    public BooleanProperty onlyConHangProperty() { return onlyConHang; }
    public BooleanProperty onlySapHetProperty() { return onlySapHet; }
}
