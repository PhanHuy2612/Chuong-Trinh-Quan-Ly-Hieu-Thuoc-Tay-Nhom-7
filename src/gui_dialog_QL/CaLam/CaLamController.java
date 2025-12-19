package gui_dialog_QL.CaLam;

import dao.CaLam_DAO;
import dao.LichLam_DAO;
import dao.NhanVien_DAO;
import entity.CaLam;
import entity.LichLam;
import entity.NhanVien;
import enums.TrangThaiCaLam;
import gui_dialog_QL.CaLam.ThemCaLam.ThemCaLamDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CaLamController {

    private final CaLamView view;

    private final CaLam_DAO caDAO = new CaLam_DAO();
    private final LichLam_DAO lichLamDAO = new LichLam_DAO();
    private final NhanVien_DAO nhanVienDAO = NhanVien_DAO.getInstance();

    private final Map<String, List<LichLam>> lichLamTheoCa = new HashMap<>();

    /* ================= CONSTRUCTOR ================= */
    public CaLamController(CaLamView view) {
        this.view = view;
        init();
    }

    /* ================= INIT ================= */
    private void init() {
        khoiTaoBang();
        ganSuKien();
        capNhatBang();
    }

    private void ganSuKien() {
        view.getBtnTaoCa().setOnAction(e -> showTaoCaDialog(null));
    }

    private void khoiTaoBang() {
        TableView<CaLam> table = view.getTable();
        table.getColumns().clear();

        TableColumn<CaLam, String> colTen = new TableColumn<>("Ca làm");
        colTen.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTenCaLam()));

        TableColumn<CaLam, String> colNgay = new TableColumn<>("Ngày");
        colNgay.setCellValueFactory(c -> {
            LocalDate d = c.getValue().getNgayLamViec();
            return new SimpleStringProperty(d != null ? d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy (EEEE)")) : "");
        });

        TableColumn<CaLam, String> colGio = new TableColumn<>("Giờ");
        colGio.setCellValueFactory(c -> {
            LocalTime bd = c.getValue().getGioBatDau();
            LocalTime kt = c.getValue().getGioKetThuc();
            return new SimpleStringProperty((bd != null && kt != null) ? bd + " - " + kt : "");
        });

        TableColumn<CaLam, String> colViTri = new TableColumn<>("Vị trí");
        colViTri.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getViTriLamViec()));

        TableColumn<CaLam, String> colNV = new TableColumn<>("Nhân viên");
        colNV.setCellValueFactory(c -> {
            List<LichLam> list = lichLamTheoCa.getOrDefault(c.getValue().getMaCaLam(), List.of());
            String names = list.stream()
                    .map(LichLam::getNhanVien)
                    .filter(Objects::nonNull)
                    .map(NhanVien::getTenNV)
                    .limit(3)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(
                    list.isEmpty() ? "Chưa có" : names + (list.size() > 3 ? "..." : "") + " (" + list.size() + ")"
            );
        });

        TableColumn<CaLam, String> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(c -> {
            LocalDate ngay = c.getValue().getNgayLamViec();
            LocalDate today = LocalDate.now();
            if (ngay == null) return new SimpleStringProperty("Tương lai");
            if (ngay.equals(today)) return new SimpleStringProperty("Hôm nay");
            if (ngay.equals(today.plusDays(1))) return new SimpleStringProperty("Sắp tới");
            if (ngay.isBefore(today)) return new SimpleStringProperty("Đã qua");
            return new SimpleStringProperty("Tương lai");
        });

        TableColumn<CaLam, Void> colAction = new TableColumn<>("Thao tác");
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDel = new Button("Xóa");

            {
                btnEdit.setOnAction(e -> showTaoCaDialog(getTableView().getItems().get(getIndex())));
                btnDel.setOnAction(e -> xoaCaLam(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(10, btnEdit, btnDel));
            }
        });

        table.getColumns().addAll(colTen, colNgay, colGio, colViTri, colNV, colTrangThai, colAction);
    }
    private void capNhatBang() {
        TableView<CaLam> table = view.getTable();
        table.getItems().clear();

        List<CaLam> dsCa = caDAO.getAllCaLam();
        List<LichLam> dsLich = lichLamDAO.getAllLichLam();

        lichLamTheoCa.clear();
        for (LichLam l : dsLich) {
            if (l.getCaLam() != null) {
                lichLamTheoCa.computeIfAbsent(l.getCaLam().getMaCaLam(), k -> new ArrayList<>()).add(l);
            }
        }

        table.getItems().addAll(dsCa);
        capNhatThongKe();
    }

    private void capNhatThongKe() {
        ((Label) view.getLblTongCa().getChildren().get(0)).setText(String.valueOf(caDAO.demTongCaLam()));
        ((Label) view.getLblCaSapToi().getChildren().get(0)).setText(String.valueOf(lichLamDAO.countCaSapToi()));
        ((Label) view.getLblNVPhanCong().getChildren().get(0)).setText(String.valueOf(lichLamDAO.countNhanVienPhanCong()));
        ((Label) view.getLblTongNV().getChildren().get(0)).setText(String.valueOf(nhanVienDAO.demTongNhanVien()));
    }

    private void showTaoCaDialog(CaLam caHienTai) {
        ThemCaLamDialog dialog = new ThemCaLamDialog(caHienTai);
        dialog.showAndWait();

        if (dialog.isConfirmed()) {
            CaLam ca = dialog.getCaLam();

            boolean ketQua;
            String thongBao;

            if (caHienTai == null) {
                ca.setMaCaLam(caDAO.sinhMaCaLam());
                ketQua = caDAO.themCaLam(ca);
                thongBao = ketQua ? "Thêm ca làm thành công!" : "Thêm ca làm thất bại!";
            } else {
                ketQua = caDAO.capNhatCaLam(ca);
                thongBao = ketQua ? "Cập nhật ca làm thành công!" : "Cập nhật ca làm thất bại!";
            }

            if (ketQua) {
                if (caHienTai != null) {
                    lichLamDAO.xoaTheoMaCa(ca.getMaCaLam());
                }

                ObservableList<NhanVien> dsNVChon = dialog.getDsNhanVienDuocChon();

                for (NhanVien nv : dsNVChon) {
                    LichLam lich = new LichLam();
                    lich.setNgayLam(ca.getNgayLamViec());
                    lich.setCaLam(ca);
                    lich.setNhanVien(nv);
                    lich.setTrangThaiCaLam(TrangThaiCaLam.CHUA_BAT_DAU);
                    lich.setGhiChu("");

                    lichLamDAO.themLichLam(lich);
                }
            }

            Alert alert = new Alert(ketQua ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText(thongBao);
            alert.showAndWait();

            if (ketQua) {
                capNhatBang();
            }
        }
    }

    private void xoaCaLam(CaLam ca) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa ca \"" + ca.getTenCaLam() + "\" không?\nTất cả phân công nhân viên của ca này cũng sẽ bị xóa.",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Xác nhận xóa");

        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            lichLamDAO.xoaTheoMaCa(ca.getMaCaLam());
            boolean xoaThanhCong = caDAO.xoaCaLam(ca.getMaCaLam());

            new Alert(xoaThanhCong ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    xoaThanhCong ? "Xóa ca làm thành công!" : "Xóa ca làm thất bại!",
                    ButtonType.OK).show();

            if (xoaThanhCong) {
                capNhatBang();
            }
        }
    }
}