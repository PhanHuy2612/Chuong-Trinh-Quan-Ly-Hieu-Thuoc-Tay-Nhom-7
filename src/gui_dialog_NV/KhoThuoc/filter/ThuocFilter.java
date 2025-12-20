package gui_dialog_NV.KhoThuoc.filter;

import entity.Thuoc;
import javafx.beans.property.*;
import javafx.collections.transformation.FilteredList;

public class ThuocFilter {

    private final StringProperty searchText = new SimpleStringProperty("");
    private final ObjectProperty<String> selectedKho = new SimpleObjectProperty<>("Tất cả kho");
    private final ObjectProperty<String> selectedNCC = new SimpleObjectProperty<>("Tất cả nhà cung cấp");

    public ThuocFilter(FilteredList<Thuoc> filteredList) {
        Runnable apply = () -> filteredList.setPredicate(this::match);
        searchText.addListener((a,b,c) -> apply.run());
        selectedKho.addListener((a,b,c) -> apply.run());
        selectedNCC.addListener((a,b,c) -> apply.run());
        apply.run();
    }

    private boolean match(Thuoc t) {
        // 1. Search
        String txt = searchText.get().trim().toLowerCase();
        boolean matchSearch = txt.isEmpty()
                || "*".equals(txt)
                || t.getTenThuoc().toLowerCase().contains(txt)
                || t.getMaThuoc().toLowerCase().contains(txt);

        boolean matchKho = "Tất cả kho".equals(selectedKho.get())
                || selectedKho.get().equals(t.getMaKho());

        boolean matchNcc = "Tất cả nhà cung cấp".equals(selectedNCC.get())
                || selectedNCC.get().equals(t.getMaNhaCungCap());

        return matchSearch && matchKho && matchNcc;
    }

    // Getter properties
    public StringProperty searchTextProperty() { return searchText; }
    public ObjectProperty<String> selectedKhoProperty() { return selectedKho; }
    public ObjectProperty<String> selectedNccProperty() { return selectedNCC; }
}
