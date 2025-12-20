package gui_dialog_NV.HoaDon;

import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import enums.PhuongThucThanhToan;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HoaDonView {

    private OnChiTietClickListener chiTietListener;
    public interface OnChiTietClickListener {
        void onClick(HoaDon hoaDon);
    }
    public void setOnChiTietClickListener(OnChiTietClickListener listener) {
        this.chiTietListener = listener;
    }
    private TableView<HoaDon> tableHoaDon;
    private VBox tongHoaDonCard, doanhThuCard, giaTriTBCard;
    private Label countLabel;
    private Button btnTimKiem;

    public HoaDonView() {
        tableHoaDon = new TableView<>();
        tableHoaDon.setPlaceholder(new Label("Đang tải dữ liệu..."));
        tableHoaDon.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public Pane getContent() {
        VBox main = new VBox(20);
        main.setPadding(new Insets(20));

        HBox header = createHeader();

        HBox middleRow = new HBox(30);
        middleRow.setAlignment(Pos.CENTER_LEFT);

        HBox statCardsBox = createStatCards();
        HBox.setHgrow(statCardsBox, Priority.ALWAYS);

        VBox filterBox = createFilterSection();
        filterBox.setPrefWidth(400);
        filterBox.setMaxWidth(450);
        filterBox.setMinHeight(180);

        middleRow.getChildren().addAll(statCardsBox, filterBox);

        VBox tableSection = createTableSection();
        VBox.setVgrow(tableSection, Priority.ALWAYS);

        main.getChildren().addAll(header, middleRow, tableSection);

        return main;
    }

    public TableView<HoaDon> getTableHoaDon() {
        return tableHoaDon;
    }

    public Button getBtnTimKiem() {
        return btnTimKiem;
    }

    public VBox getTongHoaDonCard() {
        return tongHoaDonCard;
    }

    public VBox getDoanhThuCard() {
        return doanhThuCard;
    }

    public VBox getGiaTriTBCard() {
        return giaTriTBCard;
    }

    public Label getCountLabel() {
        return countLabel;
    }

    private HBox createHeader() {
        Label title = new Label("Quản lý hóa đơn");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2C3E50"));

        Label subtitle = new Label("Xem và quản lý tất cả hóa đơn bán hàng");
        subtitle.setStyle("-fx-text-fill: #7F8C8D;");

        VBox left = new VBox(5, title, subtitle);
        HBox header = new HBox(left);
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private HBox createStatCards() {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);

        tongHoaDonCard = createStatCard("Tổng hóa đơn", "0", "Từ dữ liệu", "#3498DB");
        doanhThuCard = createStatCard("Doanh thu", "0 đ", "Tổng tiền bán", "#27AE60");
        giaTriTBCard = createStatCard("Giá trị TB", "0 đ", "Trung bình mỗi hóa đơn", "#E67E22");

        box.getChildren().addAll(tongHoaDonCard, doanhThuCard, giaTriTBCard);
        return box;
    }

    private VBox createStatCard(String title, String value, String sub, String color) {
        VBox card = new VBox(10);
        card.setPrefWidth(220);
        card.setStyle("""
            -fx-background-color: white;
            -fx-padding: 15;
            -fx-background-radius: 12;
            -fx-border-color: #E9ECEF;
            -fx-border-radius: 12;
            -fx-border-width: 1;
        """);

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 13; -fx-text-fill: #95A5A6;");

        Label lblValue = new Label(value);
        lblValue.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        lblValue.setTextFill(Color.web(color));

        Label lblSub = new Label(sub);
        lblSub.setStyle("-fx-font-size: 12; -fx-text-fill: #95A5A6;");

        card.getChildren().addAll(lblTitle, lblValue, lblSub);
        return card;
    }

    private VBox createFilterSection() {
        VBox box = new VBox(18);
        box.setStyle("""
            -fx-background-color: white;
            -fx-padding: 25;
            -fx-background-radius: 12;
            -fx-border-color: #E9ECEF;
            -fx-border-radius: 12;
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);
        """);

        Label title = new Label("Bộ lọc và tìm kiếm");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Tìm theo số hóa đơn, SDT khách hàng...");
        txtSearch.setPrefWidth(300);
        grid.add(createFilterRow("Tìm kiếm", txtSearch), 0, 0, 2, 1);

        ComboBox<String> cbTime = new ComboBox<>();
        cbTime.getItems().addAll("Hôm nay", "7 ngày qua", "30 ngày qua", "Tất cả");
        cbTime.setValue("Hôm nay");
        cbTime.setPrefWidth(180);
        grid.add(createFilterRow("Thời gian", cbTime), 0, 1);

        ComboBox<String> cbPayment = new ComboBox<>();
        cbPayment.getItems().addAll("Tất cả", "Tiền mặt", "Chuyển khoản");
        cbPayment.setValue("Tất cả");
        cbPayment.setPrefWidth(180);
        grid.add(createFilterRow("Thanh toán", cbPayment), 1, 1);

        btnTimKiem = new Button("Tìm kiếm");
        btnTimKiem.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-padding: 10 30; -fx-font-size: 14;");

        HBox btnBox = new HBox(btnTimKiem);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.setPadding(new Insets(10, 0, 0, 0));

        box.getChildren().addAll(title, grid, btnBox);
        return box;
    }

    private VBox createFilterRow(String label, Node control) {
        VBox box = new VBox(5);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12; -fx-text-fill: #6C757D;");
        box.getChildren().addAll(lbl, control);
        return box;
    }

    private VBox createTableSection() {
        VBox box = new VBox(15);
        box.setStyle("""
            -fx-background-color: white;
            -fx-padding: 15;
            -fx-background-radius: 12;
            -fx-border-color: #E9ECEF;
            -fx-border-radius: 12;
            -fx-border-width: 1;
        """);

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Danh sách hóa đơn");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        countLabel = new Label("Hiển thị 0 hóa đơn");
        countLabel.setStyle("-fx-text-fill: #7F8C8D;");

        titleRow.getChildren().addAll(title, countLabel);

        tableHoaDon.getColumns().clear();

        TableColumn<HoaDon, String> colMaHD = new TableColumn<>("Số hóa đơn");
        colMaHD.setPrefWidth(120);
        colMaHD.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getMaHD()));

        TableColumn<HoaDon, String> colNgay = new TableColumn<>("Ngày lập");
        colNgay.setPrefWidth(100);
        colNgay.setCellValueFactory(c -> {
            LocalDate d = c.getValue().getNgayLap();
            return new ReadOnlyStringWrapper(
                    d != null ? d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : ""
            );
        });

        TableColumn<HoaDon, String> colNhanVien = new TableColumn<>("Nhân viên");
        colNhanVien.setPrefWidth(150);
        colNhanVien.setCellValueFactory(c -> {
            NhanVien nv = c.getValue().getNhanVien();
            return new ReadOnlyStringWrapper(nv != null ? nv.getTenNV() : "Không xác định");
        });

        TableColumn<HoaDon, String> colKhachHang = new TableColumn<>("Khách hàng");
        colKhachHang.setPrefWidth(180);
        colKhachHang.setCellValueFactory(c -> {
            KhachHang kh = c.getValue().getKhachHang();
            if (kh == null) return new ReadOnlyStringWrapper("Khách lẻ");
            return new ReadOnlyStringWrapper(kh.getTenKH() + " (" + kh.getSoDienThoai() + ")");
        });

        TableColumn<HoaDon, String> colPTTT = new TableColumn<>("Thanh toán");
        colPTTT.setPrefWidth(120);
        colPTTT.setCellValueFactory(c -> {
            PhuongThucThanhToan p = c.getValue().getPhuongThucThanhToan();
            return new ReadOnlyStringWrapper(p != null ? p.getDisplayName() : "");
        });

        TableColumn<HoaDon, String> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setPrefWidth(130);
        colTongTien.setStyle("-fx-alignment: CENTER-RIGHT;");
        colTongTien.setCellValueFactory(c -> {
            BigDecimal tong = c.getValue().getTongTien();
            String formatted = tong == null ? "0 đ" : String.format("%,.0f đ", tong);
            return new ReadOnlyStringWrapper(formatted);
        });

        TableColumn<HoaDon, Void> colAction = new TableColumn<>("Thao tác");

        colAction.setPrefWidth(120);
        colAction.setStyle("-fx-alignment: CENTER;");

        colAction.setCellFactory(col -> new TableCell<>() {

            private final Button btnChiTiet = createIconButton(
                    "/img/icon_ChiTiet.png", "Chi tiết"
            );
            private final Button btnMuaLai = createIconButton(
                    "/img/icon_MuaLai.png", "Mua lại đơn hàng"
            );
            private final Button btnTraHang = createIconButton(
                    "/img/icon_TraHang.png", "Trả hàng"
            );

            private final HBox box = new HBox(8, btnChiTiet, btnMuaLai, btnTraHang);

            {
                box.setAlignment(Pos.CENTER);

                btnChiTiet.setOnAction(e -> {
                    HoaDon hd = getTableView().getItems().get(getIndex());
                    if (chiTietListener != null && hd != null) {
                        chiTietListener.onClick(hd);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });
        tableHoaDon.getColumns().addAll(
                colMaHD, colNgay, colNhanVien, colKhachHang,
                colPTTT, colTongTien , colAction
        );

        VBox.setVgrow(tableHoaDon, Priority.ALWAYS);

        box.getChildren().addAll(titleRow, tableHoaDon);

        return box;
    }

    private Button createIconButton(String path, String tooltip) {
        Button btn = new Button();
        btn.setStyle("-fx-background-color: transparent;");
        btn.setTooltip(new Tooltip(tooltip));
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView iv = new ImageView(img);
            iv.setFitWidth(18);
            iv.setFitHeight(18);
            btn.setGraphic(iv);
        } catch (Exception e) {
            btn.setText(tooltip);
        }
        return btn;
    }
}
