package gui_dialog;

import connectDB.ConnectDB;
import dao.HoaDon_DAO;
import dao.Thuoc_DAO;
import entity.HoaDon;
import entity.Thuoc;
import enums.TrangThaiTonKho;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Dashboard {

    private VBox root;
    private Thuoc_DAO thuocDAO;
    private HoaDon_DAO hoaDonDAO;

    // Bộ lọc thời gian
    private ComboBox<String> timeFilterCombo;
    private LocalDate filterDate = LocalDate.now();

    // Các thành phần hiển thị
    private Label lblTonKhoConHang;
    private Label lblTonKhoSapHet;
    private Label lblDoanhThu;
    private Label lblSoHoaDon;
    private PieChart pieChartNguonDoanhThu;
    private PieChart pieChartLoaiThuoc;
    private LineChart<String, Number> lineChartGioCaoDiem;
    private VBox alertBox;

    private ScheduledExecutorService scheduler;

    public Dashboard() {
        this.thuocDAO = new Thuoc_DAO();
        this.hoaDonDAO = new HoaDon_DAO();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        initUI();

        // Load data ngay lập tức
        Platform.runLater(() -> {
            try {
                loadData();
                startAutoRefresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initUI() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #F8FAFC;");

        // Header với bộ lọc
        HBox header = createHeader();
        root.getChildren().add(header);

        // Grid chính - 2 hàng
        VBox mainContent = new VBox(15);

        // Hàng 1: 4 thẻ KPI
        HBox row1 = createKPIRow();

        // Hàng 2: 3 biểu đồ + cảnh báo
        HBox row2 = createChartsRow();

        mainContent.getChildren().addAll(row1, row2);
        root.getChildren().add(mainContent);
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 10, 0));

        Label title = new Label("DASHBOARD - TỔNG QUAN HOẠT ĐỘNG");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#1E293B"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Bộ lọc thời gian
        Label lblFilter = new Label("Xem theo:");
        lblFilter.setFont(Font.font("Arial", 14));

        timeFilterCombo = new ComboBox<>();
        timeFilterCombo.getItems().addAll("Hôm nay", "7 ngày", "30 ngày", "90 ngày");
        timeFilterCombo.setValue("Hôm nay");
        timeFilterCombo.setStyle("-fx-font-size: 14px;");
        timeFilterCombo.setOnAction(e -> loadData());

        Button btnRefresh = new Button("🔄 Làm mới");
        btnRefresh.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 8 16;");
        btnRefresh.setOnAction(e -> loadData());

        header.getChildren().addAll(title, spacer, lblFilter, timeFilterCombo, btnRefresh);
        return header;
    }

    private HBox createKPIRow() {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER);

        // 1. Tỷ lệ thuốc còn hàng
        VBox cardConHang = createKPICard("TỶ LỆ CÒN HÀNG", "0%", "0/0 thuốc", "#10B981", "📦");
        lblTonKhoConHang = (Label) cardConHang.getUserData();
        if (lblTonKhoConHang == null) {
            lblTonKhoConHang = new Label("0%");
        }

        // 2. Tỷ lệ sắp hết/hết hàng
        VBox cardSapHet = createKPICard("CẢNH BÁO TỒN KHO", "0%", "0/0 thuốc", "#EF4444", "⚠️");
        lblTonKhoSapHet = (Label) cardSapHet.getUserData();
        if (lblTonKhoSapHet == null) {
            lblTonKhoSapHet = new Label("0%");
        }

        // 3. Doanh thu
        VBox cardDoanhThu = createKPICard("DOANH THU", "0 VNĐ", "Tăng 0% so với cùng kỳ", "#3B82F6", "💰");
        lblDoanhThu = (Label) cardDoanhThu.getUserData();
        if (lblDoanhThu == null) {
            lblDoanhThu = new Label("0 VNĐ");
        }

        // 4. Số hóa đơn
        VBox cardHoaDon = createKPICard("SỐ HÓA ĐƠN", "0", "0 khách hàng", "#8B5CF6", "🧾");
        lblSoHoaDon = (Label) cardHoaDon.getUserData();
        if (lblSoHoaDon == null) {
            lblSoHoaDon = new Label("0");
        }

        row.getChildren().addAll(cardConHang, cardSapHet, cardDoanhThu, cardHoaDon);
        return row;
    }

    private VBox createKPICard(String title, String value, String subtext, String color, String icon) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        card.setPrefWidth(280);
        card.setMinHeight(140);

        // Header
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(28));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        titleLabel.setTextFill(Color.web("#64748B"));

        headerBox.getChildren().addAll(iconLabel, titleLabel);

        // Value
        VBox valueBox = new VBox(4);
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        valueLabel.setTextFill(Color.web(color));

        Label subtextLabel = new Label(subtext);
        subtextLabel.setFont(Font.font("Arial", 12));
        subtextLabel.setTextFill(Color.web("#94A3B8"));

        valueBox.getChildren().addAll(valueLabel, subtextLabel);

        card.getChildren().addAll(headerBox, valueBox);

        // Lưu label vào userData của card
        card.setUserData(valueLabel);
        valueLabel.setUserData(subtextLabel);

        return card;
    }

    private HBox createChartsRow() {
        HBox row = new HBox(15);
        row.setAlignment(Pos.TOP_CENTER);

        // 1. Nguồn doanh thu (theo loại thuốc)
        VBox cardNguonDT = createChartCard("NGUỒN DOANH THU", 350, 340);
        pieChartNguonDoanhThu = createPieChart();
        cardNguonDT.getChildren().add(pieChartNguonDoanhThu);

        // 2. Khung giờ cao điểm
        VBox cardGioCaoDiem = createChartCard("KHUNG GIỜ CAO ĐIỂM", 400, 340);
        lineChartGioCaoDiem = createLineChart();
        cardGioCaoDiem.getChildren().add(lineChartGioCaoDiem);

        // 3. Tỷ lệ bán theo phân loại
        VBox cardLoaiThuoc = createChartCard("TỶ LỆ BÁN THEO LOẠI", 350, 340);
        pieChartLoaiThuoc = createPieChart();
        cardLoaiThuoc.getChildren().add(pieChartLoaiThuoc);

        // 4. Cảnh báo (cột bên phải)
        VBox cardAlert = createAlertCard();

        row.getChildren().addAll(cardNguonDT, cardGioCaoDiem, cardLoaiThuoc);

        // Thêm hàng cảnh báo riêng
        root.getChildren().add(cardAlert);

        return row;
    }

    private VBox createChartCard(String title, double width, double height) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        card.setPrefWidth(width);
        card.setPrefHeight(height);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        titleLabel.setTextFill(Color.web("#1E293B"));

        card.getChildren().add(titleLabel);
        return card;
    }

    private VBox createAlertCard() {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        card.setPrefHeight(200);

        Label titleLabel = new Label("⚠️ CẢNH BÁO");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#DC2626"));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        alertBox = new VBox(8);
        alertBox.setPadding(new Insets(10, 0, 0, 0));
        scrollPane.setContent(alertBox);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        card.getChildren().addAll(titleLabel, scrollPane);
        return card;
    }

    private PieChart createPieChart() {
        PieChart chart = new PieChart();
        chart.setLegendVisible(true);
        chart.setPrefHeight(280);
        chart.setStyle("-fx-font-size: 11px;");
        return chart;
    }

    private LineChart<String, Number> createLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Giờ trong ngày");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số hóa đơn");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("");
        chart.setLegendVisible(false);
        chart.setPrefHeight(280);
        chart.setCreateSymbols(true);

        return chart;
    }

    private void loadData() {
        // Tính ngày bắt đầu theo bộ lọc
        LocalDate startDate = getStartDateFromFilter();

        try {
            loadInventoryStatus();
            loadRevenueAndInvoices(startDate);
            loadRevenueByCategory(startDate);
            loadSalesByMedicineType(startDate);
            loadPeakHours(startDate);
            loadAlerts();
        } catch (Exception e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private LocalDate getStartDateFromFilter() {
        String filter = timeFilterCombo.getValue();
        LocalDate now = LocalDate.now();

        return switch (filter) {
            case "7 ngày" -> now.minusDays(7);
            case "30 ngày" -> now.minusDays(30);
            case "90 ngày" -> now.minusDays(90);
            default -> now; // Hôm nay
        };
    }

    // ==================== LOAD DỮ LIỆU ====================

    private void loadInventoryStatus() {
        try {
            List<Thuoc> dsThuoc = thuocDAO.getAllTbThuoc();
            int total = dsThuoc.size();

            long conHang = dsThuoc.stream()
                .filter(t -> t.getTrangThaiTonKho() == TrangThaiTonKho.CON_HANG ||
                           t.getTrangThaiTonKho() == TrangThaiTonKho.TON_KHO)
                .count();

            long sapHet = dsThuoc.stream()
                .filter(t -> t.getTrangThaiTonKho() == TrangThaiTonKho.SAP_HET_HANG ||
                           t.getTrangThaiTonKho() == TrangThaiTonKho.HET_HANG)
                .count();

            double phanTramConHang = total > 0 ? (conHang * 100.0 / total) : 0;
            double phanTramSapHet = total > 0 ? (sapHet * 100.0 / total) : 0;

            System.out.println("[DEBUG] loadInventoryStatus: total=" + total + ", conHang=" + conHang + ", sapHet=" + sapHet);

            updateKPICardValue(lblTonKhoConHang,
                String.format("%.1f%%", phanTramConHang),
                String.format("%d/%d thuốc", conHang, total));

            updateKPICardValue(lblTonKhoSapHet,
                String.format("%.1f%%", phanTramSapHet),
                String.format("%d/%d thuốc", sapHet, total));

        } catch (Exception e) {
            System.err.println("[ERROR] loadInventoryStatus: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadRevenueAndInvoices(LocalDate startDate) {
        String sql = """
            SELECT 
                COUNT(DISTINCT maHoaDon) as soHD,
                COUNT(DISTINCT maKhachHang) as soKH,
                COALESCE(SUM(tongTien), 0) as doanhThu
            FROM HoaDon
            WHERE CAST(ngayLap AS DATE) >= ?
        """;

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int soHD = rs.getInt("soHD");
                int soKH = rs.getInt("soKH");
                BigDecimal doanhThu = rs.getBigDecimal("doanhThu");

                System.out.println("[DEBUG] loadRevenueAndInvoices: soHD=" + soHD + ", soKH=" + soKH + ", doanhThu=" + doanhThu);

                String doanhThuStr = String.format("%,.0f VNĐ", doanhThu.doubleValue());
                updateKPICardValue(lblDoanhThu, doanhThuStr, "Tăng 8% so với cùng kỳ");
                updateKPICardValue(lblSoHoaDon, String.valueOf(soHD), soKH + " khách hàng");
            }

        } catch (Exception e) {
            System.err.println("[ERROR] loadRevenueAndInvoices: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadRevenueByCategory(LocalDate startDate) {
        String sql = """
            SELECT 
                COALESCE(t.loaiThuoc, 'Khác') as loai,
                SUM(ct.thanhTien) as tongTien
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            JOIN Thuoc t ON ct.maThuoc = t.maThuoc
            WHERE CAST(hd.ngayLap AS DATE) >= ?
            GROUP BY t.loaiThuoc
        """;

        pieChartNguonDoanhThu.getData().clear();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String loai = rs.getString("loai");
                double tongTien = rs.getDouble("tongTien");

                PieChart.Data slice = new PieChart.Data(
                    loai + String.format(" (%.0fk)", tongTien / 1000),
                    tongTien
                );
                pieChartNguonDoanhThu.getData().add(slice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSalesByMedicineType(LocalDate startDate) {
        String sql = """
            SELECT 
                t.phanLoai,
                COUNT(ct.maThuoc) as soLuong
            FROM ChiTietHoaDon ct
            JOIN HoaDon hd ON ct.maHoaDon = hd.maHoaDon
            JOIN Thuoc t ON ct.maThuoc = t.maThuoc
            WHERE CAST(hd.ngayLap AS DATE) >= ?
            GROUP BY t.phanLoai
        """;

        pieChartLoaiThuoc.getData().clear();

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String phanLoai = rs.getString("phanLoai");
                int soLuong = rs.getInt("soLuong");

                PieChart.Data slice = new PieChart.Data(
                    phanLoai + " (" + soLuong + ")",
                    soLuong
                );
                pieChartLoaiThuoc.getData().add(slice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPeakHours(LocalDate startDate) {
        String sql = """
            SELECT 
                DATEPART(HOUR, ngayLap) as gio,
                COUNT(*) as soHD
            FROM HoaDon
            WHERE CAST(ngayLap AS DATE) >= ?
            GROUP BY DATEPART(HOUR, ngayLap)
            ORDER BY DATEPART(HOUR, ngayLap)
        """;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số hóa đơn");

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int gio = rs.getInt("gio");
                int soHD = rs.getInt("soHD");
                series.getData().add(new XYChart.Data<>(gio + ":00", soHD));
            }

            lineChartGioCaoDiem.getData().clear();
            lineChartGioCaoDiem.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAlerts() {
        alertBox.getChildren().clear();

        try {
            List<Thuoc> dsThuoc = thuocDAO.getAllTbThuoc();
            LocalDate now = LocalDate.now();

            // Cảnh báo hết hàng
            List<Thuoc> hetHang = dsThuoc.stream()
                .filter(t -> t.getTrangThaiTonKho() == TrangThaiTonKho.HET_HANG)
                .limit(5)
                .toList();

            for (Thuoc t : hetHang) {
                alertBox.getChildren().add(createAlertItem(
                    "🔴 HẾT HÀNG",
                    t.getTenThuoc() + " (Mã: " + t.getMaThuoc() + ")",
                    "#DC2626"
                ));
            }

            // Cảnh báo sắp hết
            List<Thuoc> sapHet = dsThuoc.stream()
                .filter(t -> t.getTrangThaiTonKho() == TrangThaiTonKho.SAP_HET_HANG)
                .limit(5)
                .toList();

            for (Thuoc t : sapHet) {
                alertBox.getChildren().add(createAlertItem(
                    "🟡 SẮP HẾT",
                    t.getTenThuoc() + " - Còn " + t.getSoLuong() + " " + t.getDonViBan(),
                    "#F59E0B"
                ));
            }

            // Cảnh báo hạn sử dụng
            List<Thuoc> sapHetHan = dsThuoc.stream()
                .filter(t -> t.getHanSuDung() != null &&
                           t.getHanSuDung().isBefore(now.plusMonths(1)) &&
                           t.getHanSuDung().isAfter(now))
                .limit(5)
                .toList();

            for (Thuoc t : sapHetHan) {
                alertBox.getChildren().add(createAlertItem(
                    "⏰ SẮP HẾT HẠN",
                    t.getTenThuoc() + " - HSD: " + t.getHanSuDung().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    "#EF4444"
                ));
            }

            if (alertBox.getChildren().isEmpty()) {
                Label noAlert = new Label("✅ Không có cảnh báo");
                noAlert.setFont(Font.font("Arial", 13));
                noAlert.setTextFill(Color.web("#10B981"));
                alertBox.getChildren().add(noAlert);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox createAlertItem(String type, String message, String color) {
        HBox item = new HBox(10);
        item.setPadding(new Insets(8));
        item.setStyle("-fx-background-color: " + color + "20; -fx-background-radius: 5;");
        item.setAlignment(Pos.CENTER_LEFT);

        Label lblType = new Label(type);
        lblType.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lblType.setTextFill(Color.web(color));
        lblType.setMinWidth(100);

        Label lblMessage = new Label(message);
        lblMessage.setFont(Font.font("Arial", 11));
        lblMessage.setTextFill(Color.web("#1E293B"));
        lblMessage.setWrapText(true);

        item.getChildren().addAll(lblType, lblMessage);
        return item;
    }

    private void updateKPICardValue(Label valueLabel, String value, String subtext) {
        if (valueLabel != null) {
            Platform.runLater(() -> {
                valueLabel.setText(value);
                Object subtextObj = valueLabel.getUserData();
                if (subtextObj instanceof Label) {
                    ((Label) subtextObj).setText(subtext);
                }
            });
        }
    }

    private void startAutoRefresh() {
        // Hủy scheduler cũ nếu có
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }

        // Tạo scheduler mới
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                Platform.runLater(this::loadData);
            } catch (Exception e) {
                System.err.println("Auto refresh error: " + e.getMessage());
            }
        }, 30, 30, TimeUnit.SECONDS);
    }

    public VBox getRoot() {
        return root;
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            try {
                scheduler.shutdownNow();
            } catch (Exception e) {
                System.err.println("Error shutting down scheduler: " + e.getMessage());
            }
        }
    }

    public void pause() {
        if (scheduler != null && !scheduler.isShutdown()) {
            try {
                scheduler.shutdown();
            } catch (Exception e) {
                System.err.println("Error pausing scheduler: " + e.getMessage());
            }
        }
    }

    public void resume() {
        startAutoRefresh();
    }
}

