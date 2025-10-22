--Tạo database
CREATE DATABASE ThienLuong
GO

--Chọn database vừa tạo
USE [ThienLuong]
GO

--Tạo bảng 
--1. Bảng CaLam
CREATE TABLE CaLam (
    maCa VARCHAR(10) PRIMARY KEY,
    tenCa NVARCHAR(50) NOT NULL,
    thoiGianBatDau TIME NOT NULL,
    thoiGianKetThuc TIME NOT NULL
)
GO

--2. Bảng KhachHang
CREATE TABLE KhachHang (
    maKH VARCHAR(15) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    gioiTinh BIT NOT NULL,
    loaiKhachHang NVARCHAR(50) NOT NULL, 
    diemTichLuy INT NOT NULL DEFAULT 0
);
GO

-- Cập nhật lại Khóa ngoại trong bảng HoaDon (nếu đã xóa ở trên)
ALTER TABLE HoaDon ADD CONSTRAINT FK_HoaDon_KhachHang 
    FOREIGN KEY (maKH) 
    REFERENCES KhachHang(maKH);
GO

--3. Bảng NhanVien
CREATE TABLE NhanVien (
    maNV VARCHAR(10) PRIMARY KEY,
    tenNV NVARCHAR(100) NOT NULL,
    gioiTinh BIT NOT NULL,  --(1: Nam, 0: Nữ)
    ngaySinh DATE NOT NULL,
    soDienThoai VARCHAR(15) UNIQUE NOT NULL
);
GO

--4. Bảng TaiKhoan
CREATE TABLE TaiKhoan (
    tenDangNhap VARCHAR(15) PRIMARY KEY,   --(la SDT cua nhan vien)
    matKhau VARCHAR(255) NOT NULL,
    quyenTruyCap NVARCHAR(50) NOT NULL,
    trangThai NVARCHAR(50) NOT NULL,
    
    CONSTRAINT FK_TaiKhoan_NhanVien_SDT 
        FOREIGN KEY (tenDangNhap) 
        REFERENCES NhanVien(soDienThoai) 
        ON DELETE CASCADE
);
GO

--5. Bảng LichLam
CREATE TABLE LichLam (
    maLichLam VARCHAR(10) PRIMARY KEY,
    ngayLam DATE NOT NULL,
    maCa VARCHAR(10) NOT NULL,
    maNV VARCHAR(10) NOT NULL,
    
	--Đảm bảo một nhân viên không thể làm cùng một ca hai lần trong một ngày.
	CONSTRAINT UQ_LichLam_NgayCaNV UNIQUE (ngayLam, maCa, maNV),

    CONSTRAINT FK_LichLam_CaLam 
        FOREIGN KEY (maCa) 
        REFERENCES CaLam(maCa),
        
    CONSTRAINT FK_LichLam_NhanVien 
        FOREIGN KEY (maNV) 
        REFERENCES NhanVien(maNV)
);
GO

--6. Bảng NhaCungCap
CREATE TABLE NhaCungCap (
    maNCC VARCHAR(10) PRIMARY KEY,
    tenNCC NVARCHAR(200) NOT NULL,
    diaChi NVARCHAR(255),
    soDienThoai VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE
);
GO

--7. Bảng KhoHang
CREATE TABLE KhoHang (
    maKho VARCHAR(10) PRIMARY KEY,
    tenKho NVARCHAR(100) NOT NULL,
    diaChi NVARCHAR(255)
);
GO

--8. Bảng Thuoc
CREATE TABLE Thuoc (
    -- Thuộc tính cơ bản
    maThuoc VARCHAR(15) PRIMARY KEY,
    tenThuoc NVARCHAR(200) NOT NULL,
    giaBan DECIMAL(18, 2) NOT NULL,
    giaNhap DECIMAL(18, 2) NOT NULL,
    soLuong INT NOT NULL DEFAULT 0,
    hanSuDung DATE,
    trangThaiTonKho NVARCHAR(50) NOT NULL,
    donViTinh NVARCHAR(50) NOT NULL,
    maNCC VARCHAR(10) NOT NULL,
    maKho VARCHAR(10) NOT NULL,
    
    CONSTRAINT FK_Thuoc_NhaCungCap 
        FOREIGN KEY (maNCC) 
        REFERENCES NhaCungCap(maNCC),
        
    CONSTRAINT FK_Thuoc_KhoHang 
        FOREIGN KEY (maKho) 
        REFERENCES KhoHang(maKho),
        
    -- Ràng buộc kiểm tra (Kiểm tra giá bán > giá nhập)
    CONSTRAINT CHK_GiaBan_LonHon_GiaNhap 
        CHECK (giaBan >= giaNhap)
);
GO

--9. Bảng KhuyenMai
CREATE TABLE KhuyenMai (
    maKM VARCHAR(10) PRIMARY KEY,
    tenKM NVARCHAR(100) NOT NULL,
    phanTramGiam DECIMAL(5, 2) NOT NULL,
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,

    -- Ràng buộc kiểm tra: phần trăm giảm phải nằm trong khoảng hợp lệ (0% đến 100%)
    CONSTRAINT CHK_PhanTramGiam CHECK (phanTramGiam >= 0 AND phanTramGiam <= 100),
    
    -- Ràng buộc kiểm tra: ngày kết thúc phải sau ngày bắt đầu
    CONSTRAINT CHK_NgayHopLe CHECK (ngayKetThuc >= ngayBatDau)
);
GO

--10. Bảng HoaDon
CREATE TABLE HoaDon (
    maHD VARCHAR(15) PRIMARY KEY,
    maNV VARCHAR(10) NOT NULL,
    maKH VARCHAR(15),
    maKM VARCHAR(10), 
    ngayLap DATE NOT NULL,
    phuongThucThanhToan NVARCHAR(50) NOT NULL,
    
    -- Định nghĩa Khóa ngoại
    CONSTRAINT FK_HoaDon_NhanVien
        FOREIGN KEY (maNV) 
        REFERENCES NhanVien(maNV),
        
    CONSTRAINT FK_HoaDon_KhachHang
        FOREIGN KEY (maKH) 
        REFERENCES KhachHang(maKH),
        
    CONSTRAINT FK_HoaDon_KhuyenMai
        FOREIGN KEY (maKM) 
        REFERENCES KhuyenMai(maKM)
);
GO

--11. ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    maHD VARCHAR(15) NOT NULL,
    maThuoc VARCHAR(15) NOT NULL,
    PRIMARY KEY (maHD, maThuoc), 
    loaiKhachHang NVARCHAR(50) NOT NULL,
    tienGiam DECIMAL(18, 2) NOT NULL DEFAULT 0,
    soLuong INT NOT NULL,
    donGia DECIMAL(18, 2) NOT NULL,
    
    -- Mối quan hệ 1:N với HoaDon
    CONSTRAINT FK_CTHD_HoaDon
        FOREIGN KEY (maHD) 
        REFERENCES HoaDon(maHD)
        ON DELETE CASCADE, -- Xóa hóa đơn thì xóa chi tiết
        
    -- Mối quan hệ với Thuoc
    CONSTRAINT FK_CTHD_Thuoc
        FOREIGN KEY (maThuoc) 
        REFERENCES Thuoc(maThuoc),
        
    -- Ràng buộc kiểm tra: Số lượng phải lớn hơn 0
    CONSTRAINT CHK_SoLuong_Duong
        CHECK (soLuong > 0)
);
GO

--12. DonTraHang
CREATE TABLE DonTraHang (
    maDonTra VARCHAR(15) PRIMARY KEY,
    ngayTra DATE NOT NULL,
    lyDoTra NVARCHAR(255),
    trangThai BIT NOT NULL DEFAULT 0, --(0=Chưa xử lý, 1=Đã xử lý)
    
    maHD VARCHAR(15) NOT NULL,
    
    -- Ràng buộc: Một hóa đơn chỉ nên có MỘT đơn trả hàng (nếu nghiệp vụ yêu cầu 1:1)
    
    -- Định nghĩa Khóa ngoại
    CONSTRAINT FK_DonTraHang_HoaDon
        FOREIGN KEY (maHD) 
        REFERENCES HoaDon(maHD)
);
GO


--Thêm dữ liệu mẫu cho các bảng 
--1. Bảng CaLam
INSERT INTO CaLam (maCa, tenCa, thoiGianBatDau, thoiGianKetThuc) VALUES
('CA01', N'Ca Sáng', '07:00:00', '11:00:00'),
('CA02', N'Ca Chiều', '11:00:00', '15:00:00'),
('CA03', N'Ca Tối', '15:00:00', '19:00:00'),
('CA04', N'Ca Hành Chính', '08:00:00', '17:00:00');
GO

--2. Bảng KhachHang
INSERT INTO KhachHang (maKH, tenKH, gioiTinh, loaiKhachHang, diemTichLuy) VALUES
('0945678901', N'Trần Đình Công', 1, N'THUONG', 200),
('0868889990', N'Vũ Phương Linh', 0, N'VIP', 650),
('0791112223', N'Ngô Thế Bảo', 1, N'THUONG', 0),
('0933445566', N'Hồ Thị Trâm', 0, N'VIP', 1500), 
('0950001112', N'Bùi Thanh Tú', 1, N'THUONG', 350); 
GO

--3. Bảng NhanVien
INSERT INTO NhanVien (maNV, tenNV, gioiTinh, ngaySinh, soDienThoai) VALUES
('NV001', N'Trần Văn Hùng', 1, '1995-05-15', '0901222333'),
('NV002', N'Lê Thị Lan', 0, '1998-11-20', '0912333444'),
('NV003', N'Hoàng Minh Đức', 1, '2000-01-01', '0384445555');
GO

--4. Bảng TaiKhoan
-- Bước 19: Thêm dữ liệu mẫu cho bảng TaiKhoan
INSERT INTO TaiKhoan (tenDangNhap, matKhau, quyenTruyCap, trangThai) VALUES
('0901222333', 'mk123', N'QUAN_LY', N'HOAT_DONG'),
('0912333444', 'mk123', N'NHAN_VIEN', N'HOAT_DONG'),
('0384445555', 'mk123', N'NHAN_VIEN', N'HOAT_DONG');
GO

--5. Bảng LichLam
-- Bước 20: Thêm dữ liệu mẫu cho bảng LichLam
INSERT INTO LichLam (maLichLam, ngayLam, maCa, maNV) VALUES
-- NV001 làm 3 ca khác nhau
('LL001', '2025-10-25', 'CA01', 'NV001'), -- NV001: Ca Sáng 25/10
('LL002', '2025-10-25', 'CA03', 'NV001'), -- NV001: Ca Tối 25/10 (Được phép vì là ca khác)
('LL003', '2025-10-26', 'CA02', 'NV001'), -- NV001: Ca Chiều 26/10

-- NV002 làm ca cố định
('LL004', '2025-10-25', 'CA02', 'NV002'), -- NV002: Ca Chiều 25/10
('LL005', '2025-10-26', 'CA02', 'NV002'), -- NV002: Ca Chiều 26/10

-- NV003 làm ca hành chính và ca sáng
('LL006', '2025-10-27', 'CA04', 'NV003'), -- NV003: Ca HC 27/10
('LL007', '2025-10-28', 'CA01', 'NV003'); -- NV003: Ca Sáng 28/10
GO

--6. Bảng NhaCungCap
INSERT INTO NhaCungCap (maNCC, tenNCC, diaChi, soDienThoai, email) VALUES
('NCC01', N'Công ty Dược phẩm ABC', N'123 Đường Tôn Đức Thắng, Q1, TP.HCM', '02812345678', 'abcpharma@email.com'),
('NCC02', N'Dược phẩm Thế Kỷ Mới', N'456 Đường Hùng Vương, Q5, TP.HCM', '02898765432', 'thekymoi@email.com'),
('NCC03', N'Tập đoàn Dược Việt', N'789 Đường Lê Lợi, Hà Nội', '02456789012', 'duocviet@email.com');
GO

--7. Bảng KhoHang
INSERT INTO KhoHang (maKho, tenKho, diaChi) VALUES
('K01', N'Kho chính TP.HCM', N'Lô B, Khu công nghiệp Tân Bình'),
('K02', N'Kho dự trữ Miền Bắc', N'Số 10, Đường Phạm Văn Đồng, Hà Nội');
GO

--8. Bảng Thuoc
INSERT INTO Thuoc (maThuoc, tenThuoc, giaBan, giaNhap, soLuong, hanSuDung, trangThaiTonKho, donViTinh, maNCC, maKho) VALUES
('T001', N'Paracetamol 500mg', 5000.00, 3000.00, 1500, '2026-12-31', N'CON_HANG', N'VIEN', 'NCC01', 'K01'),
('T002', N'Thuốc ho Bổ Phổi', 75000.00, 50000.00, 200, '2027-06-30', N'CON_HANG', N'CHAI', 'NCC02', 'K01'),
('T003', N'Kháng sinh Amoxicillin', 150000.00, 100000.00, 50, '2025-11-15', N'SAP_HET_HANG', N'HOP', 'NCC01', 'K02'),
('T004', N'Vitamin C', 25000.00, 15000.00, 800, '2028-01-20', N'CON_HANG', N'GOI', 'NCC03', 'K01'),
('T005', N'Dầu nóng Trường Sơn', 40000.00, 25000.00, 10, '2029-05-01', N'SAP_HET_HANG', N'CHAI', 'NCC02', 'K02');
GO

--9. Bảng KhuyenMai
INSERT INTO KhuyenMai (maKM, tenKM, phanTramGiam, ngayBatDau, ngayKetThuc) VALUES
('KM001', N'Giảm 10% Khách hàng VIP', 10.00, '2025-10-01', '2025-12-31'),
('KM002', N'Giảm 5% Khách hàng Thường', 5.00, '2025-10-15', '2025-11-15')
GO

--10. Bảng HoaDon
INSERT INTO HoaDon (maHD, maNV, maKH, maKM, ngayLap, phuongThucThanhToan) VALUES
('HD0001', 'NV001', '0791112223', 'KM001', '2025-10-24', N'TIEN_MAT'),
('HD0002', 'NV002', '0868889990', NULL, '2025-10-24', N'CHUYEN_KHOAN'),
('HD0003', 'NV001', NULL, 'KM002', '2025-10-24', N'TIEN_MAT'), -- Bản ghi KH vãng lai (NULL)
('HD0004', 'NV003', '0933445566', NULL, '2025-10-25', N'TIEN_MAT');
GO

--11. Bảng ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHD, maThuoc, loaiKhachHang, tienGiam, soLuong, donGia) VALUES
('HD0001', 'T001', N'THUONG', 500.00, 10, 5000.00),     -- 10 viên Paracetamol
('HD0001', 'T002', N'THUONG', 0.00, 1, 75000.00),      -- 1 chai Thuốc ho
('HD0001', 'T004', N'THUONG', 2000.00, 5, 25000.00),      -- 5 gói Vitamin C

('HD0002', 'T003', N'VIP', 0.00, 2, 150000.00),    -- 2 hộp Kháng sinh
('HD0002', 'T005', N'VIP', 0.00, 3, 40000.00),      -- 3 chai Dầu nóng

('HD0003', 'T001', N'VANG_LAI', 0.00, 20, 5000.00),    -- 20 viên Paracetamol
('HD0003', 'T004', N'VANG_LAI', 1000.00, 2, 25000.00),      -- 2 gói Vitamin C

('HD0004', 'T002', N'THANH_VIEN', 0.00, 2, 75000.00),      -- 2 chai Thuốc ho
('HD0004', 'T005', N'THANH_VIEN', 0.00, 1, 40000.00),      -- 1 chai Dầu nóng
('HD0004', 'T003', N'THANH_VIEN', 0.00, 1, 150000.00);     -- 1 hộp Kháng sinh
GO

--12. Bảng DonTraHang
INSERT INTO DonTraHang (maDonTra, ngayTra, lyDoTra, maHD, trangThai) VALUES
('DT0001', '2025-10-25', N'Khách hàng muốn trả lại một phần thuốc do mua dư.', 'HD0002', 0);
GO