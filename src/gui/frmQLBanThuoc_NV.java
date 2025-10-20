package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.HashMap;
import java.util.Map;

public class frmQLBanThuoc_NV extends Application {

    private Map<String, String> menuIcons = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Quản lý thuốc - Hiệu thuốc Thiện Lương");

        // Initialize icons
        initializeIcons();

        // Get screen size for full width
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        BorderPane root = new BorderPane();
        root.setPrefSize(screenWidth, screenHeight);

        // Left Sidebar
        VBox leftSidebar = createLeftSidebar();
        root.setLeft(leftSidebar);

        // Top Header
        HBox topHeader = createTopHeader();
        root.setTop(topHeader);

        // Main Content
        VBox mainContent = createMainContent();
        root.setCenter(mainContent);

        // Right Cart Sidebar
        VBox rightCart = createRightCart();
        root.setRight(rightCart);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Full screen width and height
        primaryStage.show();
    }

    private void initializeIcons() {
        menuIcons.put("Quản lý bán thuốc", "/img/icon_GioHang.png");
        menuIcons.put("Quản lý khách hàng", "/img/icon_KhachHang.png");
        menuIcons.put("Thống kê", "/img/icon_ThongKe.png");
        menuIcons.put("Quản lý kho thuốc", "/img/icon_KhoThuoc.png");
        menuIcons.put("Quản lý hóa đơn", "/img/icon_HoaDon.png");
        menuIcons.put("Quản lý trả hàng", "/img/icon_TraHang.png");
        menuIcons.put("Ca làm", "/img/icon_ThoiGian.png");
        menuIcons.put("Tài khoản", "/img/icon_ConNguoi.png");
        menuIcons.put("Đăng xuất", "/img/icon_LogOut.png");
    }

    private VBox createLeftSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #007BFF; -fx-padding: 20;");
        sidebar.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Hiệu thuốc Thiện Lương");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-padding: 10 0 20 0;");

        // Menu items
        String[] menuItems = {"Quản lý bán thuốc", "Quản lý khách hàng", "Thống kê", "Quản lý kho thuốc", "Quản lý hóa đơn", "Quản lý trả hàng", "Ca làm", "Tài khoản"};
        for (String item : menuItems) {
            Button btn = createMenuButton(item);
            sidebar.getChildren().add(btn);
        }

        // User info at bottom
        Separator sep = new Separator();
        sep.setPrefWidth(Double.MAX_VALUE);
        sidebar.getChildren().addAll(sep);

        ImageView userIcon = new ImageView();
        try {
            Image userImage = new Image(getClass().getResourceAsStream("/img/NhanVien.jpg"));
            userIcon.setImage(userImage);
            userIcon.setFitWidth(20);
            userIcon.setFitHeight(20);
        } catch (Exception e) {
            // Fallback to circle if image not found
            Circle fallback = new Circle(20);
            fallback.setFill(Color.WHITE);
            System.err.println("User image not found: /img/NhanVien.jpg");
        }
        HBox userBox = new HBox(10, userIcon, new Label("Phạm Văn Hùng"));
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setStyle("-fx-padding: 10; -fx-text-fill: white;");
        sidebar.getChildren().add(userBox);

        // Logout button
        Button logoutBtn = createMenuButton("Đăng xuất");
        sidebar.getChildren().add(logoutBtn);

        return sidebar;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(Double.MAX_VALUE);

        String iconPath = menuIcons.get(text);
        if (iconPath != null) {
            try {
                Image iconImage = new Image(getClass().getResourceAsStream(iconPath));
                ImageView iconView = new ImageView(iconImage);
                iconView.setFitWidth(20);
                iconView.setFitHeight(20);
                btn.setGraphic(iconView);
                btn.setContentDisplay(ContentDisplay.LEFT);
            } catch (Exception e) {
                // Fallback if image not found
                System.err.println("Icon not found: " + iconPath);
            }
        }

        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 10 10 30;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-padding: 10 10 10 30;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 10 10 30;"));

        return btn;
    }

    private HBox createTopHeader() {
        HBox header = new HBox(20);
        header.setPrefHeight(60);
        header.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 10 20;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label dateLabel = new Label("Thứ Hai, 20 tháng 10, 2025");
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextField search = new TextField();
        search.setPromptText("Tìm kiếm...");
        search.setPrefWidth(300);
        search.setStyle("-fx-background-radius: 20; -fx-padding: 10;");

        Button notificationBtn = new Button("🔔");
        notificationBtn.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 50%; -fx-pref-width: 40; -fx-pref-height: 40;");

        ImageView userCircle = new ImageView();
        try {
            Image userImage = new Image(getClass().getResourceAsStream("/img/NhanVien.jpg"));
            userCircle.setImage(userImage);
            userCircle.setFitWidth(25);
            userCircle.setFitHeight(25);
        } catch (Exception e) {
            // Fallback to circle if image not found
            Circle fallback = new Circle(25);
            fallback.setFill(Color.BLUE);
            System.err.println("User image not found: /img/NhanVien.jpg");
        }
        Label userName = new Label("Nhân viên bán hàng");
        userName.setStyle("-fx-font-weight: bold;");

        HBox userSection = new HBox(10, userCircle, userName);
        userSection.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(dateLabel, spacer, search, notificationBtn, userSection);
        return header;
    }

    private VBox createMainContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 20;");
        content.setAlignment(Pos.TOP_LEFT);

        // Search section
        HBox searchSection = new HBox(10);
        TextField medicineSearch = new TextField();
        medicineSearch.setPromptText("Tìm kiếm thuốc...");
        medicineSearch.setPrefWidth(400);
        Button filterBtn = new Button("Lọc");
        filterBtn.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 5;");
        searchSection.getChildren().addAll(medicineSearch, filterBtn);

        Label sectionTitle = new Label("Quản lý bán thuốc");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Medicines grid
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.TOP_LEFT);

        // Sample medicines from image
        String[][] medicines = {
                {"Paracetamol 500mg", "2.500 đ", "250 viên"},
                {"Vitamin C 1000mg", "15.000 đ", "1000mg"},
                {"Thuốc ho Bổ phế", "25.000 đ", "60 chai"},
                {"Thuốc kháng sinh Amoxicillin", "18.000 đ", "Kháng sinh"},
                {"Dầu xanh", "8.000 đ", "120 chai"},
                {"Đậu xanh", "N/A", "120 chai"}
        };

        int col = 0;
        int row = 0;
        for (String[] med : medicines) {
            VBox card = createMedicineCard(med[0], med[1], med[2]);
            grid.add(card, col, row);
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setPrefSize(600, 600);
        scrollPane.setFitToWidth(true);

        content.getChildren().addAll(sectionTitle, searchSection, scrollPane);
        return content;
    }

    private VBox createMedicineCard(String name, String price, String detail) {
        VBox card = new VBox(10);
        card.setPrefWidth(250);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 10; -fx-border-width: 1;");

        // Placeholder image
        Rectangle imgPlaceholder = new Rectangle(220, 150);
        imgPlaceholder.setFill(Color.LIGHTGRAY);
        imgPlaceholder.setStroke(Color.GRAY);
        Label imgLabel = new Label("Hình ảnh thuốc");
        imgLabel.setAlignment(Pos.CENTER);
        VBox imgBox = new VBox(imgPlaceholder, imgLabel);
        imgBox.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label detailLabel = new Label(detail);
        detailLabel.setStyle("-fx-font-size: 12; -fx-text-fill: gray;");

        Label priceLabel = new Label(price);
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        priceLabel.setTextFill(Color.BLUE);

        Button addBtn = new Button("+ Thêm");
        addBtn.setPrefWidth(Double.MAX_VALUE);
        addBtn.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8;");

        card.getChildren().addAll(imgBox, nameLabel, detailLabel, priceLabel, addBtn);
        return card;
    }

    private VBox createRightCart() {
        VBox cart = new VBox(20);
        cart.setPrefWidth(450);
        cart.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 20;");
        cart.setAlignment(Pos.TOP_LEFT);

        Label cartTitle = new Label("Giỏ hàng");
        cartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Customer information section
        VBox customerInfo = new VBox(10);
        customerInfo.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 10; -fx-border-width: 1;");
        Label infoTitle = new Label("Thông tin khách hàng");
        infoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        HBox typeBox = new HBox(10);
        typeBox.setAlignment(Pos.CENTER_LEFT);
        Label typeLabel = new Label("Loại khách hàng:");
        ComboBox<String> customerType = new ComboBox<>();
        customerType.getItems().addAll("Khách vãng lai", "Khách hàng VIP", "Khách hàng thường");
        customerType.setPrefWidth(180);
        typeBox.getChildren().addAll(typeLabel, customerType);

        VBox phoneBox = new VBox(5);
        phoneBox.setVisible(false);
        Label phoneLabel = new Label("Số điện thoại");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Nhập số điện thoại khách hàng");
        phoneBox.getChildren().addAll(phoneLabel, phoneField);

        customerType.setOnAction(e -> {
            if (customerType.getValue() != null && customerType.getValue().equals("Khách vãng lai")) {
                phoneBox.setVisible(false);
            } else {
                phoneBox.setVisible(true);
            }
        });

        customerInfo.getChildren().addAll(infoTitle, typeBox, phoneBox);

        // Cart items (empty initially)
        ListView<String> cartItems = new ListView<>();
        cartItems.getItems().addAll("Giỏ hàng trống");

        ScrollPane cartScroll = new ScrollPane(cartItems);
        cartScroll.setFitToWidth(true);
        VBox.setVgrow(cartScroll, Priority.ALWAYS);

        Label totalLabel = new Label("Tổng: 0 đ");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalLabel.setTextFill(Color.GREEN);

        Button checkoutBtn = new Button("Thanh toán");
        checkoutBtn.setPrefWidth(Double.MAX_VALUE);
        checkoutBtn.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 10;");

        Button clearBtn = new Button("Xóa giỏ hàng");
        clearBtn.setPrefWidth(Double.MAX_VALUE);
        clearBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 10;");
        clearBtn.setOnAction(e -> {
            cartItems.getItems().clear();
            cartItems.getItems().addAll("Giỏ hàng trống");
            totalLabel.setText("Tổng: 0 đ");
        });

        cart.getChildren().addAll(cartTitle, customerInfo, cartScroll, totalLabel, checkoutBtn, clearBtn);
        return cart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}