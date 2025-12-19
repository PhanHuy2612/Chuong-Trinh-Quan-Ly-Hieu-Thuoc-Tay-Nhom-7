package gui_dialog_NV.KhoThuoc.controller;

import dao.*;
import enums.DonViTinh;
import enums.TrangThaiTonKho;
import gui_dialog_NV.KhoThuoc.dialog.EditThuocDialog;
import javafx.scene.text.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import entity.*;
import gui_dialog_NV.KhoThuoc.filter.ThuocFilter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;

public class KhoThuocController {

    private final Thuoc_DAO thuocDAO = new Thuoc_DAO();
    private final KhoHang_DAO khoDAO = new KhoHang_DAO();
    private final NhaCungCap_DAO nccDAO = new NhaCungCap_DAO();

    private final ObservableList<Thuoc> masterData = FXCollections.observableArrayList();
    private final FilteredList<Thuoc> filteredData = new FilteredList<>(masterData);
    private final SortedList<Thuoc> sortedData = new SortedList<>(filteredData);

    private ThuocFilter filter;

    private VBox view;
    private TableView<Thuoc> table;
    private TextField searchField;
    private ComboBox<String> khoCombo, nccCombo;

    private Label lblTong, lblCanNhap, lblHetHang, lblGiaTri;

    public KhoThuocController() {
        buildUI();
        loadData();
        setupFilter();
        setupStatsAutoUpdate();
    }

    public Node getView() {
        return view;
    }

    private void buildUI() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FAFBFC; -fx-spacing: 12;");

        // Top
        VBox topSection = new VBox(12);
        topSection.setStyle("-fx-padding: 20 28 12 28; -fx-background-color: transparent;");

        Label title = new Label("Quản lý kho thuốc");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitle = new Label("Theo dõi tồn kho, nhập hàng và cảnh báo hết hạn");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #666666;");

        HBox statsRow = createStatsRow();

        HBox controlBar = createControlBar();

        topSection.getChildren().addAll(title, subtitle, statsRow, controlBar);

        VBox bottom = new VBox(10);
        bottom.setStyle("-fx-background-color: white; -fx-padding: 16 20 20 20; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label tableTitle = new Label("Danh sách sản phẩm trong kho");
        tableTitle.setFont(Font.font("System", FontWeight.BOLD, 18));

        table = createTable();
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        table.setPlaceholder(new Label("Gõ tên thuốc, mã thuốc hoặc chọn kho để tìm kiếm..."));

        VBox.setVgrow(table, Priority.ALWAYS);
        bottom.getChildren().addAll(tableTitle, new Separator(), table);

        root.getChildren().addAll(topSection, bottom);
        VBox.setVgrow(bottom, Priority.ALWAYS);

        this.view = root;
    }

    private void loadData() {
        masterData.setAll(thuocDAO.getAllTbThuoc());

        if (khoCombo != null) {
            khoCombo.getItems().clear();
            khoCombo.getItems().add("Tất cả kho");
            khoDAO.getDanhSachMaKho().forEach(k -> {
                if (!khoCombo.getItems().contains(k)) khoCombo.getItems().add(k);
            });
            if (!khoCombo.getItems().contains(khoCombo.getValue())) khoCombo.setValue("Tất cả kho");
        }

        if (nccCombo != null) {
            nccCombo.getItems().clear();
            nccCombo.getItems().add("Tất cả nhà cung cấp");
            nccDAO.getAllNhaCungCap().forEach(ncc -> {
                String ma = ncc.getMaNCC();
                if (ma == null) ma = ncc.getMaNCC();
                if (ma == null) return;
                if (!nccCombo.getItems().contains(ma)) nccCombo.getItems().add(ma);
            });
            if (!nccCombo.getItems().contains(nccCombo.getValue())) nccCombo.setValue("Tất cả nhà cung cấp");
        }

        updateStats();
    }

    private HBox createStatsRow() {
        lblTong = new Label("0"); lblTong.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblCanNhap = new Label("0"); lblCanNhap.setFont(Font.font(14));
        lblHetHang = new Label("0"); lblHetHang.setFont(Font.font(14));
        lblGiaTri = new Label("0 đ"); lblGiaTri.setFont(Font.font(14));

        VBox c1 = new VBox(4, lblTong, new Label("Tổng sản phẩm"));
        VBox c2 = new VBox(4, lblCanNhap, new Label("Cần nhập thêm"));
        VBox c3 = new VBox(4, lblHetHang, new Label("Hết hàng"));
        VBox c4 = new VBox(4, lblGiaTri, new Label("Giá trị tồn kho"));

        c1.setStyle("-fx-padding: 10; -fx-background-color: #E3F2FD; -fx-background-radius: 8;");
        c2.setStyle("-fx-padding: 10; -fx-background-color: #FFF3E0; -fx-background-radius: 8;");
        c3.setStyle("-fx-padding: 10; -fx-background-color: #FFEBEE; -fx-background-radius: 8;");
        c4.setStyle("-fx-padding: 10; -fx-background-color: #E8F5E9; -fx-background-radius: 8;");

        HBox row = new HBox(12, c1, c2, c3, c4);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox createControlBar() {
        HBox bar = new HBox(15);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-padding: 12 0;");

        khoCombo = new ComboBox<>();
        khoCombo.getItems().add("Tất cả kho");
        khoCombo.getItems().addAll(khoDAO.getDanhSachMaKho());
        khoCombo.setValue("Tất cả kho");
        khoCombo.setPrefWidth(180);

        nccCombo = new ComboBox<>();
        nccCombo.getItems().add("Tất cả nhà cung cấp");
        nccDAO.getAllNhaCungCap().forEach(ncc -> nccCombo.getItems().add(ncc.getTenNCC()));
        nccCombo.setValue("Tất cả nhà cung cấp");
        nccCombo.setPrefWidth(240);

        searchField = new TextField();
        searchField.setPromptText("Tìm kiếm tên thuốc, mã thuốc... (gõ * để hiện tất cả)");
        searchField.setPrefWidth(500);
        searchField.setPrefHeight(46);
        searchField.setStyle("-fx-font-size: 14px; -fx-background-radius: 12;");


        Button excelBtn = new Button("Nhập từ Excel");
        excelBtn.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-padding: 10 32; -fx-background-radius: 12;");
        excelBtn.setOnAction(e -> importExcel());


        HBox buttons = new HBox(5,excelBtn);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bar.getChildren().addAll(
                khoCombo,
                nccCombo,
                searchField,
                spacer,
                buttons
        );
        return bar;
    }
    private void importExcel() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Chọn file Excel nhập dữ liệu thuốc");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fc.showOpenDialog(view.getScene().getWindow());
        if (file == null) return;

        int success = 0, fail = 0;

        try (FileInputStream fis = new FileInputStream(file);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row r = sheet.getRow(i);
                if (r == null) continue;

                try {
                    Thuoc t = new Thuoc();

                    t.setMaThuoc(getStringCell(r, 0).trim());
                    t.setGiaBan(getNumericCell(r, 1));
                    t.setGiaNhap(getNumericCell(r, 2));

                    // === SỬA LỖI NGÀY TẠI ĐÂY ===
                    Cell cellHSD = r.getCell(3);
                    LocalDate hanSuDung = null;
                    if (cellHSD != null) {
                        if (cellHSD.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cellHSD)) {
                            hanSuDung = cellHSD.getLocalDateTimeCellValue().toLocalDate();
                        } else {
                            String str = cellHSD.getStringCellValue().trim();
                            if (str.contains("T")) str = str.substring(0, 10);
                            if (!str.isEmpty()) hanSuDung = LocalDate.parse(str);
                        }
                    }
                    if (hanSuDung == null) hanSuDung = LocalDate.now().plusYears(2);
                    t.setHanSuDung(hanSuDung);
                    // === HẾT SỬA ===

                    t.setTenThuoc(getStringCell(r, 4).trim());
                    t.setMaKho(getStringCell(r, 5).trim());
                    t.setMaNhaCungCap(getStringCell(r, 6).trim());

                    // Đơn vị nhập kho
                    String dvNhapRaw = getStringCell(r, 7).trim();
                    DonViTinh dvNhap = switch (dvNhapRaw.toUpperCase()) {
                        case "HỘP", "HOP" -> DonViTinh.HOP;
                        case "VIÊN", "VIEN" -> DonViTinh.VIEN;
                        case "TUÝP", "TUYP" -> DonViTinh.TUYP;
                        case "CHAI" -> DonViTinh.CHAI;
                        case "GÓI", "GOI" -> DonViTinh.GOI;
                        default -> DonViTinh.HOP;
                    };
                    t.setDonViNhapKho(dvNhap);

                    t.setSoLuong((int) getNumericCell(r, 8));
                    t.setTonKhoToiThieu((int) getNumericCell(r, 10));
                    t.setTonKhoToiDa((int) getNumericCell(r, 11));

                    t.setTrangThaiTonKho(TrangThaiTonKho.fromString(getStringCell(r, 9).trim()));
                    t.setLoaiThuoc(getStringCell(r, 12).trim());

                    String dvBanRaw = getStringCell(r, 13).trim();
                    DonViTinh dvBan = switch (dvBanRaw.toUpperCase()) {
                        case "HỘP", "HOP" -> DonViTinh.HOP;
                        case "VIÊN", "VIEN" -> DonViTinh.VIEN;
                        case "TUÝP", "TUYP" -> DonViTinh.TUYP;
                        case "CHAI" -> DonViTinh.CHAI;
                        case "GÓI", "GOI" -> DonViTinh.GOI;
                        default -> DonViTinh.VIEN;
                    };
                    t.setDonViBan(dvBan);

                    t.setTiLeDonVi(getStringCell(r, 14).trim());
                    String maLoai = getStringCell(r, 15).trim();
                    t.setMaLoai(maLoai.isEmpty() ? null : maLoai);

                    if (thuocDAO.themThuoc(t)) {
                        masterData.add(t);
                        success++;
                    } else {
                        fail++;
                        System.err.println("DAO từ chối thêm - Mã: " + t.getMaThuoc());
                    }

                } catch (Exception ex) {
                    fail++;
                    System.err.println("Lỗi dòng Excel " + (i + 1) + " (mã: " + getStringCell(r, 0) + "): " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            updateStats();
            applyFilters();

            showAlert("Nhập Excel hoàn tất!\nThành công: " + success + "\nThất bại: " + fail);

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Lỗi đọc file: " + ex.getMessage());
        }
    }
    private LocalDate getDateCell(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        if (cell.getCellType() == CellType.STRING) {
            try {
                return LocalDate.parse(cell.getStringCellValue());
            } catch (Exception ignored) {}
        }
        return null;
    }

    private void applyFilters() {
        String search = searchField.getText() == null ? "" : searchField.getText().trim();
        String kho = khoCombo.getValue();
        String ncc = nccCombo.getValue();
        if (search.equals("*")) {
            filteredData.setPredicate(t -> true);
            return;
        }
        boolean CoDK =
                !search.isEmpty() ||
                        !("Tất cả kho".equals(kho)) ||
                        !("Tất cả nhà cung cấp".equals(ncc));

        if (!CoDK) {
            filteredData.setPredicate(t -> false);
            return;
        }

        filteredData.setPredicate(t -> {
            boolean matchSearch = search.isEmpty()
                    || t.getTenThuoc().toLowerCase().contains(search.toLowerCase())
                    || t.getMaThuoc().toLowerCase().contains(search.toLowerCase());

            boolean matchKho = "Tất cả kho".equals(kho) || t.getMaKho().equalsIgnoreCase(kho);
            boolean matchNcc = "Tất cả nhà cung cấp".equals(ncc) || t.getMaNhaCungCap().equalsIgnoreCase(ncc);

            return matchSearch && matchKho && matchNcc;
        });
    }

    private String getStringCell(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    private double getNumericCell(Row row, int index) {
        Cell cell = row.getCell(index);

        if (cell == null) return 0;

        switch (cell.getCellType()) {

            case NUMERIC:
                return cell.getNumericCellValue();

            case STRING:
                String txt = cell.getStringCellValue().trim();
                if (txt.isEmpty()) return 0;
                try {
                    return Double.parseDouble(txt);
                } catch (Exception e) {
                    return 0;
                }

            case BOOLEAN:
                return cell.getBooleanCellValue() ? 1 : 0;

            case FORMULA:
                try {
                    return cell.getNumericCellValue();
                } catch (Exception e) {
                    try {
                        return Double.parseDouble(cell.getStringCellValue());
                    } catch (Exception ignore) {
                        return 0;
                    }
                }

            default:
                return 0;
        }
    }



    private void refreshStatistics() {
        view.getChildren().set(0, createStatsRow());
    }


    private TableView<Thuoc> createTable() {
        TableView<Thuoc> tv = new TableView<>();
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Thuoc, String> colTen = new TableColumn<>("Tên thuốc");
        colTen.setMinWidth(180);
        colTen.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTenThuoc() + "\n" + c.getValue().getMaThuoc()));
        colTen.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); return; }
                String[] p = item.split("\n", 2);
                Label a = new Label(p[0]); a.setStyle("-fx-font-weight:bold;");
                Label b = new Label(p.length>1?p[1]:""); b.setStyle("-fx-text-fill:#666; -fx-font-size:11;");
                setGraphic(new VBox(2, a, b));
            }
        });

        TableColumn<Thuoc, String> colDonViNhap = new TableColumn<>("Đơn vị nhập");
        colDonViNhap.setCellValueFactory(c -> {
            var d = c.getValue().getDonViNhapKho();
            return new SimpleStringProperty(d != null ? d.getMoTa() : "—");
        });

        TableColumn<Thuoc, String> colDonViBan = new TableColumn<>("Đơn vị bán");
        colDonViBan.setCellValueFactory(c -> {
            var d = c.getValue().getDonViBan();
            return new SimpleStringProperty(d != null ? d.getMoTa() : "—");
        });

        TableColumn<Thuoc, String> colTyLe = new TableColumn<>("Tỉ lệ");
        colTyLe.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTiLeDonVi() == null || c.getValue().getTiLeDonVi().isBlank() ? "1:1" : c.getValue().getTiLeDonVi()
        ));

        TableColumn<Thuoc, String> colGiaNhap = new TableColumn<>("Giá nhập");
        colGiaNhap.setCellValueFactory(c -> new SimpleStringProperty(String.format("%,.0f đ", c.getValue().getGiaNhap())));

        TableColumn<Thuoc, String> colGiaBan = new TableColumn<>("Giá bán");
        colGiaBan.setCellValueFactory(c -> new SimpleStringProperty(String.format("%,.0f đ", c.getValue().getGiaBan())));

        TableColumn<Thuoc, Integer> colSoLuong = new TableColumn<>("Số lượng");
        colSoLuong.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getSoLuong()).asObject());
        colSoLuong.setCellFactory(tc -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(String.valueOf(item));
                try {
                    Thuoc t = getTableView().getItems().get(getIndex());
                    if (t.getSoLuong() == 0)
                        setStyle("-fx-font-weight:bold; -fx-text-fill:#D32F2F; -fx-background-color:#FFEBEE;");
                    else if (t.getSoLuong() <= t.getTonKhoToiThieu())
                        setStyle("-fx-font-weight:bold; -fx-text-fill:#E65100; -fx-background-color:#FFF3E0;");
                    else if (t.getSoLuong() >= t.getTonKhoToiDa())
                        setStyle("-fx-font-weight:bold; -fx-text-fill:#2E7D32; -fx-background-color:#E8F5E9;");
                    else
                        setStyle("-fx-font-weight:bold; -fx-text-fill:#1565C0;");
                } catch (Exception ex) {
                    setStyle("");
                }
            }
        });

        TableColumn<Thuoc, String> colTonMin = new TableColumn<>("Tồn tối thiểu");
        colTonMin.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getTonKhoToiThieu())));

        TableColumn<Thuoc, String> colTonMax = new TableColumn<>("Tồn tối đa");
        colTonMax.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getTonKhoToiDa())));

        TableColumn<Thuoc, Void> colAction = new TableColumn<>("Thao tác");
        colAction.setMinWidth(160);
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button edit = new Button("Sửa");
            private final Button del = new Button("Xóa");
            {
                edit.setOnAction(e -> {
                    Thuoc t = getTableView().getItems().get(getIndex());

                    EditThuocDialog dialog = new EditThuocDialog();
                    dialog.show(t);

                    if (dialog.isSaved()) {
                        if (thuocDAO.capNhatThuoc(t)) {
                            table.refresh();
                            showAlert("Cập nhật thành công!");
                        } else {
                            showAlert("Cập nhật thất bại!");
                        }
                    }
                });

                del.setOnAction(e -> {
                    Thuoc t = getTableView().getItems().get(getIndex());
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Xóa thuốc: " + t.getTenThuoc() + "?", ButtonType.YES, ButtonType.NO);
                    a.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {
                            if (thuocDAO.xoaThuoc(t.getMaThuoc())) {
                                masterData.remove(t);
                                table.refresh();
                                showAlert("Xóa thành công!");
                            } else {
                                showAlert("Xóa thất bại!");
                            }

                        }
                    });
                });
                edit.setStyle("-fx-background-color:#007BFF; -fx-text-fill:white;");
                del.setStyle("-fx-background-color:#DC3545; -fx-text-fill:white;");
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(new HBox(8, edit, del));
            }
        });

        tv.getColumns().addAll(colTen, colDonViNhap, colDonViBan, colTyLe, colGiaNhap, colGiaBan, colSoLuong, colTonMin, colTonMax, colAction);
        return tv;
    }

    private void setupFilter() {
        filter = new ThuocFilter(filteredData);

        searchField.textProperty().addListener((obs, ov, nv) -> applyFilters());
        khoCombo.valueProperty().addListener((obs, ov, nv) -> applyFilters());
        nccCombo.valueProperty().addListener((obs, ov, nv) -> applyFilters());

        filteredData.setPredicate(t -> false);
    }


    private void setupStatsAutoUpdate() {
        masterData.addListener((ListChangeListener<Thuoc>) c -> updateStats());
        filteredData.addListener((ListChangeListener<Thuoc>) c -> updateStats());
        updateStats();
    }

    private void updateStats() {
        int total = masterData.size();
        long hetHang = masterData.stream().filter(t -> t.getSoLuong() == 0).count();
        long canNhap = masterData.stream().filter(t -> t.getSoLuong() <= t.getTonKhoToiThieu()).count();
        double giaTri = masterData.stream().mapToDouble(t -> t.getGiaBan() * t.getSoLuong()).sum();

        javafx.application.Platform.runLater(() -> {
            lblTong.setText(String.valueOf(total));
            lblHetHang.setText(String.valueOf(hetHang));
            lblCanNhap.setText(String.valueOf(canNhap));
            lblGiaTri.setText(String.format("%,.0f đ", giaTri));
        });
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

}
