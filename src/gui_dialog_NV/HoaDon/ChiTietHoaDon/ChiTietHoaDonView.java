package gui_dialog_NV.HoaDon.ChiTietHoaDon;

import entity.ChiTietHoaDon;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.math.BigDecimal;

public class ChiTietHoaDonView {

    private final TableView<ChiTietHoaDon> table;
    private final Label lblMaHoaDon;
    private final Label lblTongTien;
    private final Button btnDong;

    public ChiTietHoaDonView() {
        table = new TableView<>();
        lblMaHoaDon = new Label();
        lblTongTien = new Label("0 đ");
        btnDong = new Button("Đóng");

        initTable();
    }

    public Pane getContent() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setPrefWidth(750);
        root.setPrefHeight(450);
        root.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 10;
        """);

        /* ===== HEADER ===== */
        Label title = new Label("Chi tiết hóa đơn");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        lblMaHoaDon.setStyle("-fx-text-fill: #6C757D;");

        VBox header = new VBox(5, title, lblMaHoaDon);

        /* ===== FOOTER ===== */
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_RIGHT);

        Label lblTong = new Label("Tổng cộng:");
        lblTong.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        lblTongTien.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        footer.getChildren().addAll(lblTong, lblTongTien, btnDong);

        VBox.setVgrow(table, Priority.ALWAYS);

        root.getChildren().addAll(header, table, footer);
        return root;
    }

    /* ================= TABLE ================= */

    private void initTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("Không có chi tiết hóa đơn"));

        TableColumn<ChiTietHoaDon, String> colMaThuoc =
                new TableColumn<>("Mã thuốc");
        colMaThuoc.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(
                        c.getValue().getThuoc().getMaThuoc()
                )
        );

        TableColumn<ChiTietHoaDon, String> colTenThuoc =
                new TableColumn<>("Tên thuốc");
        colTenThuoc.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(
                        c.getValue().getThuoc().getTenThuoc()
                )
        );

        TableColumn<ChiTietHoaDon, String> colDonGia =
                new TableColumn<>("Đơn giá");
        colDonGia.setStyle("-fx-alignment: CENTER-RIGHT;");
        colDonGia.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(formatTien(c.getValue().getDonGia()))
        );

        TableColumn<ChiTietHoaDon, String> colSoLuong =
                new TableColumn<>("Số lượng");
        colSoLuong.setStyle("-fx-alignment: CENTER;");
        colSoLuong.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(
                        String.valueOf(c.getValue().getSoLuong())
                )
        );

        TableColumn<ChiTietHoaDon, String> colThanhTien =
                new TableColumn<>("Thành tiền");
        colThanhTien.setStyle("-fx-alignment: CENTER-RIGHT;");
        colThanhTien.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(
                        formatTien(c.getValue().getThanhTien())
                )
        );

        table.getColumns().addAll(
                colMaThuoc,
                colTenThuoc,
                colDonGia,
                colSoLuong,
                colThanhTien
        );
    }

    /* ================= PUBLIC API ================= */

    public TableView<ChiTietHoaDon> getTable() {
        return table;
    }

    public Button getBtnDong() {
        return btnDong;
    }

    public void setMaHoaDon(String maHD) {
        lblMaHoaDon.setText("Mã hóa đơn: " + maHD);
    }

    public void setTongTien(BigDecimal tong) {
        lblTongTien.setText(formatTien(tong));
    }

    private String formatTien(BigDecimal tien) {
        return tien == null ? "0 đ" : String.format("%,.0f đ", tien);
    }
}
