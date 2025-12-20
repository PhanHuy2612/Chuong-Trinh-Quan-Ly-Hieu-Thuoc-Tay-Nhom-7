--Phải xóa database QLThuoc cũ trước khi chạy script!!!
USE master;
GO
/*
ALTER DATABASE QLThuoc 
SET SINGLE_USER 
WITH ROLLBACK IMMEDIATE;
GO
DROP DATABASE QLThuoc;
GO*/
CREATE DATABASE QLThuoc;
GO
USE QLThuoc;
GO

-- 1. Bảng Ca Làm
CREATE TABLE CaLam (
    maCaLam varchar(10) PRIMARY KEY,
    tenCaLam nvarchar(100) NOT NULL,
    ngayLamViec date,
    gioBatDau time(7),
    gioKetThuc time(7),
    viTriLamViec nvarchar(100),
    soLuongNhanVienCan int DEFAULT 0,
    ghiChu nvarchar(500)
);

-- 2. Bảng Nhân Viên
CREATE TABLE NhanVien (
    maNhanVien nvarchar(20) PRIMARY KEY,
    tenNhanVien nvarchar(100) NOT NULL,
    gioiTinh nvarchar(3) NOT NULL,
    ngaySinh date NOT NULL,
    soDienThoai nvarchar(15) NOT NULL,
    hinhAnh_NV nvarchar(50) NOT NULL,
    diaChi nvarchar(100),
    email nvarchar(100)
);

-- 3. Bảng Tài Khoản (Nối với Nhân Viên)
CREATE TABLE TaiKhoan (
    maNhanVien nvarchar(20) PRIMARY KEY REFERENCES NhanVien(maNhanVien) ON DELETE CASCADE,
    tenDangNhap nvarchar(50) NOT NULL,
    matKhau nvarchar(255) NOT NULL,
    quyenTruyCap nvarchar(40) NOT NULL CHECK (quyenTruyCap IN ('QUANLY', 'DUOCSI')),
    trangThaiTaiKhoan nvarchar(20) NOT NULL CHECK (trangThaiTaiKhoan IN ('DANGHOATDONG', 'DANGUNG')),
    Email nvarchar(100)
);

-- 4. Bảng Lịch Làm (Nối Nhân Viên & Ca Làm)
CREATE TABLE LichLam (
    maLichLam varchar(10) PRIMARY KEY,
    maNhanVien nvarchar(20) REFERENCES NhanVien(maNhanVien),
    maCaLam varchar(10) REFERENCES CaLam(maCaLam),
    ngayLam date NOT NULL,
    trangThaiCaLam nvarchar(50),
    ghiChu nvarchar(255)
);

-- 5. Bảng Khách Hàng
CREATE TABLE KhachHang (
    maKhachHang nvarchar(20) PRIMARY KEY,
    hoTen nvarchar(100) NOT NULL,
    gioiTinh bit NOT NULL,
    soDienThoai nvarchar(50) NOT NULL,
    loaiKhachHang nvarchar(20) NOT NULL CHECK (loaiKhachHang IN ('BINHTHUONG', 'VIP', 'VANGLAI')),
    diemTichLuy int DEFAULT 0,
    diemDoiThuong int
);

-- 6. Bảng Điểm Tích Lũy
CREATE TABLE DiemTichLuy (
    maDiem int IDENTITY(1,1) PRIMARY KEY,
    maKH nvarchar(20) REFERENCES KhachHang(maKhachHang),
    ngayPhatSinh date NOT NULL,
    ngayHetHan date NOT NULL
);

-- 7. Bảng Nhà Cung Cấp
CREATE TABLE NhaCungCap (
    maNhaCungCap nvarchar(20) PRIMARY KEY,
    tenNhaCungCap nvarchar(100) NOT NULL,
    soDienThoai nvarchar(15) NOT NULL,
    email nvarchar(100) NOT NULL,
    diaChi nvarchar(255) NOT NULL
);

-- 8. Bảng Kho Hàng
CREATE TABLE KhoHang (
    maKho nvarchar(20) PRIMARY KEY,
    tenKho nvarchar(100),
    ngayNhap date NOT NULL,
    email nvarchar(100) NOT NULL,
    soLuong int CHECK (soLuong >= 0),
    trangThai nvarchar(20) CHECK (trangThai IN ('TON_KHO', 'SAP_HET_HANG', 'HET_HANG', 'CON_HANG'))
);

-- 9. Bảng Thuốc
CREATE TABLE Thuoc (
    maThuoc nvarchar(20) PRIMARY KEY,
    tenThuoc nvarchar(100) NOT NULL,
    maLoai varchar(10),
    loaiThuoc nvarchar(50),
    phanLoai varchar(20) DEFAULT 'OTC',
    giaNhap decimal(18, 2) CHECK (giaNhap >= 0),
    giaBan decimal(18, 2) CHECK (giaBan >= 0),
    SoLuong int DEFAULT 0,
    DonViNhapKho nvarchar(50) DEFAULT N'viên' CHECK (DonViNhapKho IN (N'CHAI', N'HOP', N'VIEN', N'TUYP', N'GOI')),
    DonViBan nvarchar(100) CHECK (DonViBan IN (N'CHAI', N'HOP', N'VIEN', N'TUYP', N'GOI')),
    TiLeDonVi nvarchar(200),
    hanSuDung date NOT NULL,
    trangThaiTonKho nvarchar(50) NOT NULL,
    tonKhoToiThieu int DEFAULT 50,
    tonKhoToiDa int DEFAULT 1000,
    maKho nvarchar(20) REFERENCES KhoHang(maKho) ON DELETE CASCADE,
    maNhaCungCap nvarchar(20) REFERENCES NhaCungCap(maNhaCungCap) ON DELETE CASCADE
);

-- 10. Bảng Hóa Đơn
CREATE TABLE HoaDon (
    maHoaDon nvarchar(20) PRIMARY KEY,
    ngayLap datetime2(7) DEFAULT GETDATE(),
    maNhanVien nvarchar(20) REFERENCES NhanVien(maNhanVien) ON DELETE CASCADE,
    maKhachHang nvarchar(20) REFERENCES KhachHang(maKhachHang) ON DELETE CASCADE,
    phuongThucThanhToan nvarchar(20) NOT NULL CHECK (phuongThucThanhToan IN ('CHUYEN_KHOAN', 'TIEN_MAT')),
    thueVAT decimal(5, 2) DEFAULT 0 CHECK (thueVAT >= 0),
    tienGiam decimal(18, 2),
    tongTien decimal(18, 2) NOT NULL
);

-- 11. Chi Tiết Hóa Đơn
CREATE TABLE ChiTietHoaDon (
    maHoaDon nvarchar(20) REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
    maThuoc nvarchar(20) REFERENCES Thuoc(maThuoc),
    donGia decimal(18, 2) NOT NULL,
    soLuong int NOT NULL,
    thanhTien decimal(18, 2) NOT NULL,
    PRIMARY KEY (maHoaDon, maThuoc)
);

-- 12. Phiếu Nhập Kho
CREATE TABLE PhieuNhapKho (
    maPhieu nvarchar(20) PRIMARY KEY,
    ngayNhap datetime DEFAULT GETDATE(),
    maNhanVien nvarchar(20) REFERENCES NhanVien(maNhanVien),
    maNhaCungCap nvarchar(20) REFERENCES NhaCungCap(maNhaCungCap),
    lyDo nvarchar(200)
);

-- 13. Chi Tiết Phiếu Nhập
CREATE TABLE ChiTietPhieuNhap (
    maPhieu nvarchar(20) REFERENCES PhieuNhapKho(maPhieu) ON DELETE CASCADE,
    maThuoc nvarchar(20) REFERENCES Thuoc(maThuoc),
    soLuong int NOT NULL CHECK (soLuong > 0),
    donGia float NOT NULL CHECK (donGia >= 0),
    PRIMARY KEY (maPhieu, maThuoc)
);

-- 14. Khuyến Mãi
CREATE TABLE KhuyenMai (
    maKhuyenMai nvarchar(20) PRIMARY KEY,
    tenKhuyenMai nvarchar(100) NOT NULL,
    phanTramGiam decimal(5, 2) CHECK (phanTramGiam BETWEEN 0 AND 100),
    tienGiamToiDa decimal(18, 0),
    thoiGianBatDau datetime2(7) NOT NULL,
    thoiGianKetThuc datetime2(7) NOT NULL,
    ngayTao datetime DEFAULT GETDATE(),
    loaiThuocApDung nvarchar(100),
    maNhanVien nvarchar(20) REFERENCES NhanVien(maNhanVien)
);

-- 15. Trả Hàng
CREATE TABLE TraHang (
    maPhieuTra nvarchar(20) PRIMARY KEY,
    maHoaDon nvarchar(20) REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
    lyDotraHang nvarchar(255) NOT NULL,
    ngayTra datetime2(7) NOT NULL,
    trangThai bit NOT NULL -- 1: Đã trả, 0: Hủy
);

-- Chỉ mục (Indexes) để tăng tốc tìm kiếm
CREATE INDEX IX_CaLam_Ngay ON CaLam(ngayLamViec DESC);
CREATE INDEX IDX_PhieuNhap_Ngay ON PhieuNhapKho(ngayNhap DESC);
CREATE INDEX IDX_HoaDon_Ngay ON HoaDon(ngayLap DESC);
GO

--Chèn dữ liệu mẫu 
-- 1. Dữ liệu Ca Làm
INSERT INTO CaLam (maCaLam, tenCaLam, ngayLamViec, gioBatDau, gioKetThuc, viTriLamViec, soLuongNhanVienCan) VALUES 
('CA01', N'Ca Sáng', '2025-12-20', '07:00:00', '12:00:00', N'Quầy thuốc số 1', 2),
('CA02', N'Ca Chiều', '2025-12-20', '12:00:00', '17:00:00', N'Quầy thuốc số 1', 2),
('CA03', N'Ca Tối', '2025-12-20', '17:00:00', '22:00:00', N'Kho thuốc', 1);

-- 2. Dữ liệu Nhân Viên
INSERT INTO NhanVien (maNhanVien, tenNhanVien, gioiTinh, ngaySinh, soDienThoai, hinhAnh_NV, diaChi, email) VALUES 
('NV001', N'Nguyễn Trần Trọng Huy', N'Nam', '2005-12-29', '0919469702', 'ql.png', N'Quận Bình Thạnh, TP.HCM', 'tronghuy2005mn@gmail.com'),
('NV002', N'Nguyễn Hoàng Yến Như', N'Nữ', '2003-11-16', '0707120948', 'nv1.png', N'Quận 1, TP.HCM', 'tranducbo261@gmail.com');

-- 3. Dữ liệu Tài Khoản (tenDangNhap = soDienThoai của Nhân viên)
INSERT INTO TaiKhoan (maNhanVien, tenDangNhap, matKhau, quyenTruyCap, trangThaiTaiKhoan, Email) VALUES 
('NV001', '0919469702', '123', 'QUANLY', 'DANGHOATDONG', 'tronghuy2005mn@gmail.com'),
('NV002', '0707120948', '123', 'DUOCSI', 'DANGHOATDONG', 'tranducbo261@gmail.com');

--4 Dữ liệu khách hàng
INSERT INTO KhachHang (maKhachHang, hoTen, gioiTinh, soDienThoai, loaiKhachHang, diemTichLuy) VALUES 
('KH001', N'Nguyễn Thị Thu', 0, '0911223344', 'VIP', 100),
('KH002', N'Lê Văn Bình', 1, '0933445566', 'BINHTHUONG', 50),
('KH003', N'Nguyễn An Nam', 1, '0901112222', 'BINHTHUONG', 5), -- Thay vãng lai
('KH004', N'Trần Thanh Hải', 1, '0944556677', 'VIP', 250),
('KH005', N'Phạm Mỹ Linh', 0, '0955667788', 'BINHTHUONG', 20),
('KH006', N'Hoàng Anh Tuấn', 1, '0966778899', 'BINHTHUONG', 15),
('KH007', N'Vũ Ngọc Lan', 0, '0977889900', 'VIP', 300),
('KH008', N'Đặng Quốc Bảo', 1, '0988990011', 'BINHTHUONG', 45),
('KH009', N'Bùi Tuyết Mai', 0, '0912345678', 'VIP', 120),
('KH010', N'Ngô Gia Huy', 1, '0922334455', 'BINHTHUONG', 10),
('KH011', N'Lý Minh Triết', 1, '0933557799', 'VIP', 500),
('KH012', N'Chu Kim Liên', 0, '0944668800', 'BINHTHUONG', 35),
('KH013', N'Trần Thị Bưởi', 0, '0903334444', 'BINHTHUONG', 0), -- Thay vãng lai
('KH014', N'Đỗ Hữu Nghĩa', 1, '0966112233', 'BINHTHUONG', 60),
('KH015', N'Tạ Minh Tâm', 0, '0977223344', 'VIP', 180),
('KH016', N'Võ Hoài Nam', 1, '0988334455', 'BINHTHUONG', 25),
('KH017', N'Diệp Bảo Ngọc', 0, '0911445566', 'VIP', 220),
('KH018', N'Lương Gia Bình', 1, '0922556677', 'BINHTHUONG', 40),
('KH019', N'Trương Vĩnh Ký', 1, '0933667788', 'BINHTHUONG', 12),
('KH020', N'Quách Thái Công', 1, '0944778899', 'VIP', 400),
('KH021', N'Phan Thanh Giản', 1, '0955889900', 'BINHTHUONG', 5),
('KH022', N'Đoàn Thị Điểm', 0, '0966990011', 'VIP', 150),
('KH023', N'Lê Quang Định', 1, '0905556666', 'BINHTHUONG', 0), -- Thay vãng lai
('KH024', N'Hồ Xuân Hương', 0, '0988001122', 'BINHTHUONG', 80),
('KH025', N'Cao Bá Quát', 1, '0911559922', 'VIP', 600),
('KH026', N'Nguyễn Du', 1, '0922660033', 'BINHTHUONG', 95),
('KH027', N'Bà Huyện Thanh Quan', 0, '0933771144', 'VIP', 350),
('KH028', N'Tú Xương', 1, '0944882255', 'BINHTHUONG', 30),
('KH029', N'Nguyễn Khuyến', 1, '0955993366', 'BINHTHUONG', 55),
('KH030', N'Nguyễn Đình Chiểu', 1, '0966004477', 'VIP', 450);

--4. Dữ liệu nhà cung cấp
INSERT INTO NhaCungCap (maNhaCungCap, tenNhaCungCap, soDienThoai, email, diaChi) VALUES 
('NCC01', N'Công ty Dược Hậu Giang (DHG)', '02923891433', 'dhgpharma@dhgpharma.com.vn', N'288 Bis Nguyễn Văn Cừ, Cần Thơ'),
('NCC02', N'Tập đoàn Traphaco', '18006612', 'info@traphaco.com.vn', N'75 Yên Ninh, Ba Đình, Hà Nội'),
('NCC03', N'Dược phẩm OPC', '02838777927', 'info@opcpharma.com', N'1017 Hồng Bàng, Quận 6, TP.HCM');

--5. Dữ liệu kho
INSERT INTO KhoHang (maKho, tenKho, ngayNhap, email, soLuong, trangThai) VALUES 
('K01', N'Kho Thuốc Nội', '2025-12-01', 'khonoi@pharmacy.com', 1500, 'CON_HANG'),
('K02', N'Kho Thuốc Ngoại (Nhập)', '2025-12-05', 'khongoai@pharmacy.com', 800, 'TON_KHO'),
('K03', N'Kho Thực Phẩm Chức Năng', '2025-12-10', 'khotpcn@pharmacy.com', 1200, 'CON_HANG');

-- 6. Dữ liệu thuốc
INSERT INTO Thuoc (maThuoc, tenThuoc, giaNhap, giaBan, SoLuong, DonViNhapKho, DonViBan, hanSuDung, trangThaiTonKho, maKho, maNhaCungCap, loaiThuoc, phanLoai) VALUES 
-- Nhóm Giảm đau - Hạ sốt (Kho K01)
('T001', N'Paracetamol 500mg', 500, 1500, 1000, N'HOP', N'VIEN', '2027-12-30', N'Còn hàng', 'K01', 'NCC01', N'Hạ sốt', 'OTC'),
('T002', N'Panadol Extra', 2000, 3500, 500, N'HOP', N'VIEN', '2026-05-15', N'Còn hàng', 'K01', 'NCC02', N'Giảm đau', 'OTC'),
('T003', N'Efferalgan 500mg', 1000, 2500, 800, N'HOP', N'VIEN', '2026-12-01', N'Còn hàng', 'K01', 'NCC01', N'Hạ sốt', 'OTC'),
('T004', N'Hapacol 650', 800, 2000, 450, N'HOP', N'VIEN', '2027-01-15', N'Còn hàng', 'K01', 'NCC02', N'Hạ sốt', 'OTC'),
('T005', N'Alaxan', 2500, 4500, 150, N'HOP', N'VIEN', '2026-08-05', N'Còn hàng', 'K01', 'NCC02', N'Giảm đau cơ', 'OTC'),
('T006', N'Gofen 400', 3000, 5500, 120, N'HOP', N'VIEN', '2027-04-20', N'Còn hàng', 'K01', 'NCC03', N'Giảm đau', 'OTC'),
('T007', N'Voltaren 50mg', 4500, 7000, 90, N'HOP', N'VIEN', '2026-10-10', N'Cần kiểm soát', 'K01', 'NCC01', N'Kháng viêm', 'RX'),
('T008', N'Mobic 7.5mg', 8000, 12000, 80, N'HOP', N'VIEN', '2027-02-14', N'Cần kiểm soát', 'K01', 'NCC03', N'Xương khớp', 'RX'),
('T009', N'Celebrex 200mg', 15000, 25000, 60, N'HOP', N'VIEN', '2026-11-30', N'Cần kiểm soát', 'K01', 'NCC02', N'Xương khớp', 'RX'),
('T010', N'Aspirin 81mg', 500, 1200, 1000, N'HOP', N'VIEN', '2027-08-15', N'Còn hàng', 'K01', 'NCC01', N'Tim mạch', 'OTC'),

-- Nhóm Kháng sinh - Kháng viêm (Kho K02)
('T011', N'Amoxicillin 500mg', 1200, 2500, 300, N'HOP', N'VIEN', '2026-11-20', N'Còn hàng', 'K02', 'NCC03', N'Kháng sinh', 'RX'),
('T012', N'Augmentin 1g', 15000, 22000, 100, N'HOP', N'VIEN', '2026-08-14', N'Còn hàng', 'K02', 'NCC02', N'Kháng sinh', 'RX'),
('T013', N'Cefalexin 500mg', 1500, 3000, 400, N'HOP', N'VIEN', '2026-10-20', N'Còn hàng', 'K02', 'NCC01', N'Kháng sinh', 'RX'),
('T014', N'Zinnat 500mg', 22000, 28000, 80, N'HOP', N'VIEN', '2026-08-20', N'Còn hàng', 'K02', 'NCC02', N'Kháng sinh', 'RX'),
('T015', N'Klamentin 625', 4000, 7000, 150, N'HOP', N'VIEN', '2026-11-30', N'Còn hàng', 'K02', 'NCC01', N'Kháng sinh', 'RX'),
('T016', N'Azithromycin 500', 8000, 14000, 120, N'HOP', N'VIEN', '2027-04-12', N'Còn hàng', 'K02', 'NCC03', N'Kháng sinh', 'RX'),
('T017', N'Cefuroxim 500mg', 5500, 9500, 200, N'HOP', N'VIEN', '2027-02-10', N'Còn hàng', 'K02', 'NCC02', N'Kháng sinh', 'RX'),
('T018', N'Clarithromycin 500', 7000, 12000, 100, N'HOP', N'VIEN', '2026-12-05', N'Còn hàng', 'K02', 'NCC01', N'Kháng sinh', 'RX'),
('T019', N'Levofloxacin 500', 12000, 18000, 70, N'HOP', N'VIEN', '2026-09-18', N'Còn hàng', 'K02', 'NCC03', N'Kháng sinh', 'RX'),
('T020', N'Medrol 4mg', 4000, 6500, 250, N'HOP', N'VIEN', '2027-01-25', N'Còn hàng', 'K02', 'NCC01', N'Kháng viêm', 'RX'),

-- Nhóm Hô hấp - Tiêu hóa (Kho K01/K02)
('T021', N'Siro Ho Prospan', 55000, 75000, 50, N'CHAI', N'CHAI', '2026-10-10', N'Còn hàng', 'K01', 'NCC02', N'Siro ho', 'OTC'),
('T022', N'Eugica xanh', 400, 1200, 1000, N'HOP', N'VIEN', '2027-04-10', N'Bán chạy', 'K01', 'NCC03', N'Ho thảo dược', 'OTC'),
('T023', N'Acemuc 200mg', 3500, 6000, 100, N'HOP', N'GOI', '2026-10-30', N'Còn hàng', 'K01', 'NCC01', N'Long đờm', 'OTC'),
('T024', N'Bisolsolvon 8mg', 4000, 7000, 90, N'HOP', N'VIEN', '2026-09-12', N'Còn hàng', 'K01', 'NCC02', N'Long đờm', 'OTC'),
('T025', N'Maalox', 2000, 4000, 180, N'HOP', N'VIEN', '2027-03-01', N'Còn hàng', 'K02', 'NCC03', N'Dạ dày', 'OTC'),
('T026', N'Phosphalugel', 4500, 7500, 300, N'HOP', N'GOI', '2027-01-20', N'Còn hàng', 'K02', 'NCC01', N'Dạ dày', 'OTC'),
('T027', N'Smecta', 4000, 6500, 250, N'HOP', N'GOI', '2026-11-15', N'Còn hàng', 'K02', 'NCC02', N'Tiêu hóa', 'OTC'),
('T028', N'Berberin 100mg', 200, 800, 2000, N'CHAI', N'VIEN', '2028-01-01', N'Còn hàng', 'K02', 'NCC03', N'Tiêu hóa', 'OTC'),
('T029', N'Motilium M', 2500, 4500, 200, N'HOP', N'VIEN', '2027-05-12', N'Còn hàng', 'K02', 'NCC01', N'Chống nôn', 'OTC'),
('T030', N'Loperamid 2mg', 500, 1500, 150, N'HOP', N'VIEN', '2026-12-10', N'Còn hàng', 'K02', 'NCC02', N'Tiêu hóa', 'OTC'),

-- Nhóm Vitamin - TPCN (Kho K03)
('T031', N'Vitamin C Enervon', 1500, 3000, 600, N'HOP', N'VIEN', '2027-03-22', N'Còn hàng', 'K03', 'NCC03', N'Bổ sung', 'VITAMIN'),
('T032', N'Dầu cá Omega 3', 120000, 185000, 40, N'CHAI', N'VIEN', '2027-05-01', N'Bán chạy', 'K03', 'NCC01', N'Bổ mắt', 'TPCN'),
('T033', N'Calcium D3', 3500, 6000, 400, N'HOP', N'VIEN', '2027-11-10', N'Còn hàng', 'K03', 'NCC02', N'Xương khớp', 'VITAMIN'),
('T034', N'Berooca Eff', 8000, 12500, 100, N'TUYP', N'VIEN', '2026-12-20', N'Còn hàng', 'K03', 'NCC03', N'Bổ sung', 'VITAMIN'),
('T035', N'Vitamin E 400IU', 4500, 8000, 200, N'HOP', N'VIEN', '2027-06-15', N'Còn hàng', 'K03', 'NCC01', N'Đẹp da', 'VITAMIN'),
('T036', N'Sắt Fumarat', 2000, 4000, 300, N'HOP', N'VIEN', '2027-02-28', N'Còn hàng', 'K03', 'NCC02', N'Bổ máu', 'VITAMIN'),
('T037', N'Magie B6', 1500, 3500, 500, N'HOP', N'VIEN', '2027-09-12', N'Còn hàng', 'K03', 'NCC03', N'Thần kinh', 'VITAMIN'),
('T038', N'Boganic', 2500, 4500, 600, N'HOP', N'VIEN', '2027-10-05', N'Bán chạy', 'K03', 'NCC02', N'Giải độc gan', 'TPCN'),
('T039', N'Ginkgo Biloba', 5000, 9000, 250, N'HOP', N'VIEN', '2027-12-01', N'Còn hàng', 'K03', 'NCC01', N'Tuần hoàn não', 'TPCN'),
('T040', N'Kẽm Gluconat', 1800, 3800, 400, N'HOP', N'VIEN', '2027-01-20', N'Còn hàng', 'K03', 'NCC03', N'Bổ sung', 'VITAMIN'),

-- Nhóm Khác (Da liễu, Nhỏ mắt...)
('T041', N'Nước muối sinh lý', 3000, 6000, 500, N'CHAI', N'CHAI', '2028-05-10', N'Còn hàng', 'K01', 'NCC01', N'Vệ sinh', 'OTC'),
('T042', N'V.Rohto 13ml', 45000, 58000, 100, N'HOP', N'CHAI', '2026-11-25', N'Còn hàng', 'K01', 'NCC02', N'Nhỏ mắt', 'OTC'),
('T043', N'Silkron 10g', 12000, 18000, 80, N'TUYP', N'TUYP', '2026-12-30', N'Còn hàng', 'K01', 'NCC03', N'Da liễu', 'OTC'),
('T044', N'Salonpas (Gói 10)', 15000, 22000, 300, N'HOP', N'GOI', '2027-03-14', N'Còn hàng', 'K01', 'NCC02', N'Giảm đau', 'OTC'),
('T045', N'Povidine 20ml', 12000, 16000, 150, N'CHAI', N'CHAI', '2028-01-01', N'Còn hàng', 'K01', 'NCC01', N'Sát trùng', 'OTC'),
('T046', N'Hiruscar 5g', 110000, 145000, 40, N'TUYP', N'TUYP', '2027-06-20', N'Còn hàng', 'K03', 'NCC03', N'Trị sẹo', 'TPCN'),
('T047', N'Canesten 20g', 45000, 65000, 60, N'TUYP', N'TUYP', '2026-10-15', N'Còn hàng', 'K01', 'NCC02', N'Da liễu', 'OTC'),
('T048', N'Oresol (Gói)', 2000, 4000, 1000, N'HOP', N'GOI', '2027-05-30', N'Còn hàng', 'K01', 'NCC01', N'Bù nước', 'TPCN'),
('T049', N'Becozyme', 1500, 3000, 450, N'HOP', N'VIEN', '2027-02-12', N'Còn hàng', 'K03', 'NCC03', N'Bổ sung', 'VITAMIN'),
('T050', N'Laroscorbine', 5000, 9000, 100, N'HOP', N'VIEN', '2026-12-10', N'Còn hàng', 'K03', 'NCC02', N'Bổ sung', 'VITAMIN');

-- Chạy lại đoạn tạo
-- 7,8. Dữ liệu Hóa đơn và Chi tiết hóa đơn
DECLARE @i INT = 1;
DECLARE @maHD NVARCHAR(20);
DECLARE @ngayNgau DATETIME2;
DECLARE @nhanVien NVARCHAR(20);
DECLARE @khachHang NVARCHAR(20);
SET NOCOUNT ON
WHILE @i <= 300
BEGIN
    SET @maHD = 'HD' + RIGHT('000' + CAST(@i AS VARCHAR), 3);
    
    -- Tạo ngày ngẫu nhiên trong năm 2025
    SET @ngayNgau = DATEADD(SECOND, ABS(CHECKSUM(NEWID()) % DATEDIFF(SECOND, '2025-01-01', GETDATE())), '2025-01-01');
    
    -- Chọn nhân viên (Huy hoặc Như)
    SET @nhanVien = CASE WHEN RAND() > 0.5 THEN 'NV001' ELSE 'NV002' END;
    
    -- Chọn khách hàng ngẫu nhiên (KH001 - KH030)
    SET @khachHang = 'KH' + RIGHT('000' + CAST(CAST(RAND()*29 + 1 AS INT) AS VARCHAR), 3);

    -- 1. Chèn bảng HoaDon
    INSERT INTO HoaDon (maHoaDon, maKhachHang, ngayLap, phuongThucThanhToan, thueVAT, maNhanVien, tongTien, tienGiam)
    VALUES (@maHD, @khachHang, @ngayNgau, 
           CASE WHEN RAND() > 0.3 THEN 'TIEN_MAT' ELSE 'CHUYEN_KHOAN' END, 
           5.00, @nhanVien, 0, 0);

    -- 2. Chèn 1-3 chi tiết cho hóa đơn này
    DECLARE @j INT = 1;
    DECLARE @soMon INT = CAST(RAND()*2 + 1 AS INT); -- 1 đến 3 món
    
    WHILE @j <= @soMon
    BEGIN
        DECLARE @maThuoc NVARCHAR(20) = 'T' + RIGHT('000' + CAST(CAST(RAND()*49 + 1 AS INT) AS VARCHAR), 3);
        DECLARE @giaBan DECIMAL(18,2) = (SELECT giaBan FROM Thuoc WHERE maThuoc = @maThuoc);
        DECLARE @sl INT = CAST(RAND()*5 + 1 AS INT); -- Mua từ 1-5 đơn vị

        -- Tránh trùng thuốc trong cùng 1 hóa đơn
        IF NOT EXISTS (SELECT 1 FROM ChiTietHoaDon WHERE maHoaDon = @maHD AND maThuoc = @maThuoc)
        BEGIN
            INSERT INTO ChiTietHoaDon (maHoaDon, maThuoc, donGia, soLuong, thanhTien)
            VALUES (@maHD, @maThuoc, @giaBan, @sl, @giaBan * @sl);
        END
        SET @j = @j + 1;
    END

    -- 3. Cập nhật lại tổng tiền hóa đơn sau khi đã có chi tiết
    UPDATE HoaDon 
    SET tongTien = (SELECT SUM(thanhTien) FROM ChiTietHoaDon WHERE maHoaDon = @maHD) * 1.05
    WHERE maHoaDon = @maHD;

    SET @i = @i + 1;
END;
SET NOCOUNT OFF
GO

-- 9. Chèn dữ liệu Khuyến Mãi (Dành cho các sự kiện trong năm 2025)
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, phanTramGiam, tienGiamToiDa, thoiGianBatDau, thoiGianKetThuc, maNhanVien, ngayTao, loaiThuocApDung) VALUES 
('KM001', N'Chào Năm Mới 2025', 10.00, 50000, '2025-01-01', '2025-01-05', 'NV001', '2025-01-01', N'Tất cả'),
('KM002', N'Giải Nhiệt Mùa Hè', 5.00, 20000, '2025-06-01', '2025-06-30', 'NV001', '2025-05-25', N'Vitamin'),
('KM003', N'Giáng Sinh An Lành', 15.00, 100000, '2025-12-20', '2025-12-26', 'NV001', '2025-12-15', N'Tất cả');

-- 10. Chèn dữ liệu Phiếu Nhập Kho
DECLARE @m INT = 1;
WHILE @m <= 12
BEGIN
    DECLARE @maPN NVARCHAR(20) = 'PN' + RIGHT('00' + CAST(@m AS VARCHAR), 2);
    INSERT INTO PhieuNhapKho (maPhieu, ngayNhap, maNhanVien, maNhaCungCap, lyDo)
    VALUES (@maPN, CAST('2025-' + CAST(@m AS VARCHAR) + '-05' AS DATETIME), 'NV001', 
            CASE WHEN @m % 3 = 0 THEN 'NCC01' WHEN @m % 3 = 1 THEN 'NCC02' ELSE 'NCC03' END, 
            N'Nhập hàng định kỳ tháng ' + CAST(@m AS VARCHAR));

    -- Chèn chi tiết phiếu nhập (Mỗi phiếu nhập khoảng 5 loại thuốc ngẫu nhiên)
    INSERT INTO ChiTietPhieuNhap (maPhieu, maThuoc, soLuong, donGia)
    SELECT TOP 5 @maPN, maThuoc, 100, giaNhap FROM Thuoc ORDER BY NEWID();
    
    SET @m = @m + 1;
END

-- 11. Chèn dữ liệu Lịch Làm
DECLARE @d INT = 1;
WHILE @d <= 31
BEGIN
    DECLARE @ngay date = DATEADD(DAY, @d-1, '2025-12-01');
    INSERT INTO LichLam (maLichLam, maNhanVien, maCaLam, ngayLam, trangThaiCaLam) VALUES 
    ('LL' + RIGHT('000' + CAST(@d*2-1 AS VARCHAR), 3), 'NV001', 'CA01', @ngay, N'Hoàn thành'),
    ('LL' + RIGHT('000' + CAST(@d*2 AS VARCHAR), 3), 'NV002', 'CA02', @ngay, N'Hoàn thành');
    SET @d = @d + 1;
END

-- 12. Chèn dữ liệu Trả Hàng (Giả định có một vài đơn hàng bị lỗi)
INSERT INTO TraHang (maPhieuTra, maHoaDon, lyDotraHang, ngayTra, trangThai) VALUES 
('PT001', 'HD005', N'Thuốc hết hạn sử dụng', '2025-02-15', 1),
('PT002', 'HD050', N'Nhầm loại thuốc', '2025-05-20', 1),
('PT003', 'HD120', N'Khách đổi ý', '2025-08-10', 0); -- Trạng thái 0: Hủy yêu cầu trả

-- 13. Chèn dữ liệu Điểm Tích Lũy (Dành cho các khách hàng VIP)
INSERT INTO DiemTichLuy (maKH, ngayPhatSinh, ngayHetHan)
SELECT maKhachHang, '2025-01-01', '2026-01-01' FROM KhachHang WHERE loaiKhachHang = 'VIP';

SET NOCOUNT OFF;
GO