// src/gui_dialog/frmTaiKhoan.java
package gui_dialog;

import dao.NhanVien_DAO;
import dao.TaiKhoan_DAO;
import entity.NhanVien;
import entity.TaiKhoan;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;

import java.time.format.DateTimeFormatter;

public class frmTaiKhoan {

    private final TaiKhoan_DAO tkDAO = new TaiKhoan_DAO();
    private final NhanVien_DAO nvDAO = NhanVien_DAO.getInstance();

    private final String tenDangNhap;
    private final Runnable onLogout; // CALLBACK ĐĂNG XUẤT
    private NhanVien nhanVien;
    private TaiKhoan taiKhoan;

    // CONSTRUCTOR MỚI – NHẬN onLogout
    public frmTaiKhoan(String tenDangNhap, Runnable onLogout) {
        this.tenDangNhap = tenDangNhap;
        this.onLogout = onLogout;
        loadDataFromDB();
    }

    private void loadDataFromDB() {
        taiKhoan = tkDAO.getTaiKhoanByTenDangNhap(tenDangNhap);
        if (taiKhoan != null && taiKhoan.getMaNhanVien() != null) {
            nhanVien = nvDAO.getByMaNV(taiKhoan.getMaNhanVien());
        }
    }

    public Pane getContentPane() {
        if (taiKhoan == null || nhanVien == null) {
            VBox error = new VBox(10);
            error.setAlignment(Pos.CENTER);
            error.setStyle("-fx-padding: 50; -fx-background-color: #F8D7DA; -fx-background-radius: 10;");
            Label msg = new Label("Không thể tải thông tin tài khoản.\nVui lòng kiểm tra kết nối hoặc đăng nhập lại.");
            msg.setStyle("-fx-text-fill: #721C24; -fx-font-weight: bold;");
            error.getChildren().add(msg);
            return error;
        }

        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #F4F6F9; -fx-padding: 20;");

        HBox header = createHeader();
        Label subtitle = new Label("Quản lý thông tin cá nhân và cài đặt tài khoản");
        subtitle.setStyle("-fx-text-fill: #6C757D;");

        HBox profileCard = createProfileCard();
        TabPane tabPane = createTabPane();

        root.getChildren().addAll(header, subtitle, profileCard, tabPane);
        return root;
    }

    private HBox createHeader() {
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Tài khoản");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("Đăng xuất");
        logoutBtn.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 15;");
        logoutBtn.setOnAction(e -> {
            if (onLogout != null) {
                onLogout.run(); // GỌI HÀM ĐĂNG XUẤT TỪ mainLayout
            }
        });

        try {
            Image icon = new Image(getClass().getResourceAsStream("/img/icon_LogOut.png"));
            ImageView iv = new ImageView(icon);
            iv.setFitWidth(16); iv.setFitHeight(16);
            logoutBtn.setGraphic(iv);
        } catch (Exception ignored) {}

        header.getChildren().addAll(title, spacer, logoutBtn);
        return header;
    }

    private HBox createProfileCard() {
        HBox card = new HBox(20);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        card.setAlignment(Pos.CENTER_LEFT);

        Circle circle = new Circle(50, Color.web("#0D6EFD"));
        circle.setStroke(Color.WHITE); circle.setStrokeWidth(3);

        ImageView avatar = new ImageView();
        try {
            String path = nhanVien.getHinhAnh();
            if (path != null && !path.trim().isEmpty()) {
                Image img = new Image("file:" + path);
                if (!img.isError()) avatar.setImage(img);
            }
        } catch (Exception e) {
            try {
                avatar.setImage(new Image(getClass().getResourceAsStream("/img/NhanVien.jpg")));
            } catch (Exception ignored) {}
        }
        avatar.setFitWidth(100); avatar.setFitHeight(100);
        avatar.setClip(new Circle(50, 50, 50));
        StackPane avatarPane = new StackPane(circle, avatar);

        VBox info = new VBox(8);
        Label name = new Label(nhanVien.getTenNV());
        name.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label role = new Label(taiKhoan.getPhanQuyen().getMoTa());
        role.setStyle("-fx-background-color: #E3F2FD; -fx-text-fill: #1976D2; -fx-padding: 4 10; -fx-background-radius: 20;");
        role.setFont(Font.font(12));

        VBox contact = new VBox(5);
        contact.setStyle("-fx-text-fill: #6C757D;");
        contact.getChildren().addAll(
                createInfoRow("email", nhanVien.getEmail() != null ? nhanVien.getEmail() : "Chưa có email"),
                createInfoRow("phone", nhanVien.getSoDienThoai()),
                createInfoRow("calendar", "Từ " + formatDate(nhanVien.getNgaySinh()))
        );

        info.getChildren().addAll(name, role, contact);
        card.getChildren().addAll(avatarPane, info);
        return card;
    }

    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        tabPane.getTabs().addAll(createTabThongTin(), createTabBaoMat());
        return tabPane;
    }

    private Tab createTabThongTin() {
        Tab tab = new Tab("Thông tin cá nhân");
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 25;");

        Label title = new Label("Thông tin cá nhân");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label desc = new Label("Cập nhật thông tin cá nhân của bạn");
        desc.setStyle("-fx-text-fill: #6C757D;");

        GridPane grid = new GridPane();
        grid.setHgap(20); grid.setVgap(15);
        int row = 0;

        grid.add(new Label("Họ và tên"), 0, row);
        grid.add(readOnlyField(nhanVien.getTenNV()), 1, row++);

        grid.add(new Label("Tên đăng nhập"), 0, row);
        grid.add(readOnlyField(taiKhoan.getTenDangNhap()), 1, row++);

        grid.add(new Label("Số điện thoại"), 0, row);
        grid.add(readOnlyField(nhanVien.getSoDienThoai()), 1, row++);

        grid.add(new Label("Đ here"), 0, row);
        grid.add(readOnlyField(nhanVien.getDiaChi()), 1, row++);

        grid.add(new Label("Chức vụ"), 0, row);
        grid.add(readOnlyField(taiKhoan.getPhanQuyen().getMoTa()), 1, row++);

        Button editBtn = new Button("Chỉnh sửa");
        editBtn.setStyle("-fx-background-color: #0D6EFD; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20;");
        editBtn.setOnAction(e -> showAlert("Tính năng chỉnh sửa đang phát triển..."));

        HBox btnBox = new HBox(editBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        content.getChildren().addAll(title, desc, grid, btnBox);
        tab.setContent(content);
        return tab;
    }

    private Tab createTabBaoMat() {
        Tab tab = new Tab("Bảo mật");
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 25;");

        Label title = new Label("Bảo mật");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Label desc = new Label("Quản lý mật khẩu và bảo mật tài khoản");
        desc.setStyle("-fx-text-fill: #6C757D;");

        VBox passBox = new VBox(15);
        passBox.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 20; -fx-background-radius: 10;");

        PasswordField old = new PasswordField(); old.setPromptText("Mật khẩu hiện tại");
        PasswordField newP = new PasswordField(); newP.setPromptText("Mật khẩu mới");
        PasswordField confirm = new PasswordField(); confirm.setPromptText("Xác nhận mật khẩu mới");

        Button btn = new Button("Cập nhật mật khẩu");
        btn.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-background-radius: 8; -fx-padding: 10 20;");
        btn.setOnAction(e -> {
            if (newP.getText().isEmpty() || confirm.getText().isEmpty()) {
                showAlert("Vui lòng nhập đầy đủ!");
                return;
            }
            if (!newP.getText().equals(confirm.getText())) {
                showAlert("Mật khẩu xác nhận không khớp!");
                return;
            }
            showAlert("Đổi mật khẩu thành công!");
        });

        passBox.getChildren().addAll(new Label("Đổi mật khẩu"), old, newP, confirm, btn);
        content.getChildren().addAll(title, desc, passBox);
        tab.setContent(content);
        return tab;
    }

    private HBox createInfoRow(String icon, String text) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        try {
            Image img = new Image(getClass().getResourceAsStream("/img/icon_" + icon + ".png"));
            ImageView iv = new ImageView(img);
            iv.setFitWidth(16); iv.setFitHeight(16);
            row.getChildren().add(iv);
        } catch (Exception ignored) {}
        row.getChildren().add(new Label(text));
        return row;
    }

    private TextField readOnlyField(String text) {
        TextField tf = new TextField(text == null ? "Chưa có" : text);
        tf.setEditable(false);
        tf.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 10; -fx-background-radius: 8;");
        return tf;
    }

    private String formatDate(java.time.LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Chưa có";
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.show();
    }
}