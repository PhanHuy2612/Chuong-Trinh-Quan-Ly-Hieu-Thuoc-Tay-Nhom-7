package gui_dialog_NV.banthuoc;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BanThuocNV_View {

    private final HBox root = new HBox(20);

    private TextField medicineSearch;
    private ComboBox<String> cbLoaiThuoc;
    private ComboBox<String> cbNhaCungCap;
    private ComboBox<String> cbKho;
    private CheckBox chkConHang;
    private CheckBox chkSapHet;
    private VBox medicineListContainer;
    private Button refreshButton;

    private VBox cartItemsContainer;
    private Label tongCongLabel;
    private ComboBox<String> customerTypeCombo;
    private TextField phoneField;
    private Button checkoutButton;
    private Button clearCartButton;

    public BanThuocNV_View() {
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);

        VBox left = createLeftPanel();
        root.getChildren().addAll(left, new Separator(Orientation.VERTICAL), createRightCart());
        HBox.setHgrow(left, Priority.ALWAYS);
    }

    private VBox createLeftPanel() {
        VBox left = new VBox(15);
        left.setPadding(new Insets(10));
        left.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 12;");

        Label title = new Label("Bán thuốc");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));

        /* ===== SEARCH ===== */
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        Label lblSearch = new Label("Tìm thuốc:");
        lblSearch.setFont(Font.font(16));

        medicineSearch = new TextField();
        medicineSearch.setPromptText("Nhập tên thuốc...");
        medicineSearch.setPrefWidth(500);
        medicineSearch.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-padding: 12px;" +
                        "-fx-background-radius: 30px;" +
                        "-fx-border-radius: 30px;"
        );

        Button btnClear = new Button("Xóa");
        btnClear.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20px;"
        );
        btnClear.setOnAction(e -> medicineSearch.clear());

        refreshButton = new Button("Làm mới");
        refreshButton.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20px;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonGroup = new HBox(10, btnClear, refreshButton);
        searchBox.getChildren().addAll(lblSearch, medicineSearch, spacer, buttonGroup);

        GridPane filterGrid = new GridPane();
        filterGrid.setHgap(15);
        filterGrid.setVgap(10);

        cbLoaiThuoc = new ComboBox<>();
        cbLoaiThuoc.setPromptText("Loại thuốc");
        cbLoaiThuoc.setPrefWidth(220);

        cbNhaCungCap = new ComboBox<>();
        cbNhaCungCap.setPromptText("Nhà cung cấp");
        cbNhaCungCap.setPrefWidth(220);

        cbKho = new ComboBox<>();
        cbKho.setPromptText("Kho thuốc");
        cbKho.setPrefWidth(180);

        chkConHang = new CheckBox("Chỉ hiển thị còn hàng");
        chkSapHet = new CheckBox("Chỉ hiển thị sắp hết");

        HBox checkRow = new HBox(30, chkConHang, chkSapHet);
        checkRow.setAlignment(Pos.CENTER_LEFT);

        filterGrid.addRow(
                0,
                new Label("Loại:"), cbLoaiThuoc,
                new Label("NCC:"), cbNhaCungCap,
                new Label("Kho:"), cbKho
        );
        filterGrid.add(checkRow, 0, 1, 6, 1);

        medicineListContainer = new VBox(10);
        medicineListContainer.setStyle("-fx-padding: 15;");

        ScrollPane scrollMedicine = new ScrollPane(medicineListContainer);
        scrollMedicine.setFitToWidth(true);
        scrollMedicine.setStyle("-fx-background: white;");
        VBox.setVgrow(scrollMedicine, Priority.ALWAYS);

        updateMedicineListPlaceholder();

        left.getChildren().addAll(title, searchBox, filterGrid, scrollMedicine);
        return left;
    }

    private VBox createRightCart() {
        VBox cart = new VBox(15);
        cart.setPrefWidth(420);
        cart.setStyle("-fx-background-color: #f5f6fa; -fx-padding: 20; -fx-background-radius: 12;");

        Label cartTitle = new Label("Giỏ hàng");
        cartTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        cartTitle.setTextFill(Color.web("#27ae60"));

        VBox infoBox = new VBox(10);
        infoBox.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10;");

        customerTypeCombo = new ComboBox<>();
        customerTypeCombo.getItems().addAll("Khách vãng lai", "Thành viên");
        customerTypeCombo.setValue("Khách vãng lai");

        phoneField = new TextField();
        phoneField.setPromptText("Số điện thoại");
        phoneField.setVisible(false);

        customerTypeCombo.valueProperty().addListener((o, old, nev) ->
                phoneField.setVisible(!"Khách vãng lai".equals(nev))
        );

        infoBox.getChildren().addAll(
                new Label("Loại khách:"), customerTypeCombo,
                new Label("SĐT:"), phoneField
        );

        cartItemsContainer = new VBox(8);
        ScrollPane scrollCart = new ScrollPane(cartItemsContainer);
        scrollCart.setFitToWidth(true);
        VBox.setVgrow(scrollCart, Priority.ALWAYS);

        tongCongLabel = new Label("Tổng cộng: 0 đ");
        tongCongLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
        tongCongLabel.setTextFill(Color.web("#e74c3c"));

        checkoutButton = new Button("Thanh toán");
        checkoutButton.setStyle(
                "-fx-background-color: #27ae60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-padding: 15px;" +
                        "-fx-background-radius: 12px;"
        );
        checkoutButton.setMaxWidth(Double.MAX_VALUE);

        clearCartButton = new Button("Làm mới giỏ");
        clearCartButton.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 18px;" +
                        "-fx-padding: 15px;" +
                        "-fx-background-radius: 12px;"
        );
        clearCartButton.setMaxWidth(Double.MAX_VALUE);

        cart.getChildren().addAll(
                cartTitle, infoBox, scrollCart,
                tongCongLabel, checkoutButton, clearCartButton
        );
        return cart;
    }

    public void updateMedicineListPlaceholder() {
        medicineListContainer.getChildren().clear();
        Label hint = new Label("Nhập tên thuốc hoặc dùng bộ lọc để hiển thị danh sách");
        hint.setFont(Font.font(18));
        hint.setTextFill(Color.GRAY);
        hint.setWrapText(true);
        hint.setAlignment(Pos.CENTER);
        hint.setPadding(new Insets(80, 20, 80, 20));
        medicineListContainer.getChildren().add(hint);
    }

    public HBox getRoot() { return root; }
    public TextField getMedicineSearch() { return medicineSearch; }
    public ComboBox<String> getCbLoaiThuoc() { return cbLoaiThuoc; }
    public ComboBox<String> getCbNhaCungCap() { return cbNhaCungCap; }
    public ComboBox<String> getCbKho() { return cbKho; }
    public CheckBox getChkConHang() { return chkConHang; }
    public CheckBox getChkSapHet() { return chkSapHet; }
    public VBox getMedicineListContainer() { return medicineListContainer; }
    public Button getRefreshButton() { return refreshButton; }

    public VBox getCartItemsContainer() { return cartItemsContainer; }
    public Label getTongCongLabel() { return tongCongLabel; }
    public ComboBox<String> getCustomerTypeCombo() { return customerTypeCombo; }
    public TextField getPhoneField() { return phoneField; }
    public Button getCheckoutButton() { return checkoutButton; }
    public Button getClearCartButton() { return clearCartButton; }
}