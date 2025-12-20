package gui_dialog_NV.banthuoc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BanThuocNV_App extends Application {
    private String tenDangNhap = "0372816614";

    @Override
    public void start(Stage stage) {
        BanThuocNV_Controller controller = new BanThuocNV_Controller(tenDangNhap);
        Scene scene = new Scene(controller.getView().getRoot(), 1400, 800);
        stage.setTitle("Bán thuốc - Nhân viên: " + tenDangNhap);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}