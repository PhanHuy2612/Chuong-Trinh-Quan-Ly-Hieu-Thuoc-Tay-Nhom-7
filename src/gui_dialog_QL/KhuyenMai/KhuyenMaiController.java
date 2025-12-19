package gui_dialog_QL.KhuyenMai;

import dao.KhuyenMai_DAO;
import entity.KhuyenMai;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class KhuyenMaiController {

    private final KhuyenMaiView view;
    private final KhuyenMai_DAO dao;
    private final ObservableList<KhuyenMai> masterData; // Dữ liệu gốc từ DB
    private FilteredList<KhuyenMai> filteredData;       // Dữ liệu sau khi lọc

    public KhuyenMaiController(KhuyenMaiView view) {
        this.view = view;
        this.dao = new KhuyenMai_DAO();
        this.masterData = FXCollections.observableArrayList();

        initData();
        initTable();
        setupFiltersAndRefreshButton();
        setupButtons();
    }

    /* ================= LOAD DATA ================= */
    private void initData() {
        loadDataFromDatabase();
        filteredData = new FilteredList<>(masterData, p -> true);
        view.getTableKhuyenMai().setItems(filteredData);
        updateDashboard(masterData);
    }

    private void loadDataFromDatabase() {
        masterData.clear();
        masterData.addAll(dao.getAllKhuyenMai());
    }

    /* ================= INIT TABLE ================= */
    private void initTable() {
        TableView<KhuyenMai> table = view.getTableKhuyenMai();

        // Cột Mã KM
        TableColumn<KhuyenMai, String> colMa = (TableColumn<KhuyenMai, String>) table.getColumns().get(0);
        colMa.setCellValueFactory(new PropertyValueFactory<>("maKM"));

        // Cột Tên KM
        TableColumn<KhuyenMai, String> colTen = (TableColumn<KhuyenMai, String>) table.getColumns().get(1);
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenKM"));

        // Cột % giảm
        TableColumn<KhuyenMai, Double> colPhanTram = (TableColumn<KhuyenMai, Double>) table.getColumns().get(2);
        colPhanTram.setCellValueFactory(new PropertyValueFactory<>("phanTramGiam"));

        // Cột Giảm tối đa
        TableColumn<KhuyenMai, BigDecimal> colTienGiamToiDa = (TableColumn<KhuyenMai, BigDecimal>) table.getColumns().get(3);
        colTienGiamToiDa.setCellValueFactory(new PropertyValueFactory<>("tienGiamToiDa"));

        // Cột Loại áp dụng
        TableColumn<KhuyenMai, String> colLoaiApDung = (TableColumn<KhuyenMai, String>) table.getColumns().get(4);
        colLoaiApDung.setCellValueFactory(new PropertyValueFactory<>("loaiThuocApDung"));

        // Cột Bắt đầu
        TableColumn<KhuyenMai, LocalDate> colBatDau = (TableColumn<KhuyenMai, LocalDate>) table.getColumns().get(5);
        colBatDau.setCellValueFactory(new PropertyValueFactory<>("ngayBatDau"));

        // Cột Kết thúc
        TableColumn<KhuyenMai, LocalDate> colKetThuc = (TableColumn<KhuyenMai, LocalDate>) table.getColumns().get(6);
        colKetThuc.setCellValueFactory(new PropertyValueFactory<>("ngayKetThuc"));

        // Cột Trạng thái (tính động)
        TableColumn<KhuyenMai, String> colTrangThai = (TableColumn<KhuyenMai, String>) table.getColumns().get(7);
        colTrangThai.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(tinhTrangThai(cellData.getValue()))
        );

        // Cột Người tạo
        TableColumn<KhuyenMai, String> colNguoiTao = (TableColumn<KhuyenMai, String>) table.getColumns().get(8);
        colNguoiTao.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));

        // Cột Ngày tạo
        TableColumn<KhuyenMai, LocalDateTime> colNgayTao = (TableColumn<KhuyenMai, LocalDateTime>) table.getColumns().get(9);
        colNgayTao.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));

        // Cột Thao tác
        TableColumn<KhuyenMai, Void> colAction = (TableColumn<KhuyenMai, Void>) table.getColumns().get(10);
        colAction.setCellFactory(createActionCellFactory());
    }

    /* ================= FILTER & REFRESH ================= */
    private void setupFiltersAndRefreshButton() {
        // Tự động lọc khi người dùng thay đổi bất kỳ trường nào
        view.getTxtSearch().textProperty().addListener((obs, old, newVal) -> applyFilter());
        view.getCbTrangThai().valueProperty().addListener((obs, old, newVal) -> applyFilter());
        view.getDpFrom().valueProperty().addListener((obs, old, newVal) -> applyFilter());
        view.getDpTo().valueProperty().addListener((obs, old, newVal) -> applyFilter());

        // Nút "Làm mới" → reset toàn bộ filter và reload dữ liệu mới nhất
        view.getBtnLamMoi().setOnAction(e -> resetFilterAndReload());
    }

    private void applyFilter() {
        String keyword = view.getTxtSearch().getText().toLowerCase().trim();
        LocalDate from = view.getDpFrom().getValue();
        LocalDate to = view.getDpTo().getValue();
        String trangThaiFilter = view.getCbTrangThai().getValue();

        filteredData.setPredicate(createPredicate(keyword, from, to, trangThaiFilter));
        updateDashboard(filteredData);
    }

    private void resetFilterAndReload() {
        // Reset các trường filter về trạng thái mặc định
        view.getTxtSearch().clear();
        view.getDpFrom().setValue(null);
        view.getDpTo().setValue(null);
        view.getCbTrangThai().setValue("Tất cả");

        // Tải lại dữ liệu mới nhất từ database
        loadDataFromDatabase();

        // Hiển thị toàn bộ dữ liệu (không lọc)
        filteredData.setPredicate(p -> true);

        // Cập nhật dashboard với dữ liệu đầy đủ
        updateDashboard(masterData);
    }

    private Predicate<KhuyenMai> createPredicate(String keyword, LocalDate from, LocalDate to, String trangThaiFilter) {
        return km -> {
            // Lọc theo từ khóa (mã hoặc tên)
            if (!keyword.isEmpty()) {
                boolean matchesKeyword = km.getMaKM().toLowerCase().contains(keyword)
                        || km.getTenKM().toLowerCase().contains(keyword);
                if (!matchesKeyword) return false;
            }

            // Lọc theo khoảng thời gian (dựa trên ngày bắt đầu)
            if (from != null && km.getNgayBatDau() != null && km.getNgayBatDau().isBefore(from)) {
                return false;
            }
            if (to != null && km.getNgayBatDau() != null && km.getNgayBatDau().isAfter(to)) {
                return false;
            }

            // Lọc theo trạng thái
            if (!"Tất cả".equals(trangThaiFilter)) {
                String tt = tinhTrangThai(km);
                if (!tt.equals(trangThaiFilter)) return false;
            }

            return true;
        };
    }

    /* ================= DASHBOARD ================= */
    private void updateDashboard(ObservableList<KhuyenMai> data) {
        int tong = data.size();
        int dangApDung = 0;
        int sapBatDau = 0;
        int daKetThuc = 0;

        for (KhuyenMai km : data) {
            String tt = tinhTrangThai(km);
            switch (tt) {
                case "Đang áp dụng" -> dangApDung++;
                case "Sắp bắt đầu" -> sapBatDau++;
                case "Đã kết thúc" -> daKetThuc++;
            }
        }

        view.getLblTongKM().setText(String.valueOf(tong));
        view.getLblDangApDung().setText(String.valueOf(dangApDung));
        view.getLblSapBatDau().setText(String.valueOf(sapBatDau));
        view.getLblDaKetThuc().setText(String.valueOf(daKetThuc));
    }

    /* ================= TRẠNG THÁI ================= */
    private String tinhTrangThai(KhuyenMai km) {
        if (km.getNgayBatDau() == null || km.getNgayKetThuc() == null) return "Không xác định";

        LocalDate today = LocalDate.now();

        if (today.isBefore(km.getNgayBatDau())) {
            return "Sắp bắt đầu";
        } else if (today.isAfter(km.getNgayKetThuc())) {
            return "Đã kết thúc";
        } else {
            return "Đang áp dụng";
        }
    }

    /* ================= ACTION CELL (Sửa / Xóa) ================= */
    private Callback<TableColumn<KhuyenMai, Void>, TableCell<KhuyenMai, Void>> createActionCellFactory() {
        return param -> new TableCell<>() {
            private final Button btnSua = new Button("Sửa");
            private final Button btnXoa = new Button("Xóa");
            private final HBox box = new HBox(10, btnSua, btnXoa);

            {
                btnSua.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-padding: 5 15; -fx-font-size: 12;");
                btnXoa.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-padding: 5 15; -fx-font-size: 12;");

                btnSua.setOnAction(e -> {
                    KhuyenMai km = getTableView().getItems().get(getIndex());
                    // TODO: Mở form chỉnh sửa
                    System.out.println("Sửa khuyến mãi: " + km.getMaKM());
                });

                btnXoa.setOnAction(e -> {
                    KhuyenMai km = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Xác nhận xóa");
                    confirm.setHeaderText(null);
                    confirm.setContentText("Bạn có chắc muốn xóa khuyến mãi:\n" + km.getTenKM() + " (" + km.getMaKM() + ") ?");

                    if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                        // TODO: Gọi DAO để xóa thực sự trong DB
                        // dao.deleteKhuyenMai(km.getMaKM());

                        // Sau khi xóa thành công, reload dữ liệu
                        resetFilterAndReload();

                        new Alert(Alert.AlertType.INFORMATION, "Xóa khuyến mãi thành công!").show();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        };
    }

    /* ================= BUTTONS ================= */
    private void setupButtons() {
        view.getBtnThemKhuyenMai().setOnAction(e -> {
            // TODO: Mở dialog/form thêm khuyến mãi mới
            System.out.println("Mở form thêm khuyến mãi");
        });
    }
}