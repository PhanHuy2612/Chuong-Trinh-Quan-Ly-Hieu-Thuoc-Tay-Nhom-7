package gui_dialog_NV.LichLam;

import dao.LichLam_DAO;
import dao.NhanVien_DAO;
import entity.LichLam;
import entity.NhanVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class LichLamController {

    private NhanVien currentNV;
    private final LichLamView view;
    private final LichLam_DAO lichLamDAO = new LichLam_DAO();
    private final NhanVien_DAO nhanVienDAO = NhanVien_DAO.getInstance();
    private final String tenDangNhap;

    private ObservableList<LichLam> masterData;
    private FilteredList<LichLam> filteredData;

    public LichLamController(LichLamView view, String tenDangNhap) {
        this.view = view;
        this.tenDangNhap = tenDangNhap;
        init();
    }

    private void init() {
        loadData();
        initEvents();
    }

    private void loadData() {
        this.currentNV = nhanVienDAO.getBySoDienThoai(tenDangNhap);

        if (currentNV == null || currentNV.getMaNV() == null) {
            view.getTableLichLam()
                    .setPlaceholder(new Label("Không tìm thấy thông tin nhân viên."));
            return;
        }

        view.setTenNhanVien(currentNV.getTenNV());

        masterData = FXCollections.observableArrayList(
                lichLamDAO.getAllLichLam()
        );

        filteredData = new FilteredList<>(masterData, lich -> {
            if (lich.getNhanVien() == null) return false;
            return currentNV.getMaNV().equals(lich.getNhanVien().getMaNV());
        });

        view.setItems(filteredData);

        view.getTableLichLam()
                .setPlaceholder(new Label("Bạn chưa được phân công ca làm nào."));
        masterData.forEach(ll -> {
            if (ll.getCaLam() != null) {
                System.out.println(
                        ll.getCaLam().getTenCaLam() + " | " +
                                ll.getCaLam().getGioBatDau() + " - " +
                                ll.getCaLam().getGioKetThuc()
                );
            }
        });

    }

    private void initEvents() {
        view.getTxtSearch()
                .textProperty()
                .addListener((obs, oldVal, newVal) -> applyFilter());

        view.getDpTuNgay()
                .valueProperty()
                .addListener((obs, oldVal, newVal) -> applyFilter());

        view.getDpDenNgay()
                .valueProperty()
                .addListener((obs, oldVal, newVal) -> applyFilter());

        view.getBtnTimKiem()
                .setOnAction(e -> applyFilter());
    }

    private void applyFilter() {
        String keyword = view.getTxtSearch()
                .getText()
                .trim()
                .toLowerCase();

        LocalDate tuNgay = view.getDpTuNgay().getValue();
        LocalDate denNgay = view.getDpDenNgay().getValue();

        filteredData.setPredicate(lich -> {

            if (lich.getNhanVien() == null
                    || !lich.getNhanVien().getMaNV().equals(currentNV.getMaNV())) {
                return false;
            }

            // 🔵 2. Lọc theo từ khóa
            if (!keyword.isEmpty()) {
                String tenCa = lich.getCaLam() != null
                        ? lich.getCaLam().getTenCaLam().toLowerCase()
                        : "";

                String viTri = lich.getCaLam() != null
                        ? lich.getCaLam().getViTriLamViec().toLowerCase()
                        : "";

                if (!tenCa.contains(keyword) && !viTri.contains(keyword)) {
                    return false;
                }
            }

            LocalDate ngayLam = lich.getNgayLam();
            if (ngayLam != null) {
                if (tuNgay != null && ngayLam.isBefore(tuNgay)) return false;
                if (denNgay != null && ngayLam.isAfter(denNgay)) return false;
            }

            return true;
        });
    }

}
