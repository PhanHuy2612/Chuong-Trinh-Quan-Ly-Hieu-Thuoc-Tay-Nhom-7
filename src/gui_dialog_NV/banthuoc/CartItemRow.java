// gui_dialog_NV/banthuoc/CartItemRow.java
package gui_dialog_NV.banthuoc;

import entity.Thuoc;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

public class CartItemRow extends HBox {
    public CartItemRow(Thuoc thuoc, int qty, Consumer<Integer> onQuantityChange) {
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-radius: 8;");
        setSpacing(15);

        Label name = new Label(thuoc.getTenThuoc() + " x");
        name.setFont(Font.font(15));
        name.setPrefWidth(180);

        Spinner<Integer> spinner = new Spinner<>(1, thuoc.getSoLuong(), qty);
        spinner.setPrefWidth(100);
        spinner.valueProperty().addListener((obs, old, nev) -> onQuantityChange.accept(nev));

        Label price = new Label(String.format("%,.0f đ", thuoc.getGiaBan() * qty));
        price.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        price.setTextFill(Color.web("#e74c3c"));

        // Cập nhật giá khi số lượng thay đổi
        spinner.valueProperty().addListener((obs, old, nev) ->
                price.setText(String.format("%,.0f đ", thuoc.getGiaBan() * nev))
        );

        Button remove = new Button("Xóa");
        remove.setStyle("-fx-text-fill: red; -fx-background-color: transparent;");
        remove.setOnAction(e -> onQuantityChange.accept(0));

        getChildren().addAll(name, spinner, price, remove);
        HBox.setHgrow(name, Priority.ALWAYS);
    }
}