package gui;

import dao.NhanVien_DAO;
import entity.TaiKhoan;
import enums.PhanQuyen;
import gui_dialog.frmTaiKhoan;
import gui_dialog_NV.HoaDon.HoaDonController;
import gui_dialog_NV.HoaDon.HoaDonView;
import gui_dialog_NV.KhachHang.KhachHangController;
import gui_dialog_NV.KhachHang.KhachHangView;
import gui_dialog_NV.banthuoc.BanThuocNV_Controller;
import gui_dialog_NV.banthuoc.BanThuocNV_View;
import gui_dialog_NV.KhoThuoc.controller.KhoThuocView;
import gui_dialog_QL.CaLam.CaLamController;
import gui_dialog_QL.CaLam.CaLamView;
import gui_dialog_QL.KhuyenMai.KhuyenMaiController;
import gui_dialog_QL.KhuyenMai.KhuyenMaiView;
import gui_dialog_NV.LichLam.LichLamView;
import gui_dialog_NV.LichLam.LichLamController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class mainLayout extends Application {

    private TaiKhoan taiKhoan;
    private Stage primaryStage;
    private BorderPane root;
    private StackPane contentArea;
    private SidebarMenu sidebarMenu;
    private SidebarMenu_QL sidebarQL;
    private HeaderBar headerBar;

    private final NhanVien_DAO nhanVienDAO = NhanVien_DAO.getInstance();
    private final Map<String, Runnable> menuActions = new HashMap<>();

    public mainLayout() {
        // Constructor mặc định cần thiết cho Application.launch()
    }

    public mainLayout(TaiKhoan tk) {
        this.taiKhoan = tk;
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Hệ Thống Quản Lý Hiệu Thuốc Thiện Lương");
        primaryStage.setMaximized(true);

        root = new BorderPane();
        root.setStyle("-fx-background-color: #F8FAFC;");

        initMenuActions();
        createSidebarAndHeader();

        contentArea = new StackPane();
        contentArea.setStyle("-fx-padding: 20 20 20 0;");
        root.setCenter(contentArea);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        openDefaultScreen();
    }

    private void initMenuActions() {
        menuActions.put("Quản lý bán thuốc", () -> {
            try {
                BanThuocNV_Controller controller = new BanThuocNV_Controller(taiKhoan.getTenDangNhap());
                BanThuocNV_View view = controller.getView();
                Node rootNode = view.getRoot();

                if (rootNode != null) {
                    loadScreen(rootNode);
                } else {
                    showPlaceholder("Không tải được giao diện bán thuốc");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showPlaceholder("Lỗi tải màn hình bán thuốc: " + e.getMessage());
            }
        });
        menuActions.put("Quản lý kho thuốc", () -> {
            try {
                KhoThuocView form = new KhoThuocView(taiKhoan.getTenDangNhap());
                loadScreen(form.getContentPane());
            } catch (Exception e) {
                e.printStackTrace();
                showPlaceholder("Lỗi tải kho thuốc");
            }
        });
        menuActions.put("Quản lý khách hàng", () -> {
            try {
                KhachHangView view = new KhachHangView();
                new KhachHangController(view);
                loadScreen(view.getRoot());
            } catch (Exception e) {
                e.printStackTrace();
                showPlaceholder("Lỗi tải quản lý khách hàng: " + e.getMessage());
            }
        });

        menuActions.put("Quản lý hóa đơn", () -> {
            try {
                HoaDonView view = new HoaDonView();
                Pane content = view.getContent();

                new HoaDonController(view);

                loadScreen(content);
            } catch (Exception e) {
                e.printStackTrace();
                showPlaceholder("Lỗi tải màn hình quản lý hóa đơn: " + e.getMessage());
            }
        });

        menuActions.put("Quản lý khuyến mãi", () -> {
            try {
                KhuyenMaiView view = new KhuyenMaiView();
                Pane rootPane = view.getRoot();
                new KhuyenMaiController(view);
                loadScreen(rootPane);
            } catch (Exception e) {
                e.printStackTrace();
                showPlaceholder("Lỗi tải quản lý khuyến mãi");
            }
        });

        menuActions.put("Ca làm việc", () -> showPlaceholder("Ca làm việc"));
        menuActions.put("Quản lý trả hàng", () -> showPlaceholder("Quản lý trả hàng"));
        menuActions.put("Quản lý nhân viên", () -> showPlaceholder("Quản lý nhân viên"));
        menuActions.put("Báo cáo doanh thu", () -> showPlaceholder("Báo cáo doanh thu"));

        menuActions.put("Tài khoản", () -> {
            frmTaiKhoan form = new frmTaiKhoan(taiKhoan.getTenDangNhap(), this::logout);
            loadScreen(form.getContentPane());
        });
    }

    private void createSidebarAndHeader() {

        String tenNhanVien = nhanVienDAO.getTenNhanVienBySoDienThoai(taiKhoan.getTenDangNhap());
        if (tenNhanVien == null || tenNhanVien.isEmpty()) {
            tenNhanVien = taiKhoan.getTenDangNhap();
        }

        String vaiTro = taiKhoan.getPhanQuyen().getMoTa();

        if (taiKhoan.getPhanQuyen() == PhanQuyen.QUAN_LY) {
            // === QUẢN LÝ ===
            sidebarQL = new SidebarMenu_QL(
                    taiKhoan.getTenDangNhap(),
                    tenNhanVien,
                    this::logout
            );

            sidebarQL.setMenuAction("Quản lý ca làm", () -> {
                // 🔹 MVC: View + Controller
                CaLamView view = new CaLamView();
                new CaLamController(view);
                loadScreen(view.getRoot());
            });

            sidebarQL.setMenuAction("Quản lý khuyến mãi", menuActions.get("Quản lý khuyến mãi"));
            sidebarQL.setMenuAction("Quản lý hóa đơn", menuActions.get("Quản lý hóa đơn"));
            sidebarQL.setMenuAction("Quản lý nhân viên", menuActions.get("Quản lý nhân viên"));
            sidebarQL.setMenuAction("Thống kê", menuActions.get("Báo cáo doanh thu"));
            sidebarQL.setMenuAction("Quản lý trả hàng", menuActions.get("Quản lý trả hàng"));
            sidebarQL.setMenuAction("Tài khoản", menuActions.get("Tài khoản"));

            sidebarQL.updateUserInfo(
                    taiKhoan.getTenDangNhap(),
                    tenNhanVien,
                    vaiTro
            );
            root.setLeft(sidebarQL);

        } else {
            sidebarMenu = new SidebarMenu(
                    taiKhoan.getTenDangNhap(),
                    tenNhanVien,
                    this::logout
            );

            if (taiKhoan.getPhanQuyen() == PhanQuyen.DUOC_SI) {
                sidebarMenu.setMenuAction("Quản lý bán thuốc", menuActions.get("Quản lý bán thuốc"));
                sidebarMenu.setMenuAction("Quản lý kho thuốc", menuActions.get("Quản lý kho thuốc"));
                sidebarMenu.setMenuAction("Quản lý hóa đơn", menuActions.get("Quản lý hóa đơn"));

                sidebarMenu.setMenuAction("Lịch làm", () -> {
                    LichLamView view = new LichLamView();
                    new LichLamController(
                            view,
                            taiKhoan.getTenDangNhap()
                    );
                    loadScreen(view.getRoot());
                });
                sidebarMenu.setMenuAction("Quản lý trả hàng", menuActions.get("Quản lý trả hàng"));
            }

            sidebarMenu.setMenuAction("Quản lý khách hàng", menuActions.get("Quản lý khách hàng"));
            sidebarMenu.setMenuAction("Tài khoản", menuActions.get("Tài khoản"));

            sidebarMenu.updateUserInfo(
                    taiKhoan.getTenDangNhap(),
                    tenNhanVien,
                    vaiTro
            );
            root.setLeft(sidebarMenu);
        }

        // === HEADER ===
        headerBar = new HeaderBar(tenNhanVien);
        HBox headerWrapper = new HBox(headerBar);
        HBox.setHgrow(headerBar, Priority.ALWAYS);
        root.setTop(headerWrapper);
    }


    private void loadScreen(Node screen) {
        if (screen == null) {
            showPlaceholder("Không thể tải màn hình (null)");
            return;
        }

        Platform.runLater(() -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(screen);
            StackPane.setAlignment(screen, Pos.TOP_LEFT);

            if (screen instanceof Region region) {
                region.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                VBox.setVgrow(screen, Priority.ALWAYS);
                HBox.setHgrow(screen, Priority.ALWAYS);
            }
        });
    }

    private void openDefaultScreen() {

        Runnable defaultAction;

        if (taiKhoan.getPhanQuyen() == PhanQuyen.QUAN_LY) {
            defaultAction = () -> {
                CaLamView view = new CaLamView();
                new CaLamController(view);
                loadScreen(view.getRoot());
            };
        } else {
            defaultAction = menuActions.get("Quản lý bán thuốc");
        }

        if (defaultAction != null) {
            defaultAction.run();
        } else {
            showPlaceholder("Chào mừng đến với hệ thống!");
        }
    }

    private void showPlaceholder(String msg) {
        VBox box = new VBox(20, new Label(msg));
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-font-size: 24px; -fx-text-fill: #95A5A6;");
        loadScreen(box);
    }

    /* ================= LOGOUT ================= */
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Đăng xuất");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            primaryStage.close();
            Platform.runLater(() -> {
                try {
                    new frmDangNhap().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}