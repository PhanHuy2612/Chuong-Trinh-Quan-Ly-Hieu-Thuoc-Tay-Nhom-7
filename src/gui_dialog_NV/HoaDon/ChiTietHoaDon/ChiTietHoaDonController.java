package gui_dialog_NV.HoaDon.ChiTietHoaDon;

import dao.ChiTietHoaDon_DAO;
import entity.ChiTietHoaDon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

public class ChiTietHoaDonController {

    private final ChiTietHoaDonView view;
    private final ChiTietHoaDon_DAO chiTietHoaDonDAO;

    private final String maHoaDon;

    public ChiTietHoaDonController(String maHoaDon) {
        this.maHoaDon = maHoaDon;
        this.view = new ChiTietHoaDonView();
        this.chiTietHoaDonDAO = new ChiTietHoaDon_DAO();

        initData();
        registerEvents();
    }

    /* ================= INIT DATA ================= */

    private void initData() {
        // Set mã hóa đơn lên view
        view.setMaHoaDon(maHoaDon);

        // LẤY CHI TIẾT ĐÚNG THEO MÃ HÓA ĐƠN
        List<ChiTietHoaDon> dsChiTiet =
                chiTietHoaDonDAO.getChiTietByMaHD(maHoaDon);

        ObservableList<ChiTietHoaDon> data =
                FXCollections.observableArrayList(dsChiTiet);

        view.getTable().setItems(data);

        // TÍNH TỔNG TIỀN
        BigDecimal tongTien = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : data) {
            if (ct.getThanhTien() != null) {
                tongTien = tongTien.add(ct.getThanhTien());
            }
        }
        view.setTongTien(tongTien);
    }

    /* ================= EVENTS ================= */

    private void registerEvents() {
        view.getBtnDong().setOnAction(e -> {
            Stage stage = (Stage) view.getRoot()
                    .getScene()
                    .getWindow();
            stage.close();
        });
    }

    /* ================= SHOW DIALOG ================= */

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Chi tiết hóa đơn");

        Scene scene = new Scene(view.getContent());
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.centerOnScreen();

        stage.showAndWait();
    }
}
