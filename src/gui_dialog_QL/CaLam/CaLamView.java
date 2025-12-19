package gui_dialog_QL.CaLam;

import entity.CaLam;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CaLamView {

    private VBox root;
    private TableView<CaLam> table;

    private VBox lblTongCa, lblCaSapToi, lblNVPhanCong, lblTongNV;
    private Button btnTaoCa;

    public CaLamView() {
        initUI();
    }

    private void initUI() {
        root = new VBox(20);
        root.setStyle("-fx-padding: 20;");

        Label title = new Label("Quản lý ca làm");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold;");

        Label subtitle = new Label("Tạo và quản lý lịch làm việc cho nhân viên");
        subtitle.setStyle("-fx-text-fill: #6c757d;");

        HBox stats = new HBox(20);

        lblTongCa = createStatCard("Tổng ca làm");
        lblCaSapToi = createStatCard("Ca sắp tới");
        lblNVPhanCong = createStatCard("NV được phân công");
        lblTongNV = createStatCard("Nhân viên");

        stats.getChildren().addAll(lblTongCa, lblCaSapToi, lblNVPhanCong, lblTongNV);

        btnTaoCa = new Button("+ Tạo ca làm mới");
        btnTaoCa.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white;");

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        root.getChildren().addAll(title, subtitle, stats, btnTaoCa, table);
    }

    private VBox createStatCard(String title) {
        VBox box = new VBox(5);
        Label value = new Label("0");
        value.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        Label lbl = new Label(title);
        box.getChildren().addAll(value, lbl);
        return box;
    }

    /* ===== Getter để Controller dùng ===== */

    public VBox getRoot() { return root; }
    public TableView<CaLam> getTable() { return table; }
    public Button getBtnTaoCa() { return btnTaoCa; }

    public VBox getLblTongCa() { return lblTongCa; }
    public VBox getLblCaSapToi() { return lblCaSapToi; }
    public VBox getLblNVPhanCong() { return lblNVPhanCong; }
    public VBox getLblTongNV() { return lblTongNV; }
}
