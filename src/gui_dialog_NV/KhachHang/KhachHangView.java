package gui_dialog_NV.KhachHang;

import entity.KhachHang;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

public class KhachHangView {

    private BorderPane root;
    private TableView<KhachHang> tableKhachHang;

    private TextField txtSearch;
    private Button btnThem;

    private ComboBox<String> cboLoaiKH;
    private ComboBox<String> cboGioiTinh;
    private TextField txtDiemMin;

    public KhachHangView() {
        initUI();
    }

    private void initUI() {
        root = new BorderPane();
        root.setPadding(new Insets(20));

        /* ===================== TITLE ===================== */
        Label title = new Label("Quản lý khách hàng");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        /* ===================== SEARCH + ADD ===================== */
        txtSearch = new TextField();
        txtSearch.setPromptText("Tìm theo tên hoặc số điện thoại...");
        txtSearch.setPrefWidth(300);

        btnThem = new Button("+ Thêm khách hàng");
        btnThem.setStyle("-fx-background-color: #28A745; -fx-text-fill: white;");

        HBox header = new HBox(15, txtSearch, btnThem);
        header.setAlignment(Pos.CENTER_LEFT);

        /* ===================== FILTER ===================== */
        cboLoaiKH = new ComboBox<>();
        cboLoaiKH.getItems().addAll("Tất cả", "VIP", "Bình thường");
        cboLoaiKH.setValue("Tất cả");

        cboGioiTinh = new ComboBox<>();
        cboGioiTinh.getItems().addAll("Tất cả", "Nam", "Nữ");
        cboGioiTinh.setValue("Tất cả");

        txtDiemMin = new TextField();
        txtDiemMin.setPromptText("Điểm ≥");
        txtDiemMin.setPrefWidth(80);

        HBox filterBox = new HBox(15,
                new Label("Loại:"), cboLoaiKH,
                new Label("Giới tính:"), cboGioiTinh,
                new Label("Điểm ≥"), txtDiemMin
        );
        filterBox.setAlignment(Pos.CENTER_LEFT);

        VBox top = new VBox(12, title, header, filterBox);
        root.setTop(top);

        /* ===================== TABLE ===================== */
        tableKhachHang = new TableView<>();
        tableKhachHang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableKhachHang.setPlaceholder(new Label("Không có dữ liệu khách hàng"));

        // Mã KH
        TableColumn<KhachHang, String> colMaKH = new TableColumn<>("Mã KH");
        colMaKH.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getMaKH())
        );

        // Tên KH
        TableColumn<KhachHang, String> colTen = new TableColumn<>("Tên khách hàng");
        colTen.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTenKH())
        );

        // SĐT
        TableColumn<KhachHang, String> colSDT = new TableColumn<>("Số điện thoại");
        colSDT.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getSoDienThoai())
        );

        // Giới tính
        TableColumn<KhachHang, String> colGioiTinh = new TableColumn<>("Giới tính");
        colGioiTinh.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTenGioiTinh())
        );

        // Điểm tích lũy
        TableColumn<KhachHang, Integer> colDiem = new TableColumn<>("Điểm tích lũy");
        colDiem.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getDiemTichLuy())
        );
        colDiem.setStyle("-fx-alignment: CENTER-RIGHT;");

        // Loại KH
        TableColumn<KhachHang, String> colLoai = new TableColumn<>("Loại KH");
        colLoai.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getLoaiKhachHang().toString())
        );
        colLoai.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else if ("VIP".equalsIgnoreCase(item)) {
                    setText("VIP");
                    setStyle("-fx-text-fill: #D4AF37; -fx-font-weight: bold; -fx-alignment: CENTER;");
                } else {
                    setText("Bình thường");
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });

        // Thao tác
        TableColumn<KhachHang, Void> colAction = new TableColumn<>("Thao tác");
        colAction.setSortable(false);
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<KhachHang, Void> call(TableColumn<KhachHang, Void> param) {
                return new TableCell<>() {

                    private final Button btnSua = new Button("Sửa");
                    private final Button btnXoa = new Button("Xóa");

                    {
                        btnSua.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white;");
                        btnXoa.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white;");
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox box = new HBox(8, btnSua, btnXoa);
                            box.setAlignment(Pos.CENTER);
                            setGraphic(box);
                        }
                    }
                };
            }
        });

        tableKhachHang.getColumns().addAll(
                colMaKH,
                colTen,
                colSDT,
                colGioiTinh,
                colDiem,
                colLoai,
                colAction
        );

        root.setCenter(tableKhachHang);
    }

    /* ===================== GETTER ===================== */
    public Node getRoot() {
        return root;
    }

    public TableView<KhachHang> getTableKhachHang() {
        return tableKhachHang;
    }

    public TextField getTxtSearch() {
        return txtSearch;
    }

    public Button getBtnThem() {
        return btnThem;
    }

    public ComboBox<String> getCboLoaiKH() {
        return cboLoaiKH;
    }

    public ComboBox<String> getCboGioiTinh() {
        return cboGioiTinh;
    }

    public TextField getTxtDiemMin() {
        return txtDiemMin;
    }
}
