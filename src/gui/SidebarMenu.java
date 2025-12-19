// src/gui/SidebarMenu.java
package gui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.Map;

public class SidebarMenu extends VBox {

    private final Map<String, String> menuIcons = new HashMap<>();
    private final Map<String, Runnable> menuActions = new HashMap<>();
    private Label userLabel;
    private Label roleLabel;

    public SidebarMenu(String tenDangNhap, String tenNhanVien, Runnable onLogout) {
        setPrefWidth(230);
        setStyle("-fx-background-color: #007BFF; -fx-padding: 20;");
        setAlignment(Pos.TOP_LEFT);

        initializeIcons();
        buildUI(tenDangNhap, tenNhanVien, onLogout);
    }

    private void initializeIcons() {
        menuIcons.put("Quản lý bán thuốc", "/img/icon_GioHang.png");
        menuIcons.put("Quản lý khách hàng", "/img/icon_KhachHang.png");
        menuIcons.put("Thống kê", "/img/icon_ThongKe.png");
        menuIcons.put("Quản lý kho thuốc", "/img/icon_KhoThuoc.png");
        menuIcons.put("Quản lý hóa đơn", "/img/icon_HoaDon.png");
        menuIcons.put("Lịch làm", "/img/icon_ThoiGian.png");
        menuIcons.put("Tài khoản", "/img/icon_ConNguoi.png");
        menuIcons.put("Đăng xuất", "/img/icon_LogOut.png");
    }

    private void buildUI(String tenDangNhap, String tenNhanVien, Runnable onLogout) {
        Label title = new Label("Hiệu thuốc Thiện Lương");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-padding: 10 0 20 0;");
        getChildren().add(title);

        String[] menuItems = {
                "Quản lý bán thuốc",
                "Quản lý khách hàng",
                "Thống kê",
                "Quản lý kho thuốc",
                "Quản lý hóa đơn",
                "Lịch làm",
                "Tài khoản"
        };

        for (String item : menuItems) {
            Button btn = createMenuButton(item);
            getChildren().add(btn);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        getChildren().add(spacer);

        Separator sep = new Separator();
        sep.setPrefWidth(Double.MAX_VALUE);
        getChildren().add(sep);

        // Thông tin người dùng
        try {
            Image userImage = new Image(getClass().getResourceAsStream("/img/NhanVien.jpg"));
            ImageView iconView = new ImageView(userImage);
            iconView.setFitWidth(20);
            iconView.setFitHeight(20);
            HBox userBox = new HBox(10, iconView, new Label(tenNhanVien));
            userBox.setStyle("-fx-text-fill: white; -fx-padding: 10;");
            getChildren().add(userBox);
        } catch (Exception e) {
            Label fallback = new Label(tenNhanVien);
            fallback.setTextFill(Color.WHITE);
            getChildren().add(fallback);
        }

        userLabel = new Label(tenDangNhap);
        userLabel.setTextFill(Color.YELLOW);
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        getChildren().add(userLabel);

        roleLabel = new Label("Dược sĩ");
        roleLabel.setTextFill(Color.LIGHTGREEN);
        getChildren().add(roleLabel);

        // Nút đăng xuất
        Button logoutBtn = createMenuButton("Đăng xuất");
        logoutBtn.setOnAction(e -> onLogout.run());
        getChildren().add(logoutBtn);
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(Double.MAX_VALUE);
        String iconPath = menuIcons.get(text);

        if (iconPath != null) {
            try {
                Image icon = new Image(getClass().getResourceAsStream(iconPath));
                ImageView iv = new ImageView(icon);
                iv.setFitWidth(20);
                iv.setFitHeight(20);
                btn.setGraphic(iv);
                btn.setContentDisplay(ContentDisplay.LEFT);
            } catch (Exception e) {
                System.err.println("Icon not found: " + iconPath);
            }
        }

        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 10 10 30;");
        btn.setOnMouseEntered(e ->
                btn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-padding: 10 10 10 30;"));
        btn.setOnMouseExited(e ->
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 10 10 30;"));

        Runnable action = menuActions.get(text);
        if (action != null) {
            btn.setOnAction(e -> action.run());
        }

        return btn;
    }

    public void setMenuAction(String menuText, Runnable action) {
        menuActions.put(menuText, action);

        for (javafx.scene.Node node : getChildren()) {
            if (node instanceof Button btn && menuText.equals(btn.getText())) {
                btn.setOnAction(e -> action.run());
                System.out.println("[DEBUG] Đã gán action cho menu: " + menuText);
                break;
            }
        }
    }

    public void updateUserInfo(String tenDangNhap, String tenNhanVien, String vaiTro) {
        userLabel.setText(tenDangNhap);
        roleLabel.setText(vaiTro);
    }
}