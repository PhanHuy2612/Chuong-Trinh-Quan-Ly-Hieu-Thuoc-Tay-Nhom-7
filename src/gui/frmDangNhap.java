package gui;

import connectDB.ConnectDB;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import mail.EmailSender;

import java.util.Random;

public class frmDangNhap extends Application {

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
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Đăng nhập - Nhà thuốc Thiện Lương");
        primaryStage.setResizable(false);
        primaryStage.setWidth(500);
        primaryStage.setHeight(700);
        primaryStage.centerOnScreen();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F8FAFC;");
        root.setCenter(createRightPanel());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Pane createRightPanel() {
        VBox mainPanel = new VBox();
        mainPanel.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 20;");
        mainPanel.setAlignment(Pos.CENTER);

        VBox topPanel = new VBox(10);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setStyle("-fx-background-color: #ADD8E6; -fx-padding: 30 40 20 40; -fx-background-radius: 20;");

        ImageView iconView = new ImageView();
        try {
            Image icon = new Image(getClass().getResourceAsStream("/img/icon_Thuoc.png"));
            iconView.setImage(icon);
            iconView.setFitWidth(60);
            iconView.setFitHeight(60);
        } catch (Exception e) {
            Label fb = new Label("Pill");
            fb.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            fb.setTextFill(Color.web("#3B82F6"));
            topPanel.getChildren().add(fb);
        }
        topPanel.getChildren().addAll(iconView,
                createLabel("Nhà thuốc Thiện Lương", 28, true),
                createLabel("Hệ thống quản lý bán thuốc", 14, false));

        mainPanel.getChildren().add(topPanel);
        mainPanel.getChildren().add(createSpacer());

        VBox formPanel = new VBox(15);
        formPanel.setStyle("-fx-background-color: white; -fx-padding: 30 40; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        formPanel.setAlignment(Pos.CENTER_LEFT);

        formPanel.getChildren().add(createLabel("Nhập thông tin đăng nhập để truy cập hệ thống", 14, false));
        formPanel.getChildren().add(createLabel("Tên đăng nhập", 14, true));
        usernameField = new TextField();
        usernameField.setPromptText(usernamePlaceholder);
        usernameField.setStyle(getUnderlineStyle());
        formPanel.getChildren().add(usernameField);

        formPanel.getChildren().add(createLabel("Mật khẩu", 14, true));

        passwordRow = new HBox(8);
        passwordRow.setAlignment(Pos.CENTER_LEFT);

        passwordField = new PasswordField();
        passwordField.setPromptText(passwordPlaceholder);
        passwordField.setStyle(getUnderlineStyle());
        HBox.setHgrow(passwordField, Priority.ALWAYS);
        passwordRow.getChildren().add(passwordField);

        // Icon mắt mở/đóng
        ImageView eyeOpen = new ImageView(new Image(getClass().getResourceAsStream("/img/icon_MatMo.png")));
        ImageView eyeClosed = new ImageView(new Image(getClass().getResourceAsStream("/img/icon_MatDong.png")));
        eyeOpen.setFitWidth(22); eyeOpen.setFitHeight(22);
        eyeClosed.setFitWidth(22); eyeClosed.setFitHeight(22);

        eyeButton = new Button();
        eyeButton.setGraphic(eyeClosed);
        eyeButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        eyeButton.setOnAction(e -> togglePasswordVisibility(eyeOpen, eyeClosed));
        passwordRow.getChildren().add(eyeButton);

        formPanel.getChildren().add(passwordRow);

        HBox bottomRow = new HBox(10);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        rememberCheckBox = new CheckBox("Nhớ mật khẩu");
        bottomRow.getChildren().add(rememberCheckBox);

        forgotPasswordButton = new Button("Quên mật khẩu?");
        forgotPasswordButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #3B82F6; -fx-cursor: hand; -fx-underline: true;");
        forgotPasswordButton.setOnAction(e -> openQuenMatKhauDialog());
        HBox.setHgrow(forgotPasswordButton, Priority.ALWAYS);
        forgotPasswordButton.setAlignment(Pos.CENTER_RIGHT);
        bottomRow.getChildren().add(forgotPasswordButton);

        formPanel.getChildren().add(bottomRow);

        HBox buttonRow = new HBox(20);
        buttonRow.setAlignment(Pos.CENTER);
        loginButton = createButton("Đăng nhập", "#3B82F6");
        loginButton.setOnAction(e -> handleLogin());
        exitButton = createButton("Thoát", "#EF4444");
        exitButton.setOnAction(e -> Platform.exit());
        buttonRow.getChildren().addAll(loginButton, exitButton);
        formPanel.getChildren().add(buttonRow);

        mainPanel.getChildren().add(formPanel);
        mainPanel.getChildren().add(createSpacer());
        return mainPanel;
    }

    private Label createLabel(String text, int size, boolean bold) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, size));
        lbl.setTextFill(bold ? Color.BLACK : Color.web("#6B7280"));
        return lbl;
    }

    private Button createButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefSize(140, 50);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16; -fx-background-radius: 25; -fx-cursor: hand;");
        return btn;
    }

    private String getUnderlineStyle() {
        return "-fx-background-color: transparent; -fx-padding: 10 10 5 10; -fx-border-width: 0 0 3 0; -fx-border-color: #B4B4B4; -fx-font-size: 14;";
    }

    private Region createSpacer() {
        Region r = new Region();
        VBox.setVgrow(r, Priority.ALWAYS);
        return r;
    }

    private void togglePasswordVisibility(ImageView eyeOpen, ImageView eyeClosed) {
        String text = passwordField.getText();
        passwordRow.getChildren().remove(passwordField);

        if (isPasswordHidden) {
            TextField tf = new TextField(text);
            tf.setPromptText(passwordPlaceholder);
            tf.setStyle(getUnderlineStyle());
            HBox.setHgrow(tf, Priority.ALWAYS);
            passwordField = tf;
            eyeButton.setGraphic(eyeOpen);
            isPasswordHidden = false;
        } else {
            PasswordField pf = new PasswordField();
            pf.setText(text);
            pf.setPromptText(passwordPlaceholder);
            pf.setStyle(getUnderlineStyle());
            HBox.setHgrow(pf, Priority.ALWAYS);
            passwordField = pf;
            eyeButton.setGraphic(eyeClosed);
            isPasswordHidden = true;
        }
        passwordRow.getChildren().add(0, passwordField);
    }
    private void openQuenMatKhauDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Quên mật khẩu");
        dialog.initOwner(primaryStage);
        dialog.getDialogPane().setStyle("-fx-background-color: white; -fx-padding: 20;");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(12); grid.setAlignment(Pos.CENTER);

        TextField txtTenDN = new TextField(); txtTenDN.setPromptText("Tên đăng nhập");
        TextField txtEmail = new TextField(); txtEmail.setPromptText("Email đăng ký");
        TextField txtOTP = new TextField(); txtOTP.setPromptText("Nhập mã OTP");
        PasswordField txtMK1 = new PasswordField(); txtMK1.setPromptText("Mật khẩu mới");
        PasswordField txtMK2 = new PasswordField(); txtMK2.setPromptText("Xác nhận mật khẩu");

        Button btnGuiOTP = new Button("Gửi OTP");
        btnGuiOTP.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-weight: bold;");

        grid.add(new Label("Tên đăng nhập:"), 0, 0); grid.add(txtTenDN, 1, 0);
        grid.add(new Label("Email:"), 0, 1); grid.add(txtEmail, 1, 1);
        grid.add(btnGuiOTP, 1, 2);
        grid.add(new Label("Mã OTP:"), 0, 3); grid.add(txtOTP, 1, 3);
        grid.add(new Label("Mật khẩu mới:"), 0, 4); grid.add(txtMK1, 1, 4);
        grid.add(new Label("Xác nhận:"), 0, 5); grid.add(txtMK2, 1, 5);

        String[] otpHolder = {""};

        btnGuiOTP.setOnAction(e -> {
            String tenDN = txtTenDN.getText().trim();
            String email = txtEmail.getText().trim();

            TaiKhoan_DAO dao = new TaiKhoan_DAO();
            TaiKhoan tk = dao.timTheoTenDangNhap(tenDN);

            if (tk == null || !email.equalsIgnoreCase(tk.getEmail())) {
                showErrorAlert("Tên đăng nhập hoặc email không đúng!");
                return;
            }

            otpHolder[0] = String.format("%06d", new Random().nextInt(999999));
            if (EmailSender.sendOTP(email, otpHolder[0])) {
                new Alert(Alert.AlertType.INFORMATION, "Đã gửi OTP đến email!", ButtonType.OK).showAndWait();
            } else {
                showErrorAlert("Gửi OTP thất bại! Kiểm tra mạng hoặc App Password.");
            }
        });

        ButtonType btnDoi = new ButtonType("Đổi mật khẩu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnDoi, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(b -> {
            if (b == btnDoi) {
                if (!txtOTP.getText().equals(otpHolder[0])) {
                    showErrorAlert("Mã OTP không đúng!");
                    return null;
                }
                if (!txtMK1.getText().equals(txtMK2.getText())) {
                    showErrorAlert("Mật khẩu xác nhận không khớp!");
                    return null;
                }
                if (txtMK1.getText().length() < 4) {
                    showErrorAlert("Mật khẩu phải ít nhất 4 ký tự!");
                    return null;
                }

                TaiKhoan_DAO dao = new TaiKhoan_DAO();
                if (dao.doiMatKhau(txtTenDN.getText().trim(), txtMK1.getText())) {
                    new Alert(Alert.AlertType.INFORMATION, "Đổi mật khẩu thành công!", ButtonType.OK).showAndWait();
                } else {
                    showErrorAlert("Đổi mật khẩu thất bại!");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    // ==================== ĐĂNG NHẬP ====================
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField instanceof PasswordField ? passwordField.getText() : ((TextField) passwordField).getText();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        TaiKhoan_DAO dao = new TaiKhoan_DAO();
        TaiKhoan tk = dao.dangNhap(username, password);

        if (tk != null && tk.getTrangThai() == enums.TrangThaiTaiKhoan.DANGHOATDONG) {
            //new Alert(Alert.AlertType.INFORMATION, "Đăng nhập thành công! Chào " + tk.getTenDangNhap(), ButtonType.OK).showAndWait();
            primaryStage.close();
            Platform.runLater(() -> {
                try {
                    new mainLayout(tk).start(new Stage());
                } catch (Exception ex) {
                    showErrorAlert("Lỗi mở giao diện chính: " + ex.getMessage());
                }
            });
        } else {
            showErrorAlert("Sai tài khoản/mật khẩu hoặc tài khoản bị khóa!");
        }
    }

    private void showErrorAlert(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
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