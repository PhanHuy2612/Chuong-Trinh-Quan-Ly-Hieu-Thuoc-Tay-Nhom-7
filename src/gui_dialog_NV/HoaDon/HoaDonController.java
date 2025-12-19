package gui_dialog_NV.HoaDon;

import dao.HoaDon_DAO;
import entity.HoaDon;
import gui_dialog_NV.HoaDon.ChiTietHoaDon.ChiTietHoaDonController;
import gui_dialog_NV.HoaDon.ChiTietHoaDon.ChiTietHoaDonView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

public class HoaDonController {

    private final HoaDonView view;
    private final HoaDon_DAO hoaDonDAO;

    private final ObservableList<HoaDon> data = FXCollections.observableArrayList();

    public HoaDonController(HoaDonView view) {
        this.view = view;
        this.hoaDonDAO = new HoaDon_DAO();

        init();
    }

    private void init() {
        loadHoaDon();
        registerEvents();
    }

    private void registerEvents() {
        view.getBtnTimKiem().setOnAction(e -> loadHoaDon());
        view.setOnChiTietClickListener(this::openChiTietHoaDon);
    }

    private void loadHoaDon() {
        List<HoaDon> ds = hoaDonDAO.getAllHoaDon();

        data.setAll(ds);
        view.getTableHoaDon().setItems(data);

        capNhatThongKe(ds);
    }

    private void capNhatThongKe(List<HoaDon> ds) {
        int tongHD = ds.size();

        BigDecimal doanhThu = ds.stream()
                .map(HoaDon::getTongTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal giaTriTB = tongHD > 0
                ? doanhThu.divide(BigDecimal.valueOf(tongHD), BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;

        setCardValue(view.getTongHoaDonCard(), String.valueOf(tongHD));
        setCardValue(view.getDoanhThuCard(), formatTien(doanhThu));
        setCardValue(view.getGiaTriTBCard(), formatTien(giaTriTB));

        view.getCountLabel().setText("Hiển thị " + tongHD + " hóa đơn");
    }

    private void setCardValue(javafx.scene.layout.VBox card, String value) {
        Label lblValue = (Label) card.getChildren().get(1);
        lblValue.setText(value);
    }

    private String formatTien(BigDecimal tien) {
        return tien == null ? "0 đ" : tien.toPlainString() + " đ";
    }
    private void openChiTietHoaDon(HoaDon hd) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Chi tiết hóa đơn");

            ChiTietHoaDonView ctView = new ChiTietHoaDonView();
            new ChiTietHoaDonController(
                    ctView,
                    hd.getMaHD(),
                    stage
            );

            Scene scene = new Scene(ctView.getContent());
            stage.setScene(scene);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
