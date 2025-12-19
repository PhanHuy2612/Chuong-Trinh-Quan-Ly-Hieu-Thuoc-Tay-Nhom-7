package gui_dialog_NV.banthuoc;

import dao.*;
import entity.*;
import enums.LoaiKhachHang;
import enums.PhuongThucThanhToan;
import gui_dialog_NV.banthuoc.hoadon.HoaDonDialog;
import gui_dialog_NV.banthuoc.themkh.ThemKhachHangDialog;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class BanThuocNV_Controller {

    private final NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    private final BanThuocNV_View view = new BanThuocNV_View();
    private final Thuoc_DAO thuocDAO = new Thuoc_DAO();
    private final NhanVien_DAO nhanVienDAO = NhanVien_DAO.getInstance();
    private final NhaCungCap_DAO nccDAO = new NhaCungCap_DAO();
    private final KhoHang_DAO khoDAO = new KhoHang_DAO();
    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    private final KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();
    private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();

    private final NhanVien nhanVienDangNhap;
    private final CartManager cart = new CartManager();
    private final MedicineFilter filter = new MedicineFilter();

    public BanThuocNV_Controller(String soDienThoai) {
        this.nhanVienDangNhap = nhanVienDAO.getBySoDienThoai(soDienThoai);
        if (this.nhanVienDangNhap == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên với số điện thoại: " + soDienThoai);
        }

        loadInitialData();
        setupBindings();
        setupCart();
        setupRefreshButton();
        view.getMedicineSearch().requestFocus();
    }

    /* ================= LOAD DATA ================= */
    private void loadInitialData() {
        List<Thuoc> all = thuocDAO.getAllTbThuoc();
        filter.setSourceList(all);
        loadComboBoxes(all);
        filter.applyFilter();
    }

    private void loadComboBoxes(List<Thuoc> list) {
        // ===== LOẠI THUỐC =====
        ComboBox<String> cbLoai = view.getCbLoaiThuoc();
        var loaiList = list.stream()
                .map(Thuoc::getLoaiThuoc)
                .filter(s -> s != null && !s.isBlank())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        cbLoai.getItems().setAll("Loại thuốc", "Tất cả");
        cbLoai.getItems().addAll(loaiList);
        cbLoai.setValue("Loại thuốc");

        // ===== NHÀ CUNG CẤP =====
        ComboBox<String> cbNCC = view.getCbNhaCungCap();
        var nccList = list.stream()
                .map(Thuoc::getMaNhaCungCap)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        cbNCC.getItems().setAll("Nhà cung cấp", "Tất cả");
        cbNCC.getItems().addAll(nccList);
        cbNCC.setValue("Nhà cung cấp");

        cbNCC.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(String ma, boolean empty) {
                super.updateItem(ma, empty);
                if (empty || ma == null) setText(null);
                else if ("Nhà cung cấp".equals(ma) || "Tất cả".equals(ma)) setText(ma);
                else setText(nccDAO.getTenNCCByMa(ma));
            }
        });
        cbNCC.setButtonCell(cbNCC.getCellFactory().call(null));

        // ===== KHO =====
        ComboBox<String> cbKho = view.getCbKho();
        var khoList = list.stream()
                .map(Thuoc::getMaKho)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        cbKho.getItems().setAll("Kho thuốc", "Tất cả");
        cbKho.getItems().addAll(khoList);
        cbKho.setValue("Kho thuốc");

        cbKho.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(String ma, boolean empty) {
                super.updateItem(ma, empty);
                if (empty || ma == null) setText(null);
                else if ("Kho thuốc".equals(ma) || "Tất cả".equals(ma)) setText(ma);
                else setText(khoDAO.getTenKhoByMa(ma));
            }
        });
        cbKho.setButtonCell(cbKho.getCellFactory().call(null));
    }

    /* ================= NÚT LÀM MỚI ================= */
    private void setupRefreshButton() {
        view.getRefreshButton().setOnAction(e -> resetFiltersAndSearch());
    }

    private void resetFiltersAndSearch() {
        view.getMedicineSearch().clear();
        view.getCbLoaiThuoc().setValue("Loại thuốc");
        view.getCbNhaCungCap().setValue("Nhà cung cấp");
        view.getCbKho().setValue("Kho thuốc");
        view.getChkConHang().setSelected(false);
        view.getChkSapHet().setSelected(false);

        filter.applyFilter();

        view.getMedicineListContainer().getChildren().clear();
        if (filter.getFilteredList().isEmpty()) {
            view.updateMedicineListPlaceholder();
        } else {
            filter.getFilteredList().forEach(t ->
                    view.getMedicineListContainer().getChildren().add(
                            new MedicineRow(t, () -> cart.add(t))
                    )
            );
        }
    }

    /* ================= BINDINGS ================= */
    private void setupBindings() {
        view.getMedicineSearch().textProperty().bindBidirectional(filter.keywordProperty());
        view.getCbLoaiThuoc().valueProperty().bindBidirectional(filter.loaiThuocProperty());
        view.getCbNhaCungCap().valueProperty().bindBidirectional(filter.nhaCungCapProperty());
        view.getCbKho().valueProperty().bindBidirectional(filter.khoProperty());
        view.getChkConHang().selectedProperty().bindBidirectional(filter.onlyConHangProperty());
        view.getChkSapHet().selectedProperty().bindBidirectional(filter.onlySapHetProperty());

        filter.getFilteredList().addListener((ListChangeListener<Thuoc>) c -> {
            view.getMedicineListContainer().getChildren().clear();
            if (filter.getFilteredList().isEmpty()) {
                view.updateMedicineListPlaceholder();
            } else {
                filter.getFilteredList().forEach(t ->
                        view.getMedicineListContainer().getChildren().add(
                                new MedicineRow(t, () -> cart.add(t))
                        )
                );
            }
        });
    }

    /* ================= GIỎ HÀNG ================= */
    private void setupCart() {
        // Binding tổng tiền realtime
        view.getTongCongLabel().textProperty().bind(
                Bindings.createStringBinding(() ->
                                String.format("Tổng cộng: %,.0f đ", cart.getTotal()),
                        cart.totalProperty()
                )
        );

        // Rebuild giỏ khi thêm/xóa thuốc
        cart.getItems().addListener((ListChangeListener<CartManager.CartItem>) c -> rebuildCartUI());

        view.getClearCartButton().setOnAction(e -> cart.clear());

        view.getCheckoutButton().setOnAction(e -> xuLyThanhToan());

        rebuildCartUI(); // Khởi tạo lần đầu
    }

    private void rebuildCartUI() {
        view.getCartItemsContainer().getChildren().clear();
        cart.getItems().forEach(item ->
                view.getCartItemsContainer().getChildren().add(
                        new CartItemRow(item.getThuoc(), item.getQuantity(),
                                q -> {
                                    if (q <= 0) {
                                        cart.remove(item.getThuoc());
                                    } else {
                                        cart.updateQuantity(item.getThuoc(), q);
                                    }
                                })
                )
        );
    }

    private void xuLyThanhToan() {

        if (cart.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Giỏ hàng trống!", ButtonType.OK).show();
            return;
        }

        double tongTienHang = cart.getTotal();
        double chietKhauKhuyenMai = tinhChietKhauTuKhuyenMai();
        double tongSauKhuyenMai = tongTienHang - chietKhauKhuyenMai;

        String soHD = "HD" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String loaiKhach = view.getCustomerTypeCombo().getValue();
        KhachHang kh = null;
        double giamDoiThuong = 0;
        int diemSuDung = 0;

        // ================= KHÁCH HÀNG THÀNH VIÊN =================
        if (!"Khách vãng lai".equals(loaiKhach)) {

            String sdt = view.getPhoneField().getText().trim();
            if (sdt.isEmpty()) {
                new Alert(Alert.AlertType.WARNING,
                        "Vui lòng nhập số điện thoại khách hàng!", ButtonType.OK).show();
                return;
            }

            kh = khachHangDAO.getBySoDienThoai(sdt);

            if (kh == null) {
                ThemKhachHangDialog dialog =
                        new ThemKhachHangDialog(sdt, loaiKhach);
                dialog.initOwner(view.getRoot().getScene().getWindow());
                dialog.showAndWait();

                kh = khachHangDAO.getBySoDienThoai(sdt);
                if (kh == null) {
                    new Alert(Alert.AlertType.INFORMATION,
                            "Thanh toán đã bị hủy.", ButtonType.OK).show();
                    return;
                }
            }

            // Cộng điểm: 10.000đ = 1 điểm
            int diemCong = (int) (tongTienHang / 10_000);
            if (diemCong > 0) {
                kh.congDiem(diemCong);
            }

            // Đổi điểm
            if (tongSauKhuyenMai >= 10_000) {
                int diemCo = kh.getDiemDoiThuong();
                int giaTriGiam =
                        kh.getLoaiKhachHang() == LoaiKhachHang.VIP ? 12_000 : 10_000;

                int soLan = diemCo / 50;
                double giamMax = soLan * giaTriGiam;

                giamDoiThuong = Math.min(giamMax, tongSauKhuyenMai);
                diemSuDung = (int) Math.ceil(giamDoiThuong / giaTriGiam) * 50;

                if (diemSuDung > 0) {
                    kh.truDiemDoiThuong(diemSuDung);
                }
            }

            khachHangDAO.updateDiem(kh);
        }

        double tongThanhToan = tongSauKhuyenMai - giamDoiThuong;

        String ghiChuDoiThuong = giamDoiThuong > 0
                ? "Giảm đổi điểm: -" + nf.format(giamDoiThuong)
                + " (sử dụng " + diemSuDung + " điểm)"
                : null;

        // ================= HIỂN THỊ HÓA ĐƠN =================
        HoaDonDialog hoaDonDialog = new HoaDonDialog(
                kh,
                new ArrayList<>(cart.getItems()),
                tongTienHang,
                chietKhauKhuyenMai,
                giamDoiThuong,
                tongThanhToan,
                soHD,
                ghiChuDoiThuong
        );

        hoaDonDialog.initOwner(view.getRoot().getScene().getWindow());
        hoaDonDialog.showAndWait();

        if (!hoaDonDialog.isConfirmed()) {
            new Alert(Alert.AlertType.INFORMATION,
                    "Thanh toán đã bị hủy.", ButtonType.OK).show();
            return;
        }

        // ================= LƯU DB =================
        try {
            HoaDon hoaDon = new HoaDon();
            hoaDon.setMaHD(soHD);
            hoaDon.setNgayLap(LocalDate.now());
            hoaDon.setNhanVien(nhanVienDangNhap);
            hoaDon.setKhachHang(kh);
            hoaDon.setPhuongThucThanhToan(
                    hoaDonDialog.getPhuongThucThanhToan());
            hoaDon.setThueVAT(BigDecimal.ZERO);
            hoaDon.setTienGiam(
                    BigDecimal.valueOf(chietKhauKhuyenMai + giamDoiThuong));
            hoaDon.setTongTien(BigDecimal.valueOf(tongThanhToan));

            // 1️⃣ INSERT HÓA ĐƠN
            boolean ok = hoaDonDAO.addHoaDon(hoaDon);
            if (!ok) {
                new Alert(Alert.AlertType.ERROR,
                        "Lưu hóa đơn thất bại!", ButtonType.OK).show();
                return;
            }

            for (CartManager.CartItem item : cart.getItems()) {

                ChiTietHoaDon_DAO ct_DAO = new ChiTietHoaDon_DAO();
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setHoaDon(hoaDon);
                ct.setThuoc(item.getThuoc());
                ct.setSoLuong(item.getQuantity());
                ct.setDonGia(
                        BigDecimal.valueOf(item.getThuoc().getGiaBan()));
                ct.tinhThanhTien();

                ct_DAO.addChiTietHoaDon(ct);

                // Cập nhật tồn kho
                Thuoc thuoc = item.getThuoc();
                int slMoi = thuoc.getSoLuong() - item.getQuantity();
                if (slMoi < 0) slMoi = 0;
                thuoc.setSoLuong(slMoi);
                thuocDAO.updateSoLuongThuoc(thuoc);
            }

            new Alert(Alert.AlertType.INFORMATION,
                    "Thanh toán thành công!\nHóa đơn " + soHD + " đã được lưu.",
                    ButtonType.OK).show();

            cart.clear();

        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Lỗi khi lưu hóa đơn: " + ex.getMessage(),
                    ButtonType.OK).show();
        }
    }


    private double tinhChietKhauTuKhuyenMai() {
        double chietKhauTong = 0.0;
        LocalDate today = LocalDate.now();

        List<KhuyenMai> allKhuyenMai = khuyenMaiDAO.getAllKhuyenMai();

        List<KhuyenMai> activeKhuyenMai = allKhuyenMai.stream()
                .filter(km -> !today.isBefore(km.getNgayBatDau()) && !today.isAfter(km.getNgayKetThuc()))
                .collect(Collectors.toList());

        for (KhuyenMai km : activeKhuyenMai) {
            List<String> loaiApDung = (km.getLoaiThuocApDung() != null && !km.getLoaiThuocApDung().isEmpty())
                    ? Arrays.asList(km.getLoaiThuocApDung().split(","))
                    : null;

            double tongHangApDung = 0.0;

            for (CartManager.CartItem item : cart.getItems()) {
                Thuoc thuoc = item.getThuoc();
                if (loaiApDung == null || loaiApDung.contains(thuoc.getPhanLoai().name())) {
                    tongHangApDung += thuoc.getGiaBan() * item.getQuantity();
                }
            }

            double giam = tongHangApDung * (km.getPhanTramGiam() / 100.0);

            if (km.getTienGiamToiDa() != null) {
                giam = Math.min(giam, km.getTienGiamToiDa().doubleValue());
            }

            chietKhauTong += giam;
        }

        return chietKhauTong;
    }

    public BanThuocNV_View getView() {
        return view;
    }
}