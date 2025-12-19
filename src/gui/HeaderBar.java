// src/gui/HeaderBar.java
package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class HeaderBar extends HBox {

    private static final double SPACING = 15; // Khoảng cách đều nhau

    public HeaderBar(String tenNhanVien) {
        setPrefHeight(70);
        setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        setAlignment(Pos.CENTER_RIGHT);

        // === NGÀY (TRÁI) ===
        Label dateLabel = createDateLabel();

        // === PHẦN PHẢI: TÌM KIẾM + BELL + USER ===
        HBox rightSection = new HBox(SPACING);
        rightSection.setAlignment(Pos.CENTER_RIGHT);
        rightSection.setPadding(new Insets(0, 30, 0, 0)); // Cách lề phải 30px

        TextField search = createSearchField();
        Button bellBtn = createBellButton();
        HBox userBox = createUserBox(tenNhanVien);

        rightSection.getChildren().addAll(search, bellBtn, userBox);

        // === SPACER ĐỂ ĐẨY VỀ PHẢI ===
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(dateLabel, spacer, rightSection);
    }

    private Label createDateLabel() {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(new Locale("vi", "VN")));
        Label label = new Label(dateStr);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setStyle("-fx-padding: 0 0 0 30;"); // Cách lề trái 30px
        return label;
    }

    private TextField createSearchField() {
        TextField search = new TextField();
        search.setPromptText("Tìm kiếm...");
        search.setPrefWidth(300);
        search.setStyle("-fx-background-radius: 20; -fx-padding: 10; -fx-background-color: #F1F3F5;");
        return search;
    }

    private Button createBellButton() {
        Button bellBtn = new Button();
        bellBtn.setStyle("-fx-background-color: #007BFF; -fx-background-radius: 50%; -fx-pref-width: 40; -fx-pref-height: 40;");
        try {
            Image bellImg = new Image(getClass().getResourceAsStream("/img/icon_Bell.png"));
            ImageView iv = new ImageView(bellImg);
            iv.setFitWidth(20); iv.setFitHeight(20);
            bellBtn.setGraphic(iv);
        } catch (Exception e) {
            bellBtn.setText("Bell");
            bellBtn.setTextFill(Color.WHITE);
        }
        return bellBtn;
    }

    private HBox createUserBox(String tenNhanVien) {
        Node avatar = createAvatar();
        Label nameLabel = new Label(tenNhanVien);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        HBox userBox = new HBox(8, avatar, nameLabel);
        userBox.setAlignment(Pos.CENTER);
        return userBox;
    }

    private Node createAvatar() {
        try {
            Image img = new Image(getClass().getResourceAsStream("/img/NhanVien.jpg"));
            ImageView iv = new ImageView(img);
            iv.setFitWidth(32); iv.setFitHeight(32);
            iv.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);");
            return iv;
        } catch (Exception e) {
            Circle c = new Circle(16, Color.web("#007BFF"));
            c.setStroke(Color.WHITE); c.setStrokeWidth(2);
            return c;
        }
    }
}