package gui_dialog_NV.KhoThuoc.dialog;

import entity.Thuoc;
import enums.DonViTinh;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditThuocDialog {

    private boolean saved = false;

    public boolean isSaved() {
        return saved;
    }

    public void show(Thuoc t) {

        Stage stage = new Stage();
        stage.setTitle("Sửa thông tin thuốc");
        stage.initModality(Modality.APPLICATION_MODAL);

        //-- TEXT FIELDS
        TextField txtTen = new TextField(t.getTenThuoc());
        TextField txtGiaNhap = new TextField(String.valueOf(t.getGiaNhap()));
        TextField txtGiaBan = new TextField(String.valueOf(t.getGiaBan()));
        TextField txtSoLuong = new TextField(String.valueOf(t.getSoLuong()));
        TextField txtTonMin = new TextField(String.valueOf(t.getTonKhoToiThieu()));
        TextField txtTonMax = new TextField(String.valueOf(t.getTonKhoToiDa()));
        TextField txtTiLeDonVi = new TextField(t.getTiLeDonVi());

        //-- COMBOBOX CHO ĐƠN VỊ
        ComboBox<DonViTinh> cbDonViNhap = new ComboBox<>();
        cbDonViNhap.getItems().setAll(DonViTinh.values());
        cbDonViNhap.setValue(t.getDonViNhapKho());

        ComboBox<DonViTinh> cbDonViBan = new ComboBox<>();
        cbDonViBan.getItems().setAll(DonViTinh.values());
        cbDonViBan.setValue(t.getDonViBan());

        Button btnSave = new Button("Lưu");
        btnSave.setStyle("-fx-background-color:#28A745; -fx-text-fill:white;");
        Button btnCancel = new Button("Hủy");

        GridPane gp = new GridPane();
        gp.setPadding(new Insets(15));
        gp.setVgap(10);
        gp.setHgap(10);

        gp.addRow(0, new Label("Tên thuốc:"), txtTen);
        gp.addRow(1, new Label("Giá nhập:"), txtGiaNhap);
        gp.addRow(2, new Label("Giá bán:"), txtGiaBan);
        gp.addRow(3, new Label("Số lượng:"), txtSoLuong);
        gp.addRow(4, new Label("Tồn tối thiểu:"), txtTonMin);
        gp.addRow(5, new Label("Tồn tối đa:"), txtTonMax);

        gp.addRow(6, new Label("Đơn vị nhập:"), cbDonViNhap);
        gp.addRow(7, new Label("Đơn vị bán:"), cbDonViBan);
        gp.addRow(8, new Label("Tỉ lệ đơn vị:"), txtTiLeDonVi);

        gp.addRow(9, btnSave, btnCancel);

        btnCancel.setOnAction(e -> stage.close());

        btnSave.setOnAction(e -> {
            try {
                t.setTenThuoc(txtTen.getText());
                t.setGiaNhap(Double.parseDouble(txtGiaNhap.getText()));
                t.setGiaBan(Double.parseDouble(txtGiaBan.getText()));
                t.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
                t.setTonKhoToiThieu(Integer.parseInt(txtTonMin.getText()));
                t.setTonKhoToiDa(Integer.parseInt(txtTonMax.getText()));

                t.setDonViNhapKho(cbDonViNhap.getValue());
                t.setDonViBan(cbDonViBan.getValue());
                t.setTiLeDonVi(txtTiLeDonVi.getText());


                saved = true;
                stage.close();

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Dữ liệu không hợp lệ!").show();
            }
        });

        stage.setScene(new Scene(gp, 400, 420));
        stage.showAndWait();
    }
}
