package gui_dialog_NV.KhachHang;

import dao.KhachHang_DAO;
import entity.KhachHang;
import enums.LoaiKhachHang;
import gui_dialog_NV.banthuoc.themkh.ThemKhachHangDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class KhachHangController {

    private final KhachHangView view;
    private final KhachHang_DAO khachHangDAO;

    private ObservableList<KhachHang> masterData;
    private FilteredList<KhachHang> filteredData;

    public KhachHangController(KhachHangView view) {
        this.view = view;
        this.khachHangDAO = new KhachHang_DAO();

        initData();
        initEvent();
    }

    private void initData() {
        masterData = FXCollections.observableArrayList(
                khachHangDAO.getAllKhachHang()
        );

        filteredData = new FilteredList<>(masterData, p -> true);
        view.getTableKhachHang().setItems(filteredData);
    }

    private void initEvent() {

        view.getTxtSearch().textProperty().addListener((obs, o, n) -> applyFilter());
        view.getCboLoaiKH().valueProperty().addListener((obs, o, n) -> applyFilter());
        view.getCboGioiTinh().valueProperty().addListener((obs, o, n) -> applyFilter());
        view.getTxtDiemMin().textProperty().addListener((obs, o, n) -> applyFilter());

        view.getBtnThem().setOnAction(e -> themKhachHang());
    }

    private void applyFilter() {
        String keyword = view.getTxtSearch().getText().trim().toLowerCase();
        String loaiKH = view.getCboLoaiKH().getValue();
        String gioiTinh = view.getCboGioiTinh().getValue();
        String diemMinText = view.getTxtDiemMin().getText().trim();

        int diemMin = 0;
        try {
            if (!diemMinText.isEmpty())
                diemMin = Integer.parseInt(diemMinText);
        } catch (NumberFormatException ignored) {}

        int finalDiemMin = diemMin;

        filteredData.setPredicate(kh -> {

            boolean matchSearch =
                    keyword.isEmpty()
                            || kh.getTenKH().toLowerCase().contains(keyword)
                            || kh.getSoDienThoai().contains(keyword);

            boolean matchLoai =
                    loaiKH.equals("Tất cả")
                            || (loaiKH.equals("VIP") && kh.getLoaiKhachHang() == LoaiKhachHang.VIP)
                            || (loaiKH.equals("Bình thường") && kh.getLoaiKhachHang() == LoaiKhachHang.BINHTHUONG);

            boolean matchGioiTinh =
                    gioiTinh.equals("Tất cả")
                            || (gioiTinh.equals("Nam") && kh.isGioiTinh())
                            || (gioiTinh.equals("Nữ") && !kh.isGioiTinh());

            boolean matchDiem = kh.getDiemTichLuy() >= finalDiemMin;

            return matchSearch && matchLoai && matchGioiTinh && matchDiem;
        });
    }

    private void themKhachHang() {
        ThemKhachHangDialog dialog = new ThemKhachHangDialog();
        dialog.showAndWait();

        if (dialog.isConfirmed()) {
            KhachHang kh = dialog.getKhachHang();

            String maxMa = khachHangDAO.getMaxMaKH();
            String maMoi = generateMaKhachHang(maxMa);
            kh.setMaKH(maMoi);

            if (khachHangDAO.addKhachHang(kh)) {
                masterData.add(kh);
                new Alert(Alert.AlertType.INFORMATION,
                        "Thêm khách hàng thành công! Mã KH: " + maMoi, ButtonType.OK).show();
            } else {
                new Alert(Alert.AlertType.ERROR,
                        "Thêm khách hàng thất bại!", ButtonType.OK).show();
            }
        }
    }

    private String generateMaKhachHang(String maxMa) {
        if (maxMa == null || !maxMa.startsWith("KHT")) {
            return "KHT0001";
        }

        try {
            int so = Integer.parseInt(maxMa.substring(3)); // Lấy số sau "KHT"
            int soMoi = so + 1;
            return String.format("KHT%04d", soMoi);
        } catch (NumberFormatException e) {
            return "KHT0001";
        }
    }

    public void xoaKhachHang(KhachHang kh) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa khách hàng này?",
                ButtonType.YES, ButtonType.NO);

        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            if (khachHangDAO.deleteKhachHang(kh.getMaKH())) {
                masterData.remove(kh);
            }
        }
    }

    public void suaKhachHang(KhachHang kh) {
        ThemKhachHangDialog dialog = new ThemKhachHangDialog(kh);
        dialog.showAndWait();

        if (dialog.isConfirmed()) {
            if (khachHangDAO.updateKhachHang(kh)) {
                view.getTableKhachHang().refresh();
            }
        }
    }
}
