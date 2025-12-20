package gui_dialog_NV.banthuoc.hoadon;

import entity.KhachHang;
import enums.LoaiKhachHang;
import enums.PhuongThucThanhToan;  // Thêm import để trả về phương thức thanh toán
import gui_dialog_NV.banthuoc.CartManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class HoaDonDialog extends Stage {

    private boolean confirmed = false;
    private PhuongThucThanhToan phuongThucChon = PhuongThucThanhToan.TIEN_MAT; // Mặc định tiền mặt

    public boolean isConfirmed() {
        return confirmed;
    }

    public PhuongThucThanhToan getPhuongThucThanhToan() {
        return phuongThucChon;
    }

    private final KhachHang khachHang;
    private final List<CartManager.CartItem> items;
    private final double tongTienHang;
    private final double chietKhauKhuyenMai;
    private final double giamDoiThuong;
    private final double tongThanhToan;
    private final String soHD;
    private final String ghiChuDoiThuong;
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public HoaDonDialog(KhachHang khachHang, List<CartManager.CartItem> items,
                        double tongTienHang, double chietKhauKhuyenMai, double giamDoiThuong,
                        double tongThanhToan, String soHD, String ghiChuDoiThuong) {
        this.khachHang = khachHang;
        this.items = items;
        this.tongTienHang = tongTienHang;
        this.chietKhauKhuyenMai = chietKhauKhuyenMai;
        this.giamDoiThuong = giamDoiThuong;
        this.tongThanhToan = tongThanhToan;
        this.soHD = soHD;
        this.ghiChuDoiThuong = ghiChuDoiThuong;
        initUI();
    }

    private void initUI() {
        setTitle("Hóa đơn bán hàng - Nhà thuốc Thiện Lương");
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);

        ScrollPane scrollPane = new ScrollPane(createContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white;");

        VBox main = new VBox(scrollPane);
        main.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(main, 750, 950);
        setScene(scene);
    }
    public double getTongTienGiam() {
        return chietKhauKhuyenMai + giamDoiThuong;
    }

    private VBox createContent() {
        VBox content = new VBox(12);
        content.setPadding(new Insets(30, 50, 30, 50));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: white;");

        // ===== HEADER =====
        Text tenNhaThuoc = new Text("Nhà thuốc Thiện Lương");
        tenNhaThuoc.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        Text diaChi = new Text("29/41A Nguyễn Văn Khối, P.11, Gò Vấp, TP.HCM");
        diaChi.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));

        Text hotline = new Text("Hotline: 0372816614");
        hotline.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));

        VBox header = new VBox(5, tenNhaThuoc, diaChi, hotline);
        header.setAlignment(Pos.CENTER);

        // ===== TIÊU ĐỀ HÓA ĐƠN =====
        Text hoaDonTitle = new Text("HÓA ĐƠN BÁN HÀNG");
        hoaDonTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Text soHDText = new Text("Số HD: " + soHD);
        soHDText.setFont(Font.font("Arial", 14));

        String ngayGio = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Text ngayText = new Text("Ngày: " + ngayGio);
        ngayText.setFont(Font.font("Arial", 14));

        VBox invoiceHeader = new VBox(6, hoaDonTitle, soHDText, ngayText);
        invoiceHeader.setAlignment(Pos.CENTER);

        // ===== THÔNG TIN KHÁCH HÀNG & THÂN THIẾT =====
        VBox customerBox = new VBox(4);
        customerBox.setAlignment(Pos.CENTER_LEFT);

        String tenKH = (khachHang == null || khachHang.getTenKH() == null || khachHang.getTenKH().isEmpty())
                ? "Khách lẻ" : khachHang.getTenKH();
        String sdt = (khachHang == null || khachHang.getSoDienThoai() == null || khachHang.getSoDienThoai().isEmpty())
                ? "" : khachHang.getSoDienThoai();

        Text khText = new Text("Khách hàng: " + tenKH);
        khText.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        Text sdtText = new Text("Số điện thoại: " + sdt);
        sdtText.setFont(Font.font("Arial", 13));

        customerBox.getChildren().addAll(khText, sdtText);

        VBox customerSection = new VBox(6);
        customerSection.getChildren().add(customerBox);

        if (khachHang != null) {
            Text thanThiet = new Text("Khách hàng thân thiết: " + khachHang.getLoaiKhachHang());
            thanThiet.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            thanThiet.setFill(khachHang.getLoaiKhachHang() == LoaiKhachHang.VIP ?
                    Color.web("#27ae60") : Color.web("#3498db"));
            customerSection.getChildren().add(thanThiet);

            if (ghiChuDoiThuong != null) {
                Text doiThuong = new Text(ghiChuDoiThuong);
                doiThuong.setFont(Font.font("Arial", FontWeight.MEDIUM, 13));
                doiThuong.setFill(Color.web("#e67e22"));
                customerSection.getChildren().add(doiThuong);
            }
        }

        // ===== BẢNG THUỐC =====
        TableView<CartManager.CartItem> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getItems().addAll(items);

        TableColumn<CartManager.CartItem, String> colSTT = new TableColumn<>("STT");
        colSTT.setPrefWidth(50);
        colSTT.setCellValueFactory(c -> {
            int index = table.getItems().indexOf(c.getValue()) + 1;
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(index));
        });

        TableColumn<CartManager.CartItem, String> colTen = new TableColumn<>("Tên thuốc");
        colTen.setPrefWidth(300);
        colTen.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getThuoc().getTenThuoc()));

        TableColumn<CartManager.CartItem, String> colDonGia = new TableColumn<>("Đơn giá");
        colDonGia.setPrefWidth(100);
        colDonGia.setStyle("-fx-alignment: CENTER-RIGHT;");
        colDonGia.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(nf.format(c.getValue().getThuoc().getGiaBan())));

        TableColumn<CartManager.CartItem, Integer> colSL = new TableColumn<>("SL");
        colSL.setPrefWidth(60);
        colSL.setStyle("-fx-alignment: CENTER;");
        colSL.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getQuantity()));

        TableColumn<CartManager.CartItem, String> colThanhTien = new TableColumn<>("Thành tiền");
        colThanhTien.setPrefWidth(120);
        colThanhTien.setStyle("-fx-alignment: CENTER-RIGHT;");
        colThanhTien.setCellValueFactory(c -> {
            double tt = c.getValue().getThuoc().getGiaBan() * c.getValue().getQuantity();
            return new javafx.beans.property.SimpleStringProperty(nf.format(tt));
        });

        table.getColumns().addAll(colSTT, colTen, colDonGia, colSL, colThanhTien);

        // ===== TỔNG KẾT =====
        GridPane summary = new GridPane();
        summary.setHgap(30);
        summary.setVgap(10);
        summary.setAlignment(Pos.CENTER_RIGHT);

        int row = 0;

        summary.add(new Text("Tổng tiền hàng:"), 0, row);
        summary.add(new Text(nf.format(tongTienHang)), 1, row++);

        if (chietKhauKhuyenMai > 0) {
            summary.add(new Text("Chiết khấu khuyến mãi:"), 0, row);
            summary.add(new Text("-" + nf.format(chietKhauKhuyenMai)), 1, row++);
        }

        if (giamDoiThuong > 0) {
            Text giamDoiThuongText = new Text("Giảm đổi điểm khách thân thiết:");
            giamDoiThuongText.setFill(Color.web("#e67e22"));
            summary.add(giamDoiThuongText, 0, row);
            summary.add(new Text("-" + nf.format(giamDoiThuong)), 1, row++);
        }

        Text tongLabel = new Text("Tổng thanh toán:");
        tongLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        summary.add(tongLabel, 0, row);

        Text tongValue = new Text(nf.format(tongThanhToan));
        tongValue.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        tongValue.setStyle("-fx-fill: #DC2626;");
        summary.add(tongValue, 1, row);

        // ===== BẰNG CHỮ =====
        Text bangChu = new Text("Bằng chữ: " + docSo(tongThanhToan) + " đồng");
        bangChu.setFont(Font.font("Arial", FontWeight.MEDIUM, 13));
        bangChu.setWrappingWidth(600);
        bangChu.setTextAlignment(TextAlignment.CENTER);

        // ===== PHẦN THANH TOÁN =====
        VBox paymentSection = createPaymentSection();

        // ===== CẢM ƠN =====
        Text camOn = new Text("Cảm ơn quý khách đã mua hàng!\nHẹn gặp lại quý khách!");
        camOn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        camOn.setTextAlignment(TextAlignment.CENTER);

        VBox footer = new VBox(15, summary, bangChu, paymentSection, camOn);
        footer.setAlignment(Pos.CENTER);

        content.getChildren().addAll(
                header,
                new Separator(),
                invoiceHeader,
                new Separator(),
                customerSection,
                new Separator(),
                table,
                new Separator(),
                footer
        );

        return content;
    }

    private VBox createPaymentSection() {
        VBox paymentBox = new VBox(12);
        paymentBox.setAlignment(Pos.CENTER);
        paymentBox.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-padding: 20; -fx-background-radius: 10; -fx-border-radius: 10;");

        Label lblPaymentMethod = new Label("Phương thức thanh toán:");
        lblPaymentMethod.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ToggleGroup paymentGroup = new ToggleGroup();

        RadioButton rbTienMat = new RadioButton("Tiền mặt");
        rbTienMat.setToggleGroup(paymentGroup);
        rbTienMat.setSelected(true);
        rbTienMat.setUserData(PhuongThucThanhToan.TIEN_MAT);

        RadioButton rbChuyenKhoan = new RadioButton("Chuyển khoản");
        rbChuyenKhoan.setToggleGroup(paymentGroup);
        rbChuyenKhoan.setUserData(PhuongThucThanhToan.CHUYEN_KHOAN);

        // Cập nhật phương thức khi chọn
        paymentGroup.selectedToggleProperty().addListener((obs, old, nev) -> {
            if (nev != null) {
                phuongThucChon = (PhuongThucThanhToan) nev.getUserData();
            }
        });

        HBox methods = new HBox(30, rbTienMat, rbChuyenKhoan);
        methods.setAlignment(Pos.CENTER);

        // === TIỀN MẶT ===
        VBox tienMatBox = new VBox(10);

        Label lblTienKhachDua = new Label("Tiền khách đưa:");
        TextField txtTienKhachDua = new TextField();
        txtTienKhachDua.setPromptText("Nhập số tiền khách đưa");
        txtTienKhachDua.setPrefWidth(250);

        HBox quickButtons = new HBox(10);
        double[] quickValues = {
                tongThanhToan,
                Math.ceil(tongThanhToan / 100000) * 100000,
                tongThanhToan + 100000,
                tongThanhToan + 200000,
                500000,
                1000000
        };
        for (double val : quickValues) {
            if (val >= tongThanhToan) {
                Button btn = new Button(nf.format(val));
                btn.setOnAction(e -> txtTienKhachDua.setText(String.valueOf((long) val)));
                quickButtons.getChildren().add(btn);
            }
        }

        Label lblTienThoi = new Label("Tiền thối lại:");
        Text txtTienThoi = new Text("0 đ");
        txtTienThoi.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        txtTienThoi.setFill(Color.web("#27ae60"));

        SimpleDoubleProperty tienKhachDuaProp = new SimpleDoubleProperty(0);
        txtTienKhachDua.textProperty().addListener((obs, old, nev) -> {
            try {
                String clean = nev.replaceAll("[^\\d]", "");
                tienKhachDuaProp.set(clean.isEmpty() ? 0 : Double.parseDouble(clean));
            } catch (Exception ex) {
                tienKhachDuaProp.set(0);
            }
        });

        txtTienThoi.textProperty().bind(Bindings.createStringBinding(() -> {
            double thoi = tienKhachDuaProp.get() - tongThanhToan;
            return nf.format(thoi < 0 ? 0 : thoi) + " đ";
        }, tienKhachDuaProp));

        tienMatBox.getChildren().addAll(lblTienKhachDua, txtTienKhachDua, quickButtons, lblTienThoi, txtTienThoi);

        // === CHUYỂN KHOẢN ===
        VBox chuyenKhoanBox = new VBox(15);
        Label lblQR = new Label("Quét mã QR để thanh toán:");
        lblQR.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));

        ImageView qrImage = new ImageView("https://api.qrserver.com/v1/create-qr-code/?data=" + soHD + "&size=220x220");
        qrImage.setFitWidth(220);
        qrImage.setFitHeight(220);

        chuyenKhoanBox.getChildren().addAll(lblQR, qrImage);

        // Ẩn/hiện
        tienMatBox.visibleProperty().bind(rbTienMat.selectedProperty());
        tienMatBox.managedProperty().bind(rbTienMat.selectedProperty());
        chuyenKhoanBox.visibleProperty().bind(rbChuyenKhoan.selectedProperty());
        chuyenKhoanBox.managedProperty().bind(rbChuyenKhoan.selectedProperty());

        // Nút xác nhận / hủy
        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(20, 0, 0, 0));

        Button btnXacNhan = new Button("Xác nhận thanh toán");
        btnXacNhan.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 16; -fx-padding: 12 30; -fx-background-radius: 8;");
        btnXacNhan.setOnAction(e -> {
            confirmed = true;
            close();
        });

        Button btnHuy = new Button("Hủy");
        btnHuy.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 16; -fx-padding: 12 30; -fx-background-radius: 8;");
        btnHuy.setOnAction(e -> {
            confirmed = false;
            close();
        });

        buttons.getChildren().addAll(btnXacNhan, btnHuy);

        paymentBox.getChildren().addAll(lblPaymentMethod, methods, tienMatBox, chuyenKhoanBox, buttons);
        return paymentBox;
    }

    private String docSo(double amount) {
        if (amount == 0) return "Không";

        long longVal = (long) amount;
        String[] donvi = {"", "nghìn", "triệu", "tỷ"};
        String[] so = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};

        StringBuilder result = new StringBuilder();
        int i = 0;
        do {
            long part = longVal % 1000;
            if (part != 0) {
                String str = docBaSo(part, so);
                if (!str.isEmpty()) {
                    result.insert(0, donvi[i] + " ");
                    result.insert(0, str);
                }
            }
            longVal /= 1000;
            i++;
        } while (longVal > 0);

        return result.toString().trim().substring(0, 1).toUpperCase() + result.toString().trim().substring(1);
    }

    private String docBaSo(long num, String[] so) {
        StringBuilder sb = new StringBuilder();
        int tram = (int) (num / 100);
        int chuc = (int) ((num % 100) / 10);
        int donvi = (int) (num % 10);

        if (tram > 0) sb.append(so[tram]).append(" trăm ");
        if (chuc > 1) {
            sb.append(so[chuc]).append(" mươi ");
            if (donvi == 1) sb.append("mốt ");
            else if (donvi > 0) sb.append(so[donvi]);
        } else if (chuc == 1) {
            sb.append("mười ");
            if (donvi == 5) sb.append("lăm ");
            else if (donvi > 0) sb.append(so[donvi]);
        } else if (donvi > 0) {
            sb.append(so[donvi]);
        }
        return sb.toString().trim();
    }
}