package gui_dialog_NV.banthuoc;

import entity.Thuoc;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class MedicineRow extends HBox {

    public MedicineRow(Thuoc thuoc, Runnable onAddToCart) {
        setPadding(new Insets(15));
        setSpacing(20);
        setAlignment(Pos.CENTER_LEFT);
        setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 12; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");

        VBox info = new VBox(6);
        Label tenThuoc = new Label(thuoc.getTenThuoc());
        tenThuoc.setFont(Font.font("System", FontWeight.BOLD, 18));

        String trangThai = thuoc.getSoLuong() > 0 ? "Còn hàng" : "Hết hàng";
        String mau = thuoc.getSoLuong() > 0 ? "#27ae60" : "#e74c3c";

        Label chiTiet = new Label(String.format("Mã: %s | SL: %,d | Giá: %, .0f đ | Loại: %s | %s",
                thuoc.getMaThuoc(),
                thuoc.getSoLuong(),
                thuoc.getGiaBan(),
                thuoc.getLoaiThuoc() != null ? thuoc.getLoaiThuoc() : "N/A",
                trangThai));
        chiTiet.setStyle("-fx-text-fill: " + mau + "; -fx-font-size: 14px;");

        info.getChildren().addAll(tenThuoc, chiTiet);

        Button btnAdd = new Button("Thêm vào giỏ");
        btnAdd.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30; -fx-background-radius: 10;");
        btnAdd.setOnAction(e -> onAddToCart.run());

        getChildren().addAll(info, btnAdd);
        HBox.setHgrow(info, Priority.ALWAYS);
    }
}