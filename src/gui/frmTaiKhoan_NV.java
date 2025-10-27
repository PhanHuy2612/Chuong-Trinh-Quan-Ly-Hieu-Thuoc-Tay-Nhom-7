package gui;

import connectDB.ConnectDB;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import enums.PhanQuyen;
import enums.TrangThaiTaiKhoan;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import utils.JavaFXInitializer;

import javax.swing.*;

public class frmTaiKhoan_NV extends Application {

    private TaiKhoan_DAO taiKhoanDAO;
    private TaiKhoan currentUser;
    private TextField usernameField;
    private PasswordField currentPasswordField;
    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        // Khởi tạo DAO
        taiKhoanDAO = new TaiKhoan_DAO();

        // Lấy user từ ConnectDB thay vì SessionManager
        currentUser = ConnectDB.getInstance().getCurrentUser();

        if (currentUser == null) {
            showAlert("Lỗi", "Vui lòng đăng nhập!", Alert.AlertType.ERROR);
            primaryStage.close();
            openLoginScreen(primaryStage);
            return;
        }

        primaryStage.setTitle("Tài khoản - Hiệu thuốc Thiện Lương");

        BorderPane root = new BorderPane();
        root.setLeft(createLeftSidebar());

        VBox topAndCenter = new VBox();
        topAndCenter.getChildren().addAll(createTopHeader(), createMainContent());
        root.setCenter(topAndCenter);

        Scene scene = new Scene(root, 1400, 800);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLeftSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #007BFF; -fx-padding: 20;");
        sidebar.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Hiệu thuốc Thiện Lương");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-padding: 10 0 30 0;");
        sidebar.getChildren().add(title);

        String[] menuItems = {
                "Quản lý bán thuốc",
                "Quản lý khách hàng",
                "Thống kê",
                "Quản lý kho thuốc",
                "Quản lý hóa đơn",
                "Quản lý trả hàng",
                "Ca làm",
                "Tài khoản"
        };

        for (String item : menuItems) {
            Button btn = new Button(item);
            btn.setPrefWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.CENTER_LEFT);

            // Highlight menu Tài khoản
            if (item.equals("Tài khoản")) {
                btn.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-text-fill: white; -fx-padding: 12 10 12 20; -fx-font-size: 14; -fx-font-weight: bold;");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 12 10 12 20; -fx-font-size: 14;");
            }

            btn.setOnAction(e -> handleMenuClick(item, (Stage) btn.getScene().getWindow()));

            // Hover effect
            btn.setOnMouseEntered(e -> {
                if (!item.equals("Tài khoản")) {
                    btn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-padding: 12 10 12 20; -fx-font-size: 14;");
                }
            });
            btn.setOnMouseExited(e -> {
                if (!item.equals("Tài khoản")) {
                    btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 12 10 12 20; -fx-font-size: 14;");
                }
            });

            sidebar.getChildren().add(btn);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(spacer);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(255,255,255,0.3);");
        sidebar.getChildren().add(sep);

        HBox userBox = new HBox(10);
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setStyle("-fx-padding: 15 10;");

        Circle userCircle = new Circle(20, Color.WHITE);
        Label userLabel = new Label(currentUser.getTenDangNhap());
        userLabel.setTextFill(Color.WHITE);
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        userBox.getChildren().addAll(userCircle, userLabel);
        sidebar.getChildren().add(userBox);

        Button logoutBtn = new Button("Đăng xuất");
        logoutBtn.setPrefWidth(Double.MAX_VALUE);
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 12 10 12 20; -fx-font-size: 14;");
        logoutBtn.setOnAction(e -> handleLogout((Stage) logoutBtn.getScene().getWindow()));
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: rgba(255,0,0,0.3); -fx-text-fill: white; -fx-padding: 12 10 12 20; -fx-font-size: 14;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 12 10 12 20; -fx-font-size: 14;"));
        sidebar.getChildren().add(logoutBtn);

        return sidebar;
    }

    private void handleMenuClick(String menuItem, Stage currentStage) {
        switch (menuItem) {
            case "Quản lý bán thuốc":
                openBanThuocScreen(currentStage);
                break;
            case "Tài khoản":
                // Đã ở màn hình này
                break;
            default:
                showAlert("Thông báo", "Chức năng " + menuItem + " đang phát triển", Alert.AlertType.INFORMATION);
        }
    }

    private void openBanThuocScreen(Stage currentStage) {
        try {
            frmQLBanThuoc_NV banThuocScreen = new frmQLBanThuoc_NV();
            Stage newStage = new Stage();
            banThuocScreen.start(newStage);
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở màn hình bán thuốc: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void openLoginScreen(Stage currentStage) {
        try {
            SwingUtilities.invokeLater(() -> new frmDangNhap());
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở màn hình đăng nhập: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private HBox createTopHeader() {
        HBox header = new HBox(20);
        header.setPrefHeight(70);
        header.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 15 30;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label dateLabel = new Label("Thông tin tài khoản");
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        dateLabel.setTextFill(Color.web("#333333"));

        Circle userCircle = new Circle(20, Color.web("#007BFF"));
        Label userName = new Label(currentUser.getQuyenTruyCap() == PhanQuyen.DUOC_SI ? "Nhân viên bán hàng" : "Quản lý");
        userName.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        HBox userSection = new HBox(12, userCircle, userName);
        userSection.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(dateLabel, spacer, userSection);
        return header;
    }

    private VBox createMainContent() {
        VBox content = new VBox(30);
        content.setStyle("-fx-padding: 40; -fx-background-color: #FFFFFF;");
        content.setAlignment(Pos.TOP_CENTER);

        Label pageTitle = new Label("Thông tin tài khoản");
        pageTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        pageTitle.setTextFill(Color.web("#333333"));

        statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statusLabel.setVisible(false);

        VBox profileCard = new VBox(20);
        profileCard.setMaxWidth(900);
        profileCard.setStyle("-fx-background-color: white; -fx-padding: 40; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        HBox avatarSection = createAvatarSection();
        Separator separator1 = new Separator();
        separator1.setPrefWidth(900);

        GridPane infoGrid = createInfoGrid();
        Separator separator2 = new Separator();
        separator2.setPrefWidth(900);

        VBox passwordSection = createPasswordSection();
        Separator separator3 = new Separator();
        separator3.setPrefWidth(900);

        HBox actionButtons = createActionButtons();

        profileCard.getChildren().addAll(avatarSection, separator1, infoGrid, separator2, passwordSection, separator3, actionButtons);
        content.getChildren().addAll(pageTitle, statusLabel, profileCard);
        return content;
    }

    private HBox createAvatarSection() {
        HBox avatarSection = new HBox(30);
        avatarSection.setAlignment(Pos.CENTER_LEFT);

        VBox avatarBox = new VBox(10);
        avatarBox.setAlignment(Pos.CENTER);
        Circle avatar = new Circle(60, Color.web("#007BFF"));
        String initials = currentUser.getTenDangNhap().substring(0, Math.min(2, currentUser.getTenDangNhap().length())).toUpperCase();
        Label avatarLabel = new Label(initials);
        avatarLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        avatarLabel.setTextFill(Color.WHITE);
        StackPane avatarPane = new StackPane(avatar, avatarLabel);

        avatarBox.getChildren().add(avatarPane);

        VBox nameBox = new VBox(5);
        Label nameTitle = new Label(currentUser.getTenDangNhap());
        nameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        Label roleLabel = new Label(currentUser.getQuyenTruyCap() == PhanQuyen.DUOC_SI ? "Nhân viên bán hàng" : "Quản lý");
        roleLabel.setFont(Font.font("Arial", 16));
        roleLabel.setTextFill(Color.web("#6C757D"));

        Label statusBadge = new Label(currentUser.getTrangThai() == TrangThaiTaiKhoan.DANG_HOAT_DONG ? "● Đang hoạt động" : "● Đã khóa");
        statusBadge.setTextFill(currentUser.getTrangThai() == TrangThaiTaiKhoan.DANG_HOAT_DONG ? Color.GREEN : Color.RED);
        statusBadge.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        nameBox.getChildren().addAll(nameTitle, roleLabel, statusBadge);
        avatarSection.getChildren().addAll(avatarBox, nameBox);
        return avatarSection;
    }

    private GridPane createInfoGrid() {
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(30);
        infoGrid.setVgap(20);
        infoGrid.setPadding(new Insets(10, 0, 10, 0));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(80);
        infoGrid.getColumnConstraints().addAll(col1, col2);

        Label usernameLabel = new Label("Tên đăng nhập:");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        usernameField = new TextField(currentUser.getTenDangNhap());
        usernameField.setEditable(false);
        usernameField.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label permissionLabel = new Label("Phân quyền:");
        permissionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField permissionField = new TextField(currentUser.getQuyenTruyCap().toString());
        permissionField.setEditable(false);
        permissionField.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label statusLabelGrid = new Label("Trạng thái:");
        statusLabelGrid.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField statusField = new TextField(currentUser.getTrangThai().toString());
        statusField.setEditable(false);
        statusField.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 5; -fx-background-radius: 5;");

        infoGrid.addRow(0, usernameLabel, usernameField);
        infoGrid.addRow(1, permissionLabel, permissionField);
        infoGrid.addRow(2, statusLabelGrid, statusField);

        return infoGrid;
    }

    private VBox createPasswordSection() {
        VBox passwordSection = new VBox(15);
        passwordSection.setPadding(new Insets(10, 0, 10, 0));

        Label sectionTitle = new Label("Đổi mật khẩu");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#333333"));

        GridPane passwordGrid = new GridPane();
        passwordGrid.setHgap(30);
        passwordGrid.setVgap(15);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(80);
        passwordGrid.getColumnConstraints().addAll(col1, col2);

        Label currentPassLabel = new Label("Mật khẩu hiện tại:");
        currentPassLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Nhập mật khẩu hiện tại");
        currentPasswordField.setStyle("-fx-padding: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label newPassLabel = new Label("Mật khẩu mới:");
        newPassLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Nhập mật khẩu mới");
        newPasswordField.setStyle("-fx-padding: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label confirmPassLabel = new Label("Xác nhận mật khẩu:");
        confirmPassLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Nhập lại mật khẩu mới");
        confirmPasswordField.setStyle("-fx-padding: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 5; -fx-background-radius: 5;");

        passwordGrid.addRow(0, currentPassLabel, currentPasswordField);
        passwordGrid.addRow(1, newPassLabel, newPasswordField);
        passwordGrid.addRow(2, confirmPassLabel, confirmPasswordField);

        passwordSection.getChildren().addAll(sectionTitle, passwordGrid);
        return passwordSection;
    }

    private HBox createActionButtons() {
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPadding(new Insets(10, 0, 0, 0));

        Button changePasswordBtn = new Button("Đổi mật khẩu");
        changePasswordBtn.setPrefWidth(200);
        changePasswordBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand;");
        changePasswordBtn.setOnAction(e -> handleChangePassword());
        changePasswordBtn.setOnMouseEntered(e -> changePasswordBtn.setStyle("-fx-background-color: #E0A800; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand;"));
        changePasswordBtn.setOnMouseExited(e -> changePasswordBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand;"));

        Button refreshBtn = new Button("Làm mới");
        refreshBtn.setPrefWidth(200);
        refreshBtn.setStyle("-fx-background-color: #17A2B8; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand;");
        refreshBtn.setOnAction(e -> handleRefresh());
        refreshBtn.setOnMouseEntered(e -> refreshBtn.setStyle("-fx-background-color: #138496; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand;"));
        refreshBtn.setOnMouseExited(e -> refreshBtn.setStyle("-fx-background-color: #17A2B8; -fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold; -fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand;"));

        actionButtons.getChildren().addAll(changePasswordBtn, refreshBtn);
        return actionButtons;
    }

    private void handleChangePassword() {
        String currentPass = currentPasswordField.getText().trim();
        String newPass = newPasswordField.getText().trim();
        String confirmPass = confirmPasswordField.getText().trim();

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showStatus("Vui lòng điền đầy đủ thông tin!", false);
            return;
        }

        if (!currentPass.equals(currentUser.getMatKhau())) {
            showStatus("Mật khẩu hiện tại không đúng!", false);
            return;
        }

        if (newPass.length() < 6) {
            showStatus("Mật khẩu mới phải có ít nhất 6 ký tự!", false);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            showStatus("Mật khẩu xác nhận không khớp!", false);
            return;
        }

        if (newPass.equals(currentPass)) {
            showStatus("Mật khẩu mới phải khác mật khẩu cũ!", false);
            return;
        }

        boolean success = taiKhoanDAO.doiMatKhau(currentUser.getTenDangNhap(), newPass);

        if (success) {
            currentUser.setMatKhau(newPass);
            ConnectDB.getInstance().setCurrentUser(currentUser);
            showStatus("✓ Đổi mật khẩu thành công!", true);
            clearPasswordFields();
        } else {
            showStatus("✗ Đổi mật khẩu thất bại!", false);
        }
    }

    private void handleRefresh() {
        TaiKhoan refreshedUser = taiKhoanDAO.getTaiKhoanTheoTenDangNhap(currentUser.getTenDangNhap());
        if (refreshedUser != null) {
            currentUser = refreshedUser;
            ConnectDB.getInstance().setCurrentUser(refreshedUser);
            showStatus("✓ Làm mới thông tin thành công!", true);
            clearPasswordFields();
        } else {
            showStatus("✗ Không thể làm mới thông tin!", false);
        }
    }

    private void handleLogout(Stage currentStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Đăng xuất");
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            ConnectDB.getInstance().clearSession();
            openLoginScreen(currentStage);
        }
    }

    private void clearPasswordFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void showStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        statusLabel.setTextFill(isSuccess ? Color.GREEN : Color.RED);
        statusLabel.setVisible(true);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // ✅ Khởi tạo JavaFX trước
        JavaFXInitializer.initialize();

        // Chờ một chút để JavaFX khởi tạo xong
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        launch(args);
    }
}