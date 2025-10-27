package gui;

import connectDB.ConnectDB;
import dao.NhanVien_DAO;
import entity.NhanVien;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

public class frmQLNV_QL extends Application {

    // Khai báo các components
    private TextField txtTimKiem;
    private TextField txtMaNV;
    private TextField txtTenNV;
    private TextField txtSoDienThoai;
    private DatePicker dpNgaySinh;
    private ComboBox<String> cbGioiTinh;
    private TableView<NhanVien> tableView;
    private ObservableList<NhanVien> nhanVienList;
    private NhanVien_DAO nhanVienDAO;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Quản lý nhân viên");

        // QUAN TRỌNG: Kết nối database trước
        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            showAlert("Lỗi kết nối", "Không thể kết nối đến database: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        // Khởi tạo DAO
        nhanVienDAO = new NhanVien_DAO();

        // Tạo giao diện
        BorderPane root = new BorderPane();
        root.setTop(createTopSection());
        root.setCenter(createCenterSection());
        root.setRight(createRightSection());

        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load dữ liệu sau khi giao diện đã hiển thị
        loadNhanVienData();

    }

    private VBox createTopSection() {
        VBox topBox = new VBox(10);
        topBox.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 20;");

        Label title = new Label("Quản lý nhân viên");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        txtTimKiem = new TextField();
        txtTimKiem.setPromptText("Tìm kiếm theo tên hoặc số điện thoại...");
        txtTimKiem.setPrefWidth(400);

        Button btnTimKiem = new Button("Tìm kiếm");
        btnTimKiem.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white;");
        btnTimKiem.setOnAction(e -> timKiemNhanVien());

        searchBox.getChildren().addAll(txtTimKiem, btnTimKiem);
        topBox.getChildren().addAll(title, searchBox);

        return topBox;
    }

    private VBox createCenterSection() {
        VBox centerBox = new VBox(10);
        centerBox.setStyle("-fx-padding: 20;");

        // Tạo bảng
        tableView = new TableView<>();
        tableView.setPrefHeight(500);

        // Định nghĩa các cột
        TableColumn<NhanVien, String> colMaNV = new TableColumn<>("Mã NV");
        colMaNV.setCellValueFactory(new PropertyValueFactory<>("maNV"));
        colMaNV.setPrefWidth(100);

        TableColumn<NhanVien, String> colTenNV = new TableColumn<>("Tên nhân viên");
        colTenNV.setCellValueFactory(new PropertyValueFactory<>("tenNV"));
        colTenNV.setPrefWidth(200);

        TableColumn<NhanVien, Boolean> colGioiTinh = new TableColumn<>("Giới tính");
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colGioiTinh.setPrefWidth(100);
        colGioiTinh.setCellFactory(column -> new TableCell<NhanVien, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Nam" : "Nữ");
                }
            }
        });

        TableColumn<NhanVien, LocalDate> colNgaySinh = new TableColumn<>("Ngày sinh");
        colNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        colNgaySinh.setPrefWidth(120);

        TableColumn<NhanVien, String> colSoDienThoai = new TableColumn<>("Số điện thoại");
        colSoDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colSoDienThoai.setPrefWidth(150);

        tableView.getColumns().addAll(colMaNV, colTenNV, colGioiTinh, colNgaySinh, colSoDienThoai);
        tableView.setItems(nhanVienList);

        // Xử lý sự kiện chọn dòng
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                hienThiThongTin(newSelection);
            }
        });

        centerBox.getChildren().add(tableView);
        return centerBox;
    }

    private VBox createRightSection() {
        VBox rightBox = new VBox(15);
        rightBox.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 20;");
        rightBox.setPrefWidth(350);

        Label formTitle = new Label("Thông tin nhân viên");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Form fields
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(15);

        // Mã nhân viên
        Label lblMaNV = new Label("Mã nhân viên:");
        txtMaNV = new TextField();
        txtMaNV.setPromptText("Tự động tạo");
        txtMaNV.setEditable(false);

        // Tên nhân viên
        Label lblTenNV = new Label("Tên nhân viên:");
        txtTenNV = new TextField();
        txtTenNV.setPromptText("Nhập tên nhân viên");

        // Giới tính
        Label lblGioiTinh = new Label("Giới tính:");
        cbGioiTinh = new ComboBox<>();
        cbGioiTinh.getItems().addAll("Nam", "Nữ");
        cbGioiTinh.setValue("Nam");

        // Ngày sinh
        Label lblNgaySinh = new Label("Ngày sinh:");
        dpNgaySinh = new DatePicker();
        dpNgaySinh.setValue(LocalDate.now());

        // Số điện thoại
        Label lblSoDienThoai = new Label("Số điện thoại:");
        txtSoDienThoai = new TextField();
        txtSoDienThoai.setPromptText("Nhập số điện thoại");

        // Thêm vào grid
        formGrid.add(lblMaNV, 0, 0);
        formGrid.add(txtMaNV, 1, 0);
        formGrid.add(lblTenNV, 0, 1);
        formGrid.add(txtTenNV, 1, 1);
        formGrid.add(lblGioiTinh, 0, 2);
        formGrid.add(cbGioiTinh, 1, 2);
        formGrid.add(lblNgaySinh, 0, 3);
        formGrid.add(dpNgaySinh, 1, 3);
        formGrid.add(lblSoDienThoai, 0, 4);
        formGrid.add(txtSoDienThoai, 1, 4);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnThem = new Button("Thêm");
        btnThem.setStyle("-fx-background-color: #28A745; -fx-text-fill: white;");
        btnThem.setOnAction(e -> themNhanVien());

        Button btnCapNhat = new Button("Cập nhật");
        btnCapNhat.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white;");
        btnCapNhat.setOnAction(e -> capNhatNhanVien());

        Button btnXoa = new Button("Xóa");
        btnXoa.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white;");
        btnXoa.setOnAction(e -> xoaNhanVien());

        Button btnLamMoi = new Button("Làm mới");
        btnLamMoi.setStyle("-fx-background-color: #6C757D; -fx-text-fill: white;");
        btnLamMoi.setOnAction(e -> xoaRongForm());

        buttonBox.getChildren().addAll(btnThem, btnCapNhat, btnXoa, btnLamMoi);

        rightBox.getChildren().addAll(formTitle, formGrid, buttonBox);
        return rightBox;
    }

    private void loadNhanVienData() {
        List<NhanVien> list = nhanVienDAO.getAllNhanVien();
        nhanVienList.clear();
        nhanVienList.addAll(list);
    }

    private void timKiemNhanVien() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            loadNhanVienData();
        } else {
            List<NhanVien> result = nhanVienDAO.timKiemNhanVien(keyword);
            nhanVienList.clear();
            nhanVienList.addAll(result);
        }
    }

    private void hienThiThongTin(NhanVien nv) {
        txtMaNV.setText(nv.getMaNV());
        txtTenNV.setText(nv.getTenNV());
        cbGioiTinh.setValue(nv.isGioiTinh() ? "Nam" : "Nữ");
        dpNgaySinh.setValue(nv.getNgaySinh());
        txtSoDienThoai.setText(nv.getSoDienThoai());
    }

    private void themNhanVien() {
        try {
            String maNV = "NV" + System.currentTimeMillis(); // Tạo mã tự động
            String tenNV = txtTenNV.getText().trim();
            boolean gioiTinh = cbGioiTinh.getValue().equals("Nam");
            LocalDate ngaySinh = dpNgaySinh.getValue();
            String soDienThoai = txtSoDienThoai.getText().trim();

            if (tenNV.isEmpty() || soDienThoai.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin!", Alert.AlertType.ERROR);
                return;
            }

            NhanVien nv = new NhanVien(maNV, tenNV, gioiTinh, ngaySinh, soDienThoai);

            if (nhanVienDAO.themNhanVien(nv)) {
                showAlert("Thành công", "Thêm nhân viên thành công!", Alert.AlertType.INFORMATION);
                loadNhanVienData();
                xoaRongForm();
            } else {
                showAlert("Lỗi", "Thêm nhân viên thất bại!", Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            showAlert("Lỗi", "Có lỗi xảy ra: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void capNhatNhanVien() {
        try {
            NhanVien selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Lỗi", "Vui lòng chọn nhân viên cần cập nhật!", Alert.AlertType.ERROR);
                return;
            }

            selected.setTenNV(txtTenNV.getText().trim());
            selected.setGioiTinh(cbGioiTinh.getValue().equals("Nam"));
            selected.setNgaySinh(dpNgaySinh.getValue());
            selected.setSoDienThoai(txtSoDienThoai.getText().trim());

            if (nhanVienDAO.capNhatNhanVien(selected)) {
                showAlert("Thành công", "Cập nhật nhân viên thành công!", Alert.AlertType.INFORMATION);
                loadNhanVienData();
                xoaRongForm();
            } else {
                showAlert("Lỗi", "Cập nhật nhân viên thất bại!", Alert.AlertType.ERROR);
            }
        } catch (Exception ex) {
            showAlert("Lỗi", "Có lỗi xảy ra: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void xoaNhanVien() {
        try {
            NhanVien selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Lỗi", "Vui lòng chọn nhân viên cần xóa!", Alert.AlertType.ERROR);
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Xác nhận");
            confirm.setHeaderText("Xóa nhân viên");
            confirm.setContentText("Bạn có chắc chắn muốn xóa nhân viên này?");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                if (nhanVienDAO.xoaNhanVien(selected.getMaNV())) {
                    showAlert("Thành công", "Xóa nhân viên thành công!", Alert.AlertType.INFORMATION);
                    loadNhanVienData();
                    xoaRongForm();
                } else {
                    showAlert("Lỗi", "Xóa nhân viên thất bại!", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception ex) {
            showAlert("Lỗi", "Có lỗi xảy ra: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void xoaRongForm() {
        txtMaNV.clear();
        txtTenNV.clear();
        cbGioiTinh.setValue("Nam");
        dpNgaySinh.setValue(LocalDate.now());
        txtSoDienThoai.clear();
        tableView.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
