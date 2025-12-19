package gui_dialog_NV.HoaDon.ChiTietHoaDon;

import dao.ChiTietHoaDon_DAO;
import entity.ChiTietHoaDon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

public class ChiTietHoaDonController {

    private final ChiTietHoaDonView view;
    private final ChiTietHoaDon_DAO chiTietDAO;

    private final ObservableList<ChiTietHoaDon> data =
            FXCollections.observableArrayList();

    private final String maHoaDon;

    public ChiTietHoaDonController(
            ChiTietHoaDonView view,
            String maHoaDon,
            Stage stage
    ) {
        this.view = view;
        this.maHoaDon = maHoaDon;
        this.chiTietDAO = new ChiTietHoaDon_DAO();

        init(stage);
    }

    /* ================= INIT ================= */

    private void init(Stage stage) {
        view.setMaHoaDon(maHoaDon);
        loadData();
        registerEvents(stage);
    }

    /* ================= LOAD DATA ================= */

    private void loadData() {
        List<ChiTietHoaDon> ds =
                chiTietDAO.getChiTietByMaHD(maHoaDon);

        data.setAll(ds);
        view.getTable().setItems(data);

        capNhatTongTien(ds);
    }

    private void capNhatTongTien(List<ChiTietHoaDon> ds) {
        BigDecimal tong = ds.stream()
                .map(ChiTietHoaDon::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        view.setTongTien(tong);
    }

    private void registerEvents(Stage stage) {
        view.getBtnDong().setOnAction(e -> stage.close());
    }
}
