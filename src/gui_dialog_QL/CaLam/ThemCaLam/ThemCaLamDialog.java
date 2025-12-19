package gui_dialog_QL.CaLam.ThemCaLam;

import dao.NhanVien_DAO;
import entity.CaLam;
import entity.NhanVien;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemCaLamDialog extends Stage {

    private TextField txtMaCa;
    private TextField txtTenCa;
    private DatePicker dpNgayLam;

    private ComboBox<LocalTime> cbGioBatDau;
    private ComboBox<LocalTime> cbGioKetThuc;

    private TextField txtViTri;
    private TextField txtSoLuongNV;
    private TextArea txtGhiChu;

    private ListView<NhanVien> listNhanVien;
    private final ObservableList<NhanVien> dsNhanVien = FXCollections.observableArrayList();
    private final Map<NhanVien, BooleanProperty> checkMap = new HashMap<>();

    private boolean confirmed = false;
    private CaLam caLam;

    private final NhanVien_DAO nhanVienDAO = NhanVien_DAO.getInstance();

    public ThemCaLamDialog(CaLam caHienTai) {
        this.caLam = caHienTai;

        setTitle(caHienTai == null ? "Thêm ca làm" : "Sửa ca làm");
        initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(12);
        grid.setVgap(10);

        int row = 0;
        grid.add(new Label("Mã ca:"), 0, row);
        txtMaCa = new TextField();
        txtMaCa.setDisable(true);
        grid.add(txtMaCa, 1, row++);

        grid.add(new Label("Tên ca:"), 0, row);
        txtTenCa = new TextField();
        grid.add(txtTenCa, 1, row++);

        grid.add(new Label("Ngày làm:"), 0, row);
        dpNgayLam = new DatePicker(LocalDate.now());
        grid.add(dpNgayLam, 1, row++);

        cbGioBatDau = new ComboBox<>();
        cbGioKetThuc = new ComboBox<>();

        for (int h = 6; h <= 22; h++) {
            cbGioBatDau.getItems().add(LocalTime.of(h, 0));
            cbGioKetThuc.getItems().add(LocalTime.of(h, 0));
        }

        cbGioBatDau.setValue(LocalTime.of(8, 0));
        cbGioKetThuc.setValue(LocalTime.of(16, 0));

        HBox gioBox = new HBox(8, cbGioBatDau, new Label("→"), cbGioKetThuc);
        grid.add(new Label("Giờ làm:"), 0, row);
        grid.add(gioBox, 1, row++);

        grid.add(new Label("Vị trí:"), 0, row);
        txtViTri = new TextField();
        grid.add(txtViTri, 1, row++);

        Label lblNV = new Label("Phân công nhân viên:");
        listNhanVien = new ListView<>();
        listNhanVien.setPrefHeight(180);

        loadNhanVienKhongQuanLy();

        listNhanVien.setCellFactory(CheckBoxListCell.forListView(nv ->
                        checkMap.computeIfAbsent(nv, k -> {
                            BooleanProperty prop = new SimpleBooleanProperty(false);
                            prop.addListener((obs, oldV, newV) -> capNhatSoLuong());
                            return prop;
                        }),
                new StringConverter<>() {
                    @Override
                    public String toString(NhanVien nv) {
                        return nv.getTenNV() + " (" + nv.getMaNV() + ")";
                    }

                    @Override
                    public NhanVien fromString(String s) {
                        return null;
                    }
                }
        ));

        VBox nvBox = new VBox(5, lblNV, listNhanVien);
        grid.add(nvBox, 0, row, 2, 1);
        row++;

        grid.add(new Label("Số NV cần:"), 0, row);
        txtSoLuongNV = new TextField("0");
        txtSoLuongNV.setDisable(true);
        grid.add(txtSoLuongNV, 1, row++);

        grid.add(new Label("Ghi chú:"), 0, row);
        txtGhiChu = new TextArea();
        txtGhiChu.setPrefRowCount(2);
        grid.add(txtGhiChu, 1, row++);

        Button btnLuu = new Button("Lưu");
        Button btnHuy = new Button("Hủy");

        btnLuu.setOnAction(e -> xuLyLuu());
        btnHuy.setOnAction(e -> close());

        HBox btnBox = new HBox(10, btnLuu, btnHuy);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        grid.add(btnBox, 1, row);

        int w = 200;
        txtMaCa.setPrefWidth(w);
        txtTenCa.setPrefWidth(w);
        dpNgayLam.setPrefWidth(w);
        cbGioBatDau.setPrefWidth(100);
        cbGioKetThuc.setPrefWidth(100);
        txtViTri.setPrefWidth(w);
        txtSoLuongNV.setPrefWidth(80);

        if (caHienTai != null) napDuLieu(caHienTai);

        setScene(new Scene(grid, 600, 650));
    }


    private void loadNhanVienKhongQuanLy() {
        List<NhanVien> ds = nhanVienDAO.getNhanVienKhongPhaiQuanLy();
        dsNhanVien.setAll(ds);
        listNhanVien.setItems(dsNhanVien);
    }


    private void napDuLieu(CaLam ca) {
        txtMaCa.setText(ca.getMaCaLam());
        txtTenCa.setText(ca.getTenCaLam());
        dpNgayLam.setValue(ca.getNgayLamViec());
        cbGioBatDau.setValue(ca.getGioBatDau());
        cbGioKetThuc.setValue(ca.getGioKetThuc());
        txtViTri.setText(ca.getViTriLamViec());
        txtSoLuongNV.setText(String.valueOf(ca.getSoLuongNhanVienCan()));
        txtGhiChu.setText(ca.getGhiChu());
    }
    private final ObservableList<NhanVien> dsNhanVienDuocChon = FXCollections.observableArrayList();

    private void capNhatSoLuong() {
        long count = checkMap.values().stream().filter(BooleanProperty::get).count();
        txtSoLuongNV.setText(String.valueOf(count));

        dsNhanVienDuocChon.clear();
        checkMap.forEach((nv, prop) -> {
            if (prop.get()) {
                dsNhanVienDuocChon.add(nv);
            }
        });
    }

    // Getter
    public ObservableList<NhanVien> getDsNhanVienDuocChon() {
        return dsNhanVienDuocChon;
    }

    private void xuLyLuu() {
        if (!cbGioKetThuc.getValue().isAfter(cbGioBatDau.getValue())) {
            new Alert(Alert.AlertType.WARNING,
                    "Giờ kết thúc phải sau giờ bắt đầu").show();
            return;
        }

        if (caLam == null) caLam = new CaLam();

        caLam.setTenCaLam(txtTenCa.getText().trim());
        caLam.setNgayLamViec(dpNgayLam.getValue());
        caLam.setGioBatDau(cbGioBatDau.getValue());
        caLam.setGioKetThuc(cbGioKetThuc.getValue());
        caLam.setViTriLamViec(txtViTri.getText().trim());
        caLam.setSoLuongNhanVienCan(Integer.parseInt(txtSoLuongNV.getText()));
        caLam.setGhiChu(txtGhiChu.getText().trim());

        confirmed = true;
        close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public CaLam getCaLam() {
        return caLam;
    }
}
