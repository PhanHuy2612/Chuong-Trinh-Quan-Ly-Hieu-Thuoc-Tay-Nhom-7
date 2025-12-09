package gui;

import connectDB.ConnectDB;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class frmDangNhap extends Application {

    private Map<String, String> icons = new HashMap<>();
    private TextField passwordField;
    private TextField usernameField;
    private Button eyeButton;
    private CheckBox rememberCheckBox;
    private Button forgotPasswordButton;
    private Button loginButton;
    private Button exitButton;
    private String usernamePlaceholder = "Nhập tên đăng nhập";
    private String passwordPlaceholder = "Nhập mật khẩu";
    private Stage primaryStage;
    private boolean isPasswordHidden = true;
    private HBox passwordRow;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;  // Assign to class field
        primaryStage.setTitle("Đăng nhập - Hệ thống Quản lý Bán Thuốc Nhà thuốc Thiện Lương");
        primaryStage.setResizable(false);
        primaryStage.setWidth(500);
        primaryStage.setHeight(700);
        primaryStage.centerOnScreen();

        // Kết nối DB
        try {
            ConnectDB.getInstance().connect();
            System.out.println("Kết nối DB thành công từ form đăng nhập.");
        } catch (SQLException ex) {
            System.err.println("Lỗi kết nối DB từ form đăng nhập: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Kết Nối");
            alert.setHeaderText(null);
            alert.setContentText("Không thể kết nối cơ sở dữ liệu: " + ex.getMessage());
            alert.showAndWait();
        }

        // Initialize icons
        initializeIcons();

        // Root layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F8FAFC;");

        // Right panel - Login form
        Pane rightPanel = createRightPanel();
        root.setCenter(rightPanel);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pane createRightPanel() {
        VBox mainPanel = new VBox();
        mainPanel.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 20;");
        mainPanel.setAlignment(Pos.CENTER);
        VBox.setVgrow(mainPanel, Priority.ALWAYS);

        // Top panel - Icon, title, subtitle
        VBox topPanel = new VBox(10);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setStyle("-fx-background-color: #ADD8E6; -fx-padding: 30 40 20 40; -fx-background-radius: 20;");
        topPanel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(topPanel, Priority.ALWAYS);

        // Icon
        ImageView iconView = new ImageView();
        try {
            Image icon = new Image(getClass().getResourceAsStream("/img/icon_Thuoc.png"));
            iconView.setImage(icon);
            iconView.setFitWidth(60);
            iconView.setFitHeight(60);
            iconView.setPreserveRatio(true);
            topPanel.getChildren().add(iconView);  // Move inside try
        } catch (Exception e) {
            Label fallbackIcon = new Label("💊");
            fallbackIcon.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            fallbackIcon.setTextFill(Color.web("#3B82F6"));
            topPanel.getChildren().add(fallbackIcon);
        }

        // Title
        Label titleLabel = new Label("Nhà thuốc Thiện Lương");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.BLACK);
        topPanel.getChildren().add(titleLabel);

        // Subtitle
        Label subtitleLabel = new Label("Hệ thống quản lý bán thuốc");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#6B7280"));
        topPanel.getChildren().add(subtitleLabel);

        mainPanel.getChildren().add(topPanel);

        // Spacer to push form to center vertically
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        mainPanel.getChildren().add(spacer);

        // Form panel - Rounded
        VBox formPanel = new VBox(15);
        formPanel.setStyle("-fx-background-color: white; -fx-padding: 30 40; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        formPanel.setMaxWidth(Double.MAX_VALUE);
        formPanel.setAlignment(Pos.CENTER_LEFT);

        // Description
        Label descriptionLabel = new Label("Nhập thông tin đăng nhập để truy cập hệ thống");
        descriptionLabel.setFont(Font.font("Arial", 14));
        descriptionLabel.setTextFill(Color.web("#6B7280"));
        formPanel.getChildren().add(descriptionLabel);

        // Username
        Label userLabel = new Label("Tên đăng nhập");
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        formPanel.getChildren().add(userLabel);

        usernameField = new TextField();
        usernameField.setPromptText(usernamePlaceholder);
        usernameField.setStyle(getUnderlineStyle());
        usernameField.setMaxWidth(Double.MAX_VALUE);
        formPanel.getChildren().add(usernameField);

        // Password
        Label passLabel = new Label("Mật khẩu");
        passLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        formPanel.getChildren().add(passLabel);

        passwordRow = new HBox();
        passwordRow.setAlignment(Pos.CENTER_LEFT);
        passwordRow.setSpacing(5);
        passwordRow.setMaxWidth(Double.MAX_VALUE);

        passwordField = new PasswordField();
        passwordField.setPromptText(passwordPlaceholder);
        passwordField.setStyle(getUnderlineStyle());
        HBox.setHgrow(passwordField, Priority.ALWAYS);
        passwordRow.getChildren().add(passwordField);

        // Eye button - Use emoji fallback only
        eyeButton = new Button();
        eyeButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        eyeButton.setText("👁");
        eyeButton.setFont(Font.font(16));
        eyeButton.setOnAction(e -> togglePasswordVisibility());
        passwordRow.getChildren().add(eyeButton);

        formPanel.getChildren().add(passwordRow);

        // Remember and forgot
        HBox bottomRow = new HBox(10);
        bottomRow.setAlignment(Pos.CENTER_LEFT);
        bottomRow.setMaxWidth(Double.MAX_VALUE);

        rememberCheckBox = new CheckBox("Nhớ mật khẩu");
        rememberCheckBox.setFont(Font.font("Arial", 12));
        bottomRow.getChildren().add(rememberCheckBox);

        forgotPasswordButton = new Button("Quên mật khẩu?");
        forgotPasswordButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #3B82F6; -fx-cursor: hand; -fx-underline: true;");
        forgotPasswordButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Quên mật khẩu");
            alert.setHeaderText(null);
            alert.setContentText("Chức năng quên mật khẩu sẽ được triển khai sau. Vui lòng liên hệ admin!");
            alert.showAndWait();
        });
        HBox.setHgrow(forgotPasswordButton, Priority.ALWAYS);
        bottomRow.getChildren().add(forgotPasswordButton);

        formPanel.getChildren().add(bottomRow);

        // Buttons
        HBox buttonRow = new HBox(20);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.setSpacing(20);
        buttonRow.setMaxWidth(Double.MAX_VALUE);

        loginButton = new Button("Đăng nhập");
        loginButton.setPrefSize(140, 50);
        loginButton.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16; -fx-background-radius: 25; -fx-cursor: hand;");
        loginButton.setOnAction(e -> handleLogin());
        buttonRow.getChildren().add(loginButton);

        exitButton = new Button("Thoát");
        exitButton.setPrefSize(140, 50);
        exitButton.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16; -fx-background-radius: 25; -fx-cursor: hand;");
        exitButton.setOnAction(e -> Platform.exit());
        buttonRow.getChildren().add(exitButton);

        formPanel.getChildren().add(buttonRow);

        mainPanel.getChildren().add(formPanel);

        // Another spacer for bottom
        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);
        mainPanel.getChildren().add(bottomSpacer);

        return mainPanel;
    }

    private String getUnderlineStyle() {
        return "-fx-background-color: transparent; -fx-padding: 10 10 5 10; -fx-border-width: 0 0 3 0; -fx-border-color: #B4B4B4; -fx-font-size: 14;";
    }

    private void togglePasswordVisibility() {
        String currentText = passwordField.getText();
        passwordRow.getChildren().remove(passwordField);  // Remove old field from UI

        if (isPasswordHidden) {
            // Switch to visible (TextField)
            passwordField = new TextField();
            passwordField.setText(currentText);
            passwordField.setPromptText(passwordPlaceholder);
            passwordField.setStyle(getUnderlineStyle());
            HBox.setHgrow(passwordField, Priority.ALWAYS);
            eyeButton.setText("🙈");
            eyeButton.setGraphic(null);
            isPasswordHidden = false;
        } else {
            // Switch to hidden (PasswordField)
            passwordField = new PasswordField();
            passwordField.setText(currentText);
            passwordField.setPromptText(passwordPlaceholder);
            passwordField.setStyle(getUnderlineStyle());
            HBox.setHgrow(passwordField, Priority.ALWAYS);
            eyeButton.setText("👁");
            eyeButton.setGraphic(null);
            isPasswordHidden = true;
        }

        passwordRow.getChildren().add(0, passwordField);  // Add new field to UI at index 0 to maintain order
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        if (username.equals(usernamePlaceholder)) username = "";
        String password = passwordField.getText().trim();
        if (password.equals(passwordPlaceholder)) password = "";

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập tên đăng nhập và mật khẩu!");
            alert.showAndWait();
            return;
        }

        try {
            TaiKhoan_DAO dao = new TaiKhoan_DAO();
            TaiKhoan tk = dao.dangNhap(username, password);

            if (tk != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setHeaderText(null);
                alert.setContentText("Đăng nhập thành công! Chào mừng " + tk.getTenDangNhap());
                alert.showAndWait();

                // Close current stage
                primaryStage.close();

                // Open main app
                String finalUsername = username;
                Platform.runLater(() -> {
                    try {
                        new frmQLBanThuoc_NV(finalUsername).start(new Stage());
                    } catch (Exception ex) {
                        // Handle exception from start() asynchronously
                        Platform.runLater(() -> {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Lỗi");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Lỗi khi mở ứng dụng chính: " + ex.getMessage());
                            errorAlert.showAndWait();
                        });
                    }
                });
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Tên đăng nhập hoặc mật khẩu không đúng!");
                alert.showAndWait();
                passwordField.clear();
            }
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Lỗi hệ thống: " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void initializeIcons() {
        // Icons loaded as needed in UI
    }

    @Override
    public void stop() throws Exception {
        ConnectDB.getInstance().disconnect();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}