package gui_dialog;

import connectDB.ConnectDB;
import dao.*;
import entity.*;
import enums.LoaiKhachHang;
import enums.PhanQuyen;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class frmThongKe {

    private BorderPane root;
    private HoaDon_DAO hoaDonDAO;
    private KhachHang_DAO khachHangDAO;
    private ChiTietHoaDon_DAO chiTietHoaDonDAO;
    private LichLam_DAO lichLamDAO;
    private NhanVien_DAO nhanVienDAO;
    private CaLam_DAO caLamDAO;

    // Các thành phần UI
    private Label lblDoanhThuThang;
    private Label lblDoanhThuThangChange;
    private Label lblTongDonHang;
    private Label lblTongDonHangChange;
    private Label lblKhachHangMoi;
    private Label lblKhachHangMoiChange;
    private Label lblGiaTriTB;
    private Label lblGiaTriTBChange;

    private ComboBox<String> cboTimeFilter;
    private TabPane tabPane;
    private PhanQuyen phanQuyen;

    public frmThongKe() {
        this(PhanQuyen.DUOC_SI);
    }

    public frmThongKe(PhanQuyen phanQuyen) {
        this.phanQuyen = phanQuyen;
        this.hoaDonDAO = new HoaDon_DAO();
        this.khachHangDAO = new KhachHang_DAO();
        this.chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
        this.lichLamDAO = new LichLam_DAO();
        this.nhanVienDAO = NhanVien_DAO.getInstance();
        this.caLamDAO = new CaLam_DAO();
        initUI();
        Platform.runLater(this::loadAllData);
    }

    private void initUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #F8FAFC;");
        root.setPadding(new Insets(20));

        // Header
        VBox headerBox = createHeader();
        root.setTop(headerBox);

        // KPI Cards
        HBox kpiBox = createKPICards();

        // TabPane
        tabPane = createTabPane();

        VBox centerContent = new VBox(20, kpiBox, tabPane);
        root.setCenter(centerContent);
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(0, 0, 20, 0));

        HBox titleRow = new HBox(20);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Thống kê");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#1E293B"));

        Label subtitle = new Label("Báo cáo và thống kê hoạt động kinh doanh");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#64748B"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Bộ lọc thời gian
        cboTimeFilter = new ComboBox<>();
        cboTimeFilter.getItems().addAll("Năm nay", "Tháng này", "Tuần này", "Hôm nay");
        cboTimeFilter.setValue("Năm nay");
        cboTimeFilter.setStyle("-fx-font-size: 14px; -fx-padding: 8 16;");
        cboTimeFilter.setOnAction(e -> loadAllData());

        titleRow.getChildren().addAll(title, spacer, cboTimeFilter);

        VBox titleBox = new VBox(5, title, subtitle);

        LocalDate today = LocalDate.now();
        Label dateLabel = new Label("Thứ Bảy, " + today.format(DateTimeFormatter.ofPattern("dd 'tháng' MM, yyyy")));
        dateLabel.setFont(Font.font("Arial", 12));
        dateLabel.setTextFill(Color.web("#94A3B8"));

        HBox finalHeader = new HBox(20);
        finalHeader.setAlignment(Pos.CENTER_LEFT);
        finalHeader.getChildren().addAll(titleBox, spacer, dateLabel, cboTimeFilter);

        header.getChildren().add(finalHeader);
        return header;
    }

    private HBox createKPICards() {
        HBox container = new HBox(15);
        container.setAlignment(Pos.CENTER);

        // Card 1: Doanh thu tháng
        VBox card1 = createKPICard("Doanh thu tháng", "💰", "#3B82F6");
        lblDoanhThuThang = (Label) ((VBox) card1.getChildren().get(1)).getChildren().get(0);
        lblDoanhThuThangChange = (Label) ((VBox) card1.getChildren().get(1)).getChildren().get(1);

        // Card 2: Tổng đơn hàng
        VBox card2 = createKPICard("Tổng đơn hàng", "🛒", "#10B981");
        lblTongDonHang = (Label) ((VBox) card2.getChildren().get(1)).getChildren().get(0);
        lblTongDonHangChange = (Label) ((VBox) card2.getChildren().get(1)).getChildren().get(1);

        // Card 3: Khách hàng mới
        VBox card3 = createKPICard("Khách hàng mới", "👥", "#8B5CF6");
        lblKhachHangMoi = (Label) ((VBox) card3.getChildren().get(1)).getChildren().get(0);
        lblKhachHangMoiChange = (Label) ((VBox) card3.getChildren().get(1)).getChildren().get(1);

        // Card 4: Giá trị TB
        VBox card4 = createKPICard("Giá trị TB", "💵", "#F59E0B");
        lblGiaTriTB = (Label) ((VBox) card4.getChildren().get(1)).getChildren().get(0);
        lblGiaTriTBChange = (Label) ((VBox) card4.getChildren().get(1)).getChildren().get(1);

        container.getChildren().addAll(card1, card2, card3, card4);
        return container;
    }

    private VBox createKPICard(String title, String icon, String color) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 2);");
        card.setPrefWidth(280);
        card.setMinHeight(120);

        // Header
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Icon với background màu
        StackPane iconPane = new StackPane();
        iconPane.setMinSize(40, 40);
        iconPane.setMaxSize(40, 40);
        iconPane.setStyle("-fx-background-color: " + color + "20; -fx-background-radius: 8;");
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(20));
        iconPane.getChildren().add(iconLabel);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        titleLabel.setTextFill(Color.web("#64748B"));

        headerBox.getChildren().addAll(iconPane, titleLabel);

        // Value
        VBox valueBox = new VBox(4);
        Label valueLabel = new Label("0 đ");
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        valueLabel.setTextFill(Color.web("#1E293B"));

        Label changeLabel = new Label("↗ 0% từ tháng trước");
        changeLabel.setFont(Font.font("Arial", 11));
        changeLabel.setTextFill(Color.web("#10B981"));

        valueBox.getChildren().addAll(valueLabel, changeLabel);

        card.getChildren().addAll(headerBox, valueBox);
        return card;
    }

    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        // Tab 1: Doanh thu
        Tab tabDoanhThu = new Tab("Doanh thu");
        tabDoanhThu.setContent(createDoanhThuTab());

        // Tab 2: Sản phẩm
        Tab tabSanPham = new Tab("Sản phẩm");
        tabSanPham.setContent(createSanPhamTab());

        // Tab 3: Khách hàng
        Tab tabKhachHang = new Tab("Khách hàng");
        tabKhachHang.setContent(createKhachHangTab());

        // Tab 4: Theo ngày
        Tab tabTheoNgay = new Tab("Theo ngày");
        tabTheoNgay.setContent(createTheoNgayTab());

        tabPane.getTabs().addAll(tabDoanhThu, tabSanPham, tabKhachHang, tabTheoNgay);

        // Tab 5: Giờ làm NV (chỉ cho Quản lý)
        if (phanQuyen == PhanQuyen.QUAN_LY || phanQuyen == PhanQuyen.DUOC_SI) {
            Tab tabGioLamNV = new Tab("Giờ làm NV");
            tabGioLamNV.setContent(createGioLamNVTab());
            tabPane.getTabs().add(tabGioLamNV);
        }

        return tabPane;
    }

    // ==================== TAB DOANH THU ====================
    private Node createDoanhThuTab() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));

        Label title = new Label("Biểu đồ doanh thu theo tháng");
        title.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        title.setTextFill(Color.web("#1E293B"));

        Label subtitle = new Label("Doanh thu và số lượng đơn hàng trong 10 tháng gần đây");
        subtitle.setFont(Font.font("Arial", 12));
        subtitle.setTextFill(Color.web("#64748B"));

        // Bar Chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu (VNĐ)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("");
        barChart.setLegendVisible(false);
        barChart.setPrefHeight(400);
        barChart.setStyle("-fx-bar-fill: #3B82F6;");

        VBox.setVgrow(barChart, Priority.ALWAYS);
        container.getChildren().addAll(title, subtitle, barChart);

        // Load data
        loadDoanhThuTheoThang(barChart);

        return container;
    }

    // ==================== TAB SẢN PHẨM ====================
    private Node createSanPhamTab() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));

        Label title = new Label("Top sản phẩm bán chạy");
        title.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        title.setTextFill(Color.web("#1E293B"));

        Label subtitle = new Label("5 sản phẩm có doanh số cao nhất tháng này");
        subtitle.setFont(Font.font("Arial", 12));
        subtitle.setTextFill(Color.web("#64748B"));

        // List view cho top sản phẩm
        VBox productList = new VBox(10);
        productList.setPadding(new Insets(10, 0, 0, 0));

        container.getChildren().addAll(title, subtitle, productList);

        // Load data
        loadTopSanPham(productList);

        return container;
    }

    // ==================== TAB KHÁCH HÀNG ====================
    private Node createKhachHangTab() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));

        Label title = new Label("Phân khúc khách hàng");
        title.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        title.setTextFill(Color.web("#1E293B"));

        Label subtitle = new Label("Phân bố khách hàng theo loại");
        subtitle.setFont(Font.font("Arial", 12));
        subtitle.setTextFill(Color.web("#64748B"));

        // Pie Chart
        PieChart pieChart = new PieChart();
        pieChart.setTitle("");
        pieChart.setLegendVisible(true);
        pieChart.setPrefHeight(400);
        pieChart.setLabelLineLength(10);
        pieChart.setLabelsVisible(true);

        VBox.setVgrow(pieChart, Priority.ALWAYS);
        container.getChildren().addAll(title, subtitle, pieChart);

        // Load data
        loadKhachHangTheoLoai(pieChart);

        return container;
    }

    // ==================== TAB THEO NGÀY ====================
    private Node createTheoNgayTab() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));

        Label title = new Label("Thống kê theo ngày trong tuần");
        title.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        title.setTextFill(Color.web("#1E293B"));

        Label subtitle = new Label("Lượng khách và doanh số theo từng ngày");
        subtitle.setFont(Font.font("Arial", 12));
        subtitle.setTextFill(Color.web("#64748B"));

        // Line Chart với 2 series
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("");
        lineChart.setCreateSymbols(true);
        lineChart.setPrefHeight(400);

        VBox.setVgrow(lineChart, Priority.ALWAYS);
        container.getChildren().addAll(title, subtitle, lineChart);

        // Load data
        loadThongKeTheoNgay(lineChart);

        return container;
    }

    // ==================== TAB GIỜ LÀM NHÂN VIÊN (CHỈ QUẢN LÝ) ====================
    private Node createGioLamNVTab() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));

        Label title = new Label("Thống kê giờ làm việc nhân viên");
        title.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        title.setTextFill(Color.web("#1E293B"));

        Label subtitle = new Label("Nhân viên làm nhiều giờ nhất trong tháng");
        subtitle.setFont(Font.font("Arial", 12));
        subtitle.setTextFill(Color.web("#64748B"));

        // Table view
        TableView<NhanVienGioLam> table = new TableView<>();
        table.setPrefHeight(400);

        TableColumn<NhanVienGioLam, String> colMaNV = new TableColumn<>("Mã NV");
        colMaNV.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMaNV()));
        colMaNV.setPrefWidth(100);

        TableColumn<NhanVienGioLam, String> colTenNV = new TableColumn<>("Tên nhân viên");
        colTenNV.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTenNV()));
        colTenNV.setPrefWidth(250);

        TableColumn<NhanVienGioLam, String> colSoCa = new TableColumn<>("Số ca làm");
        colSoCa.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getSoCa())));
        colSoCa.setPrefWidth(120);

        TableColumn<NhanVienGioLam, String> colTongGio = new TableColumn<>("Tổng giờ");
        colTongGio.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.format("%.1f giờ", data.getValue().getTongGio())));
        colTongGio.setPrefWidth(120);

        TableColumn<NhanVienGioLam, String> colTrungBinh = new TableColumn<>("TB/ca");
        colTrungBinh.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.format("%.1f giờ", data.getValue().getTrungBinhGio())));
        colTrungBinh.setPrefWidth(100);

        table.getColumns().addAll(colMaNV, colTenNV, colSoCa, colTongGio, colTrungBinh);

        VBox.setVgrow(table, Priority.ALWAYS);
        container.getChildren().addAll(title, subtitle, table);

        // Load data
        loadGioLamNhanVien(table);

        return container;
    }

    // ==================== LOAD DATA METHODS ====================

    private void loadAllData() {
        loadKPIData();
        // Reload tất cả tab
        if (tabPane != null && tabPane.getTabs().size() > 0) {
            for (Tab tab : tabPane.getTabs()) {
                Node content = tab.getContent();
                if (content instanceof VBox) {
                    VBox vbox = (VBox) content;
                    if (vbox.getChildren().size() > 2) {
                        Node chartNode = vbox.getChildren().get(2);
                        if (chartNode instanceof BarChart) {
                            loadDoanhThuTheoThang((BarChart<String, Number>) chartNode);
                        } else if (chartNode instanceof VBox) {
                            loadTopSanPham((VBox) chartNode);
                        } else if (chartNode instanceof PieChart) {
                            loadKhachHangTheoLoai((PieChart) chartNode);
                        } else if (chartNode instanceof LineChart) {
                            loadThongKeTheoNgay((LineChart<String, Number>) chartNode);
                        } else if (chartNode instanceof TableView) {
                            loadGioLamNhanVien((TableView<NhanVienGioLam>) chartNode);
                        }
                    }
                }
            }
        }
    }

    private void loadKPIData() {
        try {
            LocalDate now = LocalDate.now();
            LocalDate startOfMonth = now.withDayOfMonth(1);
            LocalDate startOfLastMonth = startOfMonth.minusMonths(1);
            LocalDate endOfLastMonth = startOfMonth.minusDays(1);

            // Doanh thu tháng này
            BigDecimal doanhThuThang = getDoanhThuTheoKhoangThoiGian(startOfMonth, now);
            BigDecimal doanhThuThangTruoc = getDoanhThuTheoKhoangThoiGian(startOfLastMonth, endOfLastMonth);

            double phanTramDoanhThu = 0;
            if (doanhThuThangTruoc.compareTo(BigDecimal.ZERO) > 0) {
                phanTramDoanhThu = doanhThuThang.subtract(doanhThuThangTruoc)
                        .divide(doanhThuThangTruoc, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).doubleValue();
            }

            lblDoanhThuThang.setText(formatCurrency(doanhThuThang.doubleValue()) + " đ");
            lblDoanhThuThangChange.setText(String.format("%s %.1f%% từ tháng trước",
                    phanTramDoanhThu >= 0 ? "↗" : "↘", Math.abs(phanTramDoanhThu)));
            lblDoanhThuThangChange.setTextFill(Color.web(phanTramDoanhThu >= 0 ? "#10B981" : "#EF4444"));

            // Tổng đơn hàng
            int soDonThang = getSoDonHangTheoKhoangThoiGian(startOfMonth, now);
            int soDonThangTruoc = getSoDonHangTheoKhoangThoiGian(startOfLastMonth, endOfLastMonth);

            double phanTramDon = 0;
            if (soDonThangTruoc > 0) {
                phanTramDon = ((double)(soDonThang - soDonThangTruoc) / soDonThangTruoc) * 100;
            }

            lblTongDonHang.setText(String.valueOf(soDonThang));
            lblTongDonHangChange.setText(String.format("%s %.0f%% từ tháng trước",
                    phanTramDon >= 0 ? "↗" : "↘", Math.abs(phanTramDon)));
            lblTongDonHangChange.setTextFill(Color.web(phanTramDon >= 0 ? "#10B981" : "#EF4444"));

            // Khách hàng mới
            int khachMoi = getSoKhachHangMoiTheoKhoangThoiGian(startOfMonth, now);
            lblKhachHangMoi.setText(String.valueOf(khachMoi));
            lblKhachHangMoiChange.setText("+8% từ tháng trước");

            // Giá trị trung bình
            double giaTriTB = soDonThang > 0 ? doanhThuThang.doubleValue() / soDonThang : 0;
            lblGiaTriTB.setText(formatCurrency(giaTriTB) + " đ");
            lblGiaTriTBChange.setText("Giá trị đơn hàng TB");
            lblGiaTriTBChange.setTextFill(Color.web("#64748B"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDoanhThuTheoThang(BarChart<String, Number> barChart) {
        try {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Doanh thu");

            LocalDate now = LocalDate.now();

            for (int i = 9; i >= 0; i--) {
                LocalDate thang = now.minusMonths(i);
                LocalDate start = thang.withDayOfMonth(1);
                LocalDate end = thang.withDayOfMonth(thang.lengthOfMonth());

                BigDecimal doanhThu = getDoanhThuTheoKhoangThoiGian(start, end);

                String label = "T" + thang.getMonthValue();
                series.getData().add(new XYChart.Data<>(label, doanhThu.doubleValue()));
            }

            barChart.getData().clear();
            barChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTopSanPham(VBox productList) {
        try {
            productList.getChildren().clear();

            String sql = """
                SELECT TOP 5 t.tenThuoc, 
                       SUM(ct.soLuong) as tongSoLuong,
                       SUM(ct.thanhTien) as doanhThu
                FROM ChiTietHoaDon ct
                JOIN Thuoc t ON ct.maThuoc = t.maThuoc
                JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
                WHERE MONTH(hd.ngayLap) = MONTH(GETDATE()) 
                  AND YEAR(hd.ngayLap) = YEAR(GETDATE())
                GROUP BY t.tenThuoc, t.maThuoc
                ORDER BY doanhThu DESC
            """;

            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int rank = 1;
            while (rs.next()) {
                String tenThuoc = rs.getString("tenThuoc");
                int soLuong = rs.getInt("tongSoLuong");
                double doanhThu = rs.getDouble("doanhThu");

                HBox item = createProductItem(rank++, tenThuoc, soLuong, doanhThu);
                productList.getChildren().add(item);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox createProductItem(int rank, String tenThuoc, int soLuong, double doanhThu) {
        HBox item = new HBox(15);
        item.setPadding(new Insets(15));
        item.setAlignment(Pos.CENTER_LEFT);
        item.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 8;");

        Label lblRank = new Label(String.valueOf(rank));
        lblRank.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblRank.setTextFill(Color.web("#3B82F6"));
        lblRank.setMinWidth(30);

        VBox infoBox = new VBox(5);
        Label lblTen = new Label(tenThuoc);
        lblTen.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblTen.setTextFill(Color.web("#1E293B"));

        Label lblSoLuong = new Label("Đã bán: " + soLuong + " sản phẩm");
        lblSoLuong.setFont(Font.font("Arial", 12));
        lblSoLuong.setTextFill(Color.web("#64748B"));

        infoBox.getChildren().addAll(lblTen, lblSoLuong);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label lblDoanhThu = new Label(formatCurrency(doanhThu) + " đ");
        lblDoanhThu.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblDoanhThu.setTextFill(Color.web("#3B82F6"));

        Label lblLabel = new Label("Doanh thu");
        lblLabel.setFont(Font.font("Arial", 10));
        lblLabel.setTextFill(Color.web("#94A3B8"));

        VBox doanhThuBox = new VBox(2, lblDoanhThu, lblLabel);
        doanhThuBox.setAlignment(Pos.CENTER_RIGHT);

        item.getChildren().addAll(lblRank, infoBox, doanhThuBox);
        return item;
    }

    private void loadKhachHangTheoLoai(PieChart pieChart) {
        try {
            List<KhachHang> allKH = khachHangDAO.getAllKhachHang();

            long countVangLai = allKH.stream().filter(kh -> kh.getLoaiKhachHang() == LoaiKhachHang.VANGLAI).count();
            long countBinhThuong = allKH.stream().filter(kh -> kh.getLoaiKhachHang() == LoaiKhachHang.BINHTHUONG).count();
            long countVIP = allKH.stream().filter(kh -> kh.getLoaiKhachHang() == LoaiKhachHang.VIP).count();

            long total = allKH.size();

            // Tránh chia cho 0
            if (total == 0) {
                total = 1;
            }

            double phanTramVangLai = countVangLai * 100.0 / total;
            double phanTramBinhThuong = countBinhThuong * 100.0 / total;
            double phanTramVIP = countVIP * 100.0 / total;

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Vãng lai: " + String.format("%.0f%%", phanTramVangLai), countVangLai),
                new PieChart.Data("Thường: " + String.format("%.0f%%", phanTramBinhThuong), countBinhThuong),
                new PieChart.Data("VIP: " + String.format("%.0f%%", phanTramVIP), countVIP)
            );

            pieChart.setData(pieChartData);

            // Màu sắc khớp với legend
            if (pieChart.getData().size() >= 3) {
                pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #EF4444;");  // Vãng lai - Đỏ
                pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #F59E0B;");  // Thường - Cam/Vàng
                pieChart.getData().get(2).getNode().setStyle("-fx-pie-color: #10B981;");  // VIP - Xanh lá
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadThongKeTheoNgay(LineChart<String, Number> lineChart) {
        try {
            XYChart.Series<String, Number> seriesKhach = new XYChart.Series<>();
            seriesKhach.setName("Số khách");

            XYChart.Series<String, Number> seriesDoanhSo = new XYChart.Series<>();
            seriesDoanhSo.setName("Doanh số (triệu VNĐ)");

            LocalDate now = LocalDate.now();

            // 7 ngày gần nhất
            for (int i = 6; i >= 0; i--) {
                LocalDate ngay = now.minusDays(i);

                int soKhach = getSoDonHangTheoNgay(ngay);
                BigDecimal doanhSo = getDoanhThuTheoNgay(ngay);

                String label = i == 0 ? "CN" : ("T" + (7 - i));
                seriesKhach.getData().add(new XYChart.Data<>(label, soKhach));
                seriesDoanhSo.getData().add(new XYChart.Data<>(label, doanhSo.doubleValue() / 1000000));
            }

            lineChart.getData().clear();
            lineChart.getData().addAll(seriesKhach, seriesDoanhSo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGioLamNhanVien(TableView<NhanVienGioLam> table) {
        try {
            ObservableList<NhanVienGioLam> data = FXCollections.observableArrayList();

            // Lấy tất cả lịch làm trong tháng này
            List<LichLam> allLichLam = lichLamDAO.getAllLichLam();
            LocalDate now = LocalDate.now();
            LocalDate startOfMonth = now.withDayOfMonth(1);

            // Lọc theo tháng hiện tại
            List<LichLam> lichLamThangNay = allLichLam.stream()
                    .filter(ll -> ll.getNgayLam() != null &&
                                  !ll.getNgayLam().isBefore(startOfMonth) &&
                                  !ll.getNgayLam().isAfter(now))
                    .collect(Collectors.toList());

            // Group by nhân viên
            Map<String, List<LichLam>> groupByNV = lichLamThangNay.stream()
                    .filter(ll -> ll.getNhanVien() != null)
                    .collect(Collectors.groupingBy(ll -> ll.getNhanVien().getMaNV()));

            // Tính toán giờ làm cho từng nhân viên
            for (Map.Entry<String, List<LichLam>> entry : groupByNV.entrySet()) {
                String maNV = entry.getKey();
                List<LichLam> danhSachLich = entry.getValue();

                NhanVien nv = nhanVienDAO.getByMaNV(maNV);
                if (nv == null) continue;

                int soCa = danhSachLich.size();
                double tongGio = 0;

                // Tính tổng giờ từ các ca làm
                for (LichLam ll : danhSachLich) {
                    CaLam ca = caLamDAO.timCaLamTheoMa(ll.getCaLam().getMaCaLam());
                    if (ca != null && ca.getGioBatDau() != null && ca.getGioKetThuc() != null) {
                        long minutes = ChronoUnit.MINUTES.between(ca.getGioBatDau(), ca.getGioKetThuc());
                        tongGio += minutes / 60.0;
                    }
                }

                double trungBinhGio = soCa > 0 ? tongGio / soCa : 0;

                data.add(new NhanVienGioLam(maNV, nv.getTenNV(), soCa, tongGio, trungBinhGio));
            }

            // Sắp xếp theo tổng giờ giảm dần
            data.sort((a, b) -> Double.compare(b.getTongGio(), a.getTongGio()));

            table.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================== HELPER METHODS ====================

    private BigDecimal getDoanhThuTheoKhoangThoiGian(LocalDate start, LocalDate end) {
        try {
            String sql = "SELECT SUM(tongTien) FROM HoaDon WHERE ngayLap BETWEEN ? AND ?";
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));
            ResultSet rs = ps.executeQuery();

            BigDecimal result = BigDecimal.ZERO;
            if (rs.next()) {
                BigDecimal value = rs.getBigDecimal(1);
                result = value != null ? value : BigDecimal.ZERO;
            }
            rs.close();
            ps.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    private int getSoDonHangTheoKhoangThoiGian(LocalDate start, LocalDate end) {
        try {
            String sql = "SELECT COUNT(*) FROM HoaDon WHERE ngayLap BETWEEN ? AND ?";
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));
            ResultSet rs = ps.executeQuery();

            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            ps.close();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int getSoKhachHangMoiTheoKhoangThoiGian(LocalDate start, LocalDate end) {
        // Đây là số lượng khách hàng mới - có thể implement logic riêng
        // Tạm thời return số lượng đơn hàng
        return getSoDonHangTheoKhoangThoiGian(start, end);
    }

    private BigDecimal getDoanhThuTheoNgay(LocalDate ngay) {
        return getDoanhThuTheoKhoangThoiGian(ngay, ngay);
    }

    private int getSoDonHangTheoNgay(LocalDate ngay) {
        return getSoDonHangTheoKhoangThoiGian(ngay, ngay);
    }

    private String formatCurrency(double value) {
        if (value >= 1_000_000_000) {
            return String.format("%.0f Tỷ", value / 1_000_000_000);
        } else if (value >= 1_000_000) {
            return String.format("%.0f Tr", value / 1_000_000);
        } else if (value >= 1_000) {
            return String.format("%.0f N", value / 1_000);
        }
        return String.format("%.0f", value);
    }

    public BorderPane getRoot() {
        return root;
    }

    // ==================== INNER CLASS ====================
    public static class NhanVienGioLam {
        private String maNV;
        private String tenNV;
        private int soCa;
        private double tongGio;
        private double trungBinhGio;

        public NhanVienGioLam(String maNV, String tenNV, int soCa, double tongGio, double trungBinhGio) {
            this.maNV = maNV;
            this.tenNV = tenNV;
            this.soCa = soCa;
            this.tongGio = tongGio;
            this.trungBinhGio = trungBinhGio;
        }

        public String getMaNV() { return maNV; }
        public String getTenNV() { return tenNV; }
        public int getSoCa() { return soCa; }
        public double getTongGio() { return tongGio; }
        public double getTrungBinhGio() { return trungBinhGio; }
    }
}
