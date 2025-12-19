package gui_dialog_NV.banthuoc.themkh;

import dao.KhachHang_DAO;
import entity.KhachHang;
import enums.LoaiKhachHang;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ThemKhachHangDialog extends Stage {

    private final KhachHang_DAO dao = new KhachHang_DAO();

    private boolean confirmed = false;
    private KhachHang khachHang;

    public ThemKhachHangDialog() {
        this(null, null);
    }

    public ThemKhachHangDialog(String sdt, String loaiKhachGoiY) {
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Thêm khách hàng mới");

        initUI(sdt, loaiKhachGoiY);
    }

    public ThemKhachHangDialog(KhachHang kh) {
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Sửa thông tin khách hàng");

        initUIForEdit(kh);
    }

    private void initUI(String sdtDuocTruyen, String loaiKhachGoiY) {
        boolean coSDTSan = (sdtDuocTruyen != null && !sdtDuocTruyen.trim().isEmpty());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        /* ---------- Form ---------- */
        TextField txtTen = new TextField();
        txtTen.setPromptText("Họ và tên *");

        ToggleGroup groupGT = new ToggleGroup();
        RadioButton rbNam = new RadioButton("Nam");
        RadioButton rbNu = new RadioButton("Nữ");
        rbNam.setToggleGroup(groupGT);
        rbNu.setToggleGroup(groupGT);
        rbNam.setSelected(true);

        TextField txtSDT = new TextField();
        Label lblSDT = new Label();
        if (coSDTSan) {
            lblSDT.setText(sdtDuocTruyen);
            lblSDT.setStyle("-fx-font-weight: bold");
        } else {
            txtSDT.setPromptText("Số điện thoại *");
        }

        grid.add(new Label("Họ tên:"), 0, 0);
        grid.add(txtTen, 1, 0, 2, 1);

        grid.add(new Label("Giới tính:"), 0, 1);
        grid.add(rbNam, 1, 1);
        grid.add(rbNu, 2, 1);

        grid.add(new Label("Số điện thoại:"), 0, 2);
        if (coSDTSan) {
            grid.add(lblSDT, 1, 2, 2, 1);
        } else {
            grid.add(txtSDT, 1, 2, 2, 1);
        }

        /* ---------- Buttons ---------- */
        Button btnLuu = new Button("Lưu");
        Button btnHuy = new Button("Hủy");

        btnLuu.setOnAction(e -> {
            String ten = txtTen.getText().trim();
            String soDienThoai = coSDTSan ? sdtDuocTruyen.trim() : txtSDT.getText().trim();

            if (ten.isEmpty()) {
                alert("Vui lòng nhập họ tên!");
                return;
            }
            if (!coSDTSan && soDienThoai.isEmpty()) {
                alert("Vui lòng nhập số điện thoại!");
                return;
            }
            if (!coSDTSan && !soDienThoai.matches("\\d{10,11}")) {
                alert("Số điện thoại không hợp lệ (10-11 chữ số)!");
                return;
            }

            // Kiểm tra trùng SDT (trừ trường hợp đang sửa)
            if (!coSDTSan && dao.getBySoDienThoai(soDienThoai) != null) {
                alert("Số điện thoại đã tồn tại!");
                return;
            }

            KhachHang kh = new KhachHang();
            kh.setTenKH(ten);
            kh.setGioiTinh(rbNam.isSelected());
            kh.setSoDienThoai(soDienThoai);
            kh.setDiemTichLuy(0);
            kh.setDiemDoiThuong(0);

            // Xác định loại khách hàng
            LoaiKhachHang loai = LoaiKhachHang.BINHTHUONG;
            if (loaiKhachGoiY != null) {
                loai = switch (loaiKhachGoiY) {
                    case "Khách VIP" -> LoaiKhachHang.VIP;
                    case "Khách thường" -> LoaiKhachHang.BINHTHUONG;
                    default -> LoaiKhachHang.BINHTHUONG;
                };
            }
            kh.setLoaiKhachHang(loai);

            this.khachHang = kh;
            this.confirmed = true;
            close();
        });

        btnHuy.setOnAction(e -> close());

        grid.add(btnLuu, 1, 4);
        grid.add(btnHuy, 2, 4);

        setScene(new Scene(grid, coSDTSan ? 420 : 460, coSDTSan ? 260 : 290));
    }

    /* ================= UI cho sửa khách hàng ================= */
    private void initUIForEdit(KhachHang kh) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtTen = new TextField(kh.getTenKH());
        txtTen.setPromptText("Họ và tên");

        ToggleGroup groupGT = new ToggleGroup();
        RadioButton rbNam = new RadioButton("Nam");
        RadioButton rbNu = new RadioButton("Nữ");
        rbNam.setToggleGroup(groupGT);
        rbNu.setToggleGroup(groupGT);
        if (kh.isGioiTinh()) rbNam.setSelected(true);
        else rbNu.setSelected(true);

        TextField txtSDT = new TextField(kh.getSoDienThoai());
        txtSDT.setPromptText("Số điện thoại");

        grid.add(new Label("Họ tên:"), 0, 0);
        grid.add(txtTen, 1, 0, 2, 1);

        grid.add(new Label("Giới tính:"), 0, 1);
        grid.add(rbNam, 1, 1);
        grid.add(rbNu, 2, 1);

        grid.add(new Label("Số điện thoại:"), 0, 2);
        grid.add(txtSDT, 1, 2, 2, 1);

        Button btnLuu = new Button("Lưu");
        Button btnHuy = new Button("Hủy");

        btnLuu.setOnAction(e -> {
            String ten = txtTen.getText().trim();
            String sdt = txtSDT.getText().trim();

            if (ten.isEmpty() || sdt.isEmpty()) {
                alert("Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // Cập nhật thông tin (không thay đổi điểm và loại KH ở đây, chỉ cập nhật cơ bản)
            kh.setTenKH(ten);
            kh.setGioiTinh(rbNam.isSelected());
            kh.setSoDienThoai(sdt);

            this.khachHang = kh;
            this.confirmed = true;
            close();
        });

        btnHuy.setOnAction(e -> close());

        grid.add(btnLuu, 1, 3);
        grid.add(btnHuy, 2, 3);

        setScene(new Scene(grid, 460, 290));
    }

    /* ================= GETTERS ================= */
    public boolean isConfirmed() {
        return confirmed;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    /* ================= ALERT ================= */
    private void alert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
}