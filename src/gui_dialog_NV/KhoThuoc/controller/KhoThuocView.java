package gui_dialog_NV.KhoThuoc.controller;

import gui.HeaderBar;
import gui.SidebarMenu;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class KhoThuocView {

    private final String tenDangNhap;
    private final String tenNhanVien;

    public KhoThuocView(String tenDangNhap) {
        this(tenDangNhap, tenDangNhap);
    }

    public KhoThuocView(String tenDangNhap, String tenNhanVien) {
        this.tenDangNhap = tenDangNhap;
        this.tenNhanVien = tenNhanVien != null && !tenNhanVien.trim().isEmpty() ? tenNhanVien : tenDangNhap;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Quản lý kho thuốc - Hiệu thuốc Thiện Lương");

        BorderPane root = new BorderPane();
        root.setLeft(new SidebarMenu(tenDangNhap, tenNhanVien, primaryStage::close));
        root.setTop(new HeaderBar(tenNhanVien));

        KhoThuocController controller = new KhoThuocController();
        root.setCenter(controller.getView());

        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public Node getContentPane() {
        KhoThuocController controller = new KhoThuocController();
        return controller.getView();
    }
}