package gui_dialog_QL.KhuyenMai;

import entity.KhuyenMai;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class KhuyenMaiView {

    private final VBox root = new VBox(5);

    private Label lblTongKM;
    private Label lblDangApDung;
    private Label lblSapBatDau;
    private Label lblDaKetThuc;

    private TextField txtSearch;
    private DatePicker dpFrom;
    private DatePicker dpTo;
    private ComboBox<String> cbTrangThai;
    private Button btnLamMoi;

    private TableView<KhuyenMai> tableKhuyenMai;
    private Button btnThemKhuyenMai;

    public KhuyenMaiView() {
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #F5F6FA;");

        root.getChildren().addAll(
                createTitle(),
                createDashboardAndFilterSection(),
                createTableSection(),
                createActionBar()
        );
    }

    private Node createTitle() {
        VBox box = new VBox(5);

        Label title = new Label("Quản lý khuyến mãi");
        title.setFont(Font.font("System", FontWeight.BOLD, 26));

        Label subtitle = new Label("Tạo, cập nhật và theo dõi các chương trình khuyến mãi");
        subtitle.setTextFill(Color.GRAY);

        box.getChildren().addAll(title, subtitle);
        return box;
    }

    private HBox createDashboardAndFilterSection() {
        HBox box = new HBox(5);
        box.setAlignment(Pos.TOP_LEFT);

        VBox dashboardBox = createDashboard();
        HBox.setHgrow(dashboardBox, Priority.ALWAYS);

        VBox filterBox = (VBox) createFilterSection();
        filterBox.setMaxWidth(700);
        HBox.setHgrow(filterBox, Priority.ALWAYS);

        box.getChildren().addAll(dashboardBox, filterBox);
        return box;
    }

    private VBox createDashboard() {
        HBox statBox = new HBox(15);

        lblTongKM = new Label("0");
        lblDangApDung = new Label("0");
        lblSapBatDau = new Label("0");
        lblDaKetThuc = new Label("0");

        statBox.getChildren().addAll(
                createStatCard("Tổng khuyến mãi", lblTongKM, "#0984e3"),
                createStatCard("Đang áp dụng", lblDangApDung, "#00b894"),
                createStatCard("Sắp bắt đầu", lblSapBatDau, "#fdcb6e"),
                createStatCard("Đã kết thúc", lblDaKetThuc, "#d63031")
        );

        VBox box = new VBox(statBox);
        return box;
    }

    private VBox createStatCard(String title, Label value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setPrefWidth(150);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 3);"
        );

        Label lblTitle = new Label(title);
        lblTitle.setTextFill(Color.GRAY);

        value.setFont(Font.font("System", FontWeight.BOLD, 26));
        value.setTextFill(Color.web(color));

        card.getChildren().addAll(lblTitle, value);
        return card;
    }

    private Node createFilterSection() {
        VBox box = new VBox(5);
        box.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 12; -fx-border-color: #E9ECEF; -fx-border-radius: 12; -fx-border-width: 1;");

        Label filterTitle = new Label("Bộ lọc và tìm kiếm");
        filterTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        txtSearch = new TextField();
        txtSearch.setPromptText("Tìm theo mã hoặc tên khuyến mãi...");
        txtSearch.setPrefWidth(200);
        grid.add(createFilterLabel("Tìm kiếm"), 0, 0);
        grid.add(txtSearch, 1, 0, 2, 1);

        dpFrom = new DatePicker();
        dpFrom.setPromptText("Từ ngày");
        dpTo = new DatePicker();
        dpTo.setPromptText("Đến ngày");
        grid.add(createFilterLabel("Thời gian"), 0, 1);
        HBox dateBox = new HBox(10, dpFrom, new Label("-"), dpTo);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(dateBox, 1, 1, 2, 1);

        cbTrangThai = new ComboBox<>();
        cbTrangThai.getItems().addAll("Tất cả", "Đang áp dụng", "Sắp bắt đầu", "Đã kết thúc");
        cbTrangThai.setValue("Tất cả");
        cbTrangThai.setPrefWidth(200);
        grid.add(createFilterLabel("Trạng thái"), 0, 2);
        grid.add(cbTrangThai, 1, 2);

        btnLamMoi = new Button("Làm mới");
        btnLamMoi.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-padding: 8 24; -fx-font-size: 14;");

        HBox btnBox = new HBox(btnLamMoi);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.setPadding(new Insets(0, 0, 0, 0));

        box.getChildren().addAll(filterTitle, grid, btnBox);
        return box;
    }

    private Label createFilterLabel(String text) {
        Label lbl = new Label(text);
        lbl.setTextFill(Color.GRAY);
        lbl.setFont(Font.font(14));
        return lbl;
    }

    /* ================= TABLE ================= */
    private Node createTableSection() {
        tableKhuyenMai = new TableView<>();
        tableKhuyenMai.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableKhuyenMai.setPlaceholder(new Label("Không có dữ liệu khuyến mãi"));

        // Cấu hình các cột
        TableColumn<KhuyenMai, String> colMa = new TableColumn<>("Mã KM");
        colMa.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaKM()));
        colMa.setPrefWidth(100);

        TableColumn<KhuyenMai, String> colTen = new TableColumn<>("Tên khuyến mãi");
        colTen.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTenKM()));
        colTen.setPrefWidth(300);

        TableColumn<KhuyenMai, String> colPhanTramGiam = new TableColumn<>("% giảm");
        colPhanTramGiam.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPhanTramGiam() + "%"));
        colPhanTramGiam.setPrefWidth(100);
        colPhanTramGiam.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<KhuyenMai, BigDecimal> colTienGiamToiDa = new TableColumn<>("Giảm tối đa");
        colTienGiamToiDa.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getTienGiamToiDa())
        );
        colTienGiamToiDa.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Không giới hạn");
                    setStyle("-fx-text-fill: gray; -fx-alignment: CENTER-RIGHT;");
                } else {
                    setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(item));
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });
        colTienGiamToiDa.setPrefWidth(160);

        TableColumn<KhuyenMai, String> colLoaiApDung = new TableColumn<>("Loại áp dụng");
        colLoaiApDung.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getLoaiThuocApDung() == null || c.getValue().getLoaiThuocApDung().isEmpty()
                        ? "Toàn bộ" : c.getValue().getLoaiThuocApDung()
        ));
        colLoaiApDung.setPrefWidth(200);

        TableColumn<KhuyenMai, LocalDate> colBatDau = new TableColumn<>("Bắt đầu");
        colBatDau.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getNgayBatDau())
        );
        colBatDau.setPrefWidth(130);

        TableColumn<KhuyenMai, LocalDate> colKetThuc = new TableColumn<>("Kết thúc");
        colKetThuc.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getNgayKetThuc())
        );
        colKetThuc.setPrefWidth(130);

        TableColumn<KhuyenMai, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(c -> new SimpleStringProperty(calculateTrangThai(c.getValue())));
        colTrangThai.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Đang áp dụng".equals(item)) {
                        setStyle("-fx-text-fill: #00b894; -fx-font-weight: bold;");
                    } else if ("Sắp bắt đầu".equals(item)) {
                        setStyle("-fx-text-fill: #fdcb6e;");
                    } else if ("Đã kết thúc".equals(item)) {
                        setStyle("-fx-text-fill: #d63031;");
                    }
                }
            }
        });
        colTrangThai.setPrefWidth(130);

        TableColumn<KhuyenMai, String> colNguoiTao = new TableColumn<>("Người tạo");
        colNguoiTao.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMaNhanVien()));
        colNguoiTao.setPrefWidth(120);

        TableColumn<KhuyenMai, LocalDateTime> colNgayTao = new TableColumn<>("Ngày tạo");
        colNgayTao.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getNgayTao())
        );
        colNgayTao.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(formatter));
            }
        });
        colNgayTao.setPrefWidth(160);

        TableColumn<KhuyenMai, Void> colAction = new TableColumn<>("Thao tác");
        colAction.setPrefWidth(140);
        colAction.setCellFactory(createActionCellFactory());

        tableKhuyenMai.getColumns().addAll(
                colMa, colTen, colPhanTramGiam, colTienGiamToiDa,
                colLoaiApDung, colBatDau, colKetThuc, colTrangThai,
                colNguoiTao, colNgayTao, colAction
        );

        VBox box = new VBox(10);
        VBox.setVgrow(tableKhuyenMai, Priority.ALWAYS);
        box.getChildren().addAll(tableKhuyenMai);
        return box;
    }

    private String calculateTrangThai(KhuyenMai km) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(km.getNgayBatDau())) return "Sắp bắt đầu";
        if (today.isAfter(km.getNgayKetThuc())) return "Đã kết thúc";
        return "Đang áp dụng";
    }

    private Callback<TableColumn<KhuyenMai, Void>, TableCell<KhuyenMai, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button btnSua = new Button("Sửa");
            private final Button btnXoa = new Button("Xóa");

            {
                btnSua.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-padding: 4 12;");
                btnXoa.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-padding: 4 12;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actions = new HBox(8, btnSua, btnXoa);
                    actions.setAlignment(Pos.CENTER);
                    setGraphic(actions);

                    btnSua.setOnAction(e -> System.out.println("Sửa: " + getTableView().getItems().get(getIndex()).getMaKM()));
                    btnXoa.setOnAction(e -> System.out.println("Xóa: " + getTableView().getItems().get(getIndex()).getMaKM()));
                }
            }
        };
    }

    /* ================= ACTION BAR ================= */
    private HBox createActionBar() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_RIGHT);

        btnThemKhuyenMai = new Button("+ Tạo khuyến mãi");
        btnThemKhuyenMai.setStyle(
                "-fx-background-color: #0984e3;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 10;"
        );

        box.getChildren().add(btnThemKhuyenMai);
        return box;
    }

    public VBox getRoot() { return root; }
    public TableView<KhuyenMai> getTableKhuyenMai() { return tableKhuyenMai; }
    public Button getBtnThemKhuyenMai() { return btnThemKhuyenMai; }
    public TextField getTxtSearch() { return txtSearch; }
    public DatePicker getDpFrom() { return dpFrom; }
    public DatePicker getDpTo() { return dpTo; }
    public ComboBox<String> getCbTrangThai() { return cbTrangThai; }
    public Button getBtnLamMoi() { return btnLamMoi; }
    public Label getLblTongKM() { return lblTongKM; }
    public Label getLblDangApDung() { return lblDangApDung; }
    public Label getLblSapBatDau() { return lblSapBatDau; }
    public Label getLblDaKetThuc() { return lblDaKetThuc; }
}
