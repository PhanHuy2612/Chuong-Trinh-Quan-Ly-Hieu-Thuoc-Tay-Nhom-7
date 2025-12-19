package gui_dialog_NV.LichLam;

import entity.LichLam;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.format.DateTimeFormatter;

public class LichLamView {

    private BorderPane root;
    private TableView<LichLam> tableLichLam;

    private TextField txtSearch;
    private DatePicker dpTuNgay;
    private DatePicker dpDenNgay;
    private Button btnTimKiem;

    private Label lblTenNhanVien;

    public LichLamView() {
        initUI();
    }

    private void initUI() {
        root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("Lịch làm việc");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        lblTenNhanVien = new Label();
        lblTenNhanVien.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555;");

        txtSearch = new TextField();
        txtSearch.setPromptText("Tìm theo tên ca, vị trí...");
        txtSearch.setPrefWidth(300);

        dpTuNgay = new DatePicker();
        dpTuNgay.setPromptText("Từ ngày");

        dpDenNgay = new DatePicker();
        dpDenNgay.setPromptText("Đến ngày");

        btnTimKiem = new Button("Tìm kiếm");
        btnTimKiem.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white;");

        HBox filterBox = new HBox(10,
                new Label("Từ:"), dpTuNgay,
                new Label("Đến:"), dpDenNgay,
                btnTimKiem
        );
        filterBox.setAlignment(Pos.CENTER_LEFT);

        HBox topBox = new HBox(20, txtSearch, filterBox);
        topBox.setAlignment(Pos.CENTER_LEFT);

        VBox top = new VBox(10, title, lblTenNhanVien, topBox);
        root.setTop(top);

        tableLichLam = new TableView<>();
        tableLichLam.setPlaceholder(new Label("Không có lịch làm nào"));
        tableLichLam.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<LichLam, String> colCa = new TableColumn<>("Ca làm");
        colCa.setCellValueFactory(c -> {
            var ca = c.getValue().getCaLam();
            return new javafx.beans.property.SimpleStringProperty(
                    ca != null ? ca.getTenCaLam() : ""
            );
        });

        TableColumn<LichLam, String> colNgay = new TableColumn<>("Ngày làm");
        colNgay.setCellValueFactory(c -> {
            var lich = c.getValue();
            if (lich == null || lich.getNgayLam() == null)
                return new SimpleStringProperty("");

            var df = DateTimeFormatter.ofPattern("dd/MM/yyyy (EEEE)");
            return new SimpleStringProperty(lich.getNgayLam().format(df));
        });
        TableColumn<LichLam, String> colGio = new TableColumn<>("Giờ làm");
        colGio.setCellValueFactory(c -> {
            var ca = c.getValue().getCaLam();
            if (ca == null || ca.getGioBatDau() == null || ca.getGioKetThuc() == null)
                return new SimpleStringProperty("");
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
            return new SimpleStringProperty(
                    ca.getGioBatDau().format(tf)
                            + " - " +
                            ca.getGioKetThuc().format(tf)
            );
        });




        TableColumn<LichLam, String> colViTri = new TableColumn<>("Vị trí");
        colViTri.setCellValueFactory(c -> {
            var ca = c.getValue().getCaLam();
            return new javafx.beans.property.SimpleStringProperty(
                    ca != null ? ca.getViTriLamViec() : ""
            );
        });

        TableColumn<LichLam, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(c -> {
            var tt = c.getValue().getTrangThaiCaLam();
            return new javafx.beans.property.SimpleStringProperty(
                    tt != null ? tt.getMoTa() : "Chưa bắt đầu"
            );
        });

        colTrangThai.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Hoàn thành" -> setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                        case "Đang làm" -> setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
                        case "Chưa bắt đầu" -> setStyle("-fx-text-fill: orange;");
                        default -> setStyle("");
                    }
                }
            }
        });

        tableLichLam.getColumns().addAll(
                colCa,
                colNgay,
                colGio,
                colViTri,
                colTrangThai
        );


        root.setCenter(tableLichLam);
    }

    public BorderPane getRoot() { return root; }
    public TableView<LichLam> getTableLichLam() { return tableLichLam; }
    public TextField getTxtSearch() { return txtSearch; }
    public DatePicker getDpTuNgay() { return dpTuNgay; }
    public DatePicker getDpDenNgay() { return dpDenNgay; }
    public Button getBtnTimKiem() { return btnTimKiem; }

    public void setTenNhanVien(String tenNV) {
        lblTenNhanVien.setText("Lịch làm việc của: " + tenNV);
    }

    public void setItems(ObservableList<LichLam> items) {
        tableLichLam.setItems(items);
    }
}
