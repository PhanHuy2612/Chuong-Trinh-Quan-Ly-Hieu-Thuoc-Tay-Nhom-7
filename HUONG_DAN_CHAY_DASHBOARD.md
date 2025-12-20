# 🚀 HƯỚNG DẪN CHẠY VÀ TEST DASHBOARD

## Bước 1: Chuẩn Bị Database

### Kiểm tra dữ liệu trong database
Mở SQL Server Management Studio và chạy các query sau:

```sql
USE QLThuoc;

-- 1. Kiểm tra số lượng thuốc
SELECT COUNT(*) as 'Tong so thuoc' FROM Thuoc;

-- 2. Kiểm tra trạng thái tồn kho
SELECT 
    trangThaiTonKho, 
    COUNT(*) as SoLuong,
    CAST(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM Thuoc) AS DECIMAL(5,2)) as PhanTram
FROM Thuoc
GROUP BY trangThaiTonKho;

-- 3. Kiểm tra hóa đơn
SELECT COUNT(*) as 'Tong so hoa don' FROM HoaDon;

-- 4. Kiểm tra hóa đơn trong ngày hôm nay
SELECT COUNT(*) as 'Hoa don hom nay' 
FROM HoaDon 
WHERE CAST(ngayLap AS DATE) = CAST(GETDATE() AS DATE);

-- 5. Kiểm tra doanh thu trong ngày
SELECT 
    COUNT(*) as SoHoaDon,
    COUNT(DISTINCT maKhachHang) as SoKhachHang,
    COALESCE(SUM(tongTien), 0) as DoanhThu
FROM HoaDon
WHERE CAST(ngayLap AS DATE) = CAST(GETDATE() AS DATE);
```

### Nếu không có dữ liệu, chạy script tạo dữ liệu mẫu:

```sql
-- Thêm dữ liệu mẫu cho test Dashboard
USE QLThuoc;

-- 1. Đảm bảo có thuốc với nhiều trạng thái khác nhau
UPDATE Thuoc SET trangThaiTonKho = 'CON_HANG' WHERE maThuoc LIKE 'T000[1-5]';
UPDATE Thuoc SET trangThaiTonKho = 'SAP_HET_HANG' WHERE maThuoc LIKE 'T000[6-8]';
UPDATE Thuoc SET trangThaiTonKho = 'HET_HANG' WHERE maThuoc LIKE 'T000[9]';
UPDATE Thuoc SET trangThaiTonKho = 'TON_KHO' WHERE maThuoc LIKE 'T001[0-9]';

-- 2. Thêm hóa đơn mẫu trong nhiều giờ khác nhau (nếu cần)
-- Bạn có thể thêm hóa đơn qua giao diện hoặc SQL

-- 3. Kiểm tra thuốc sắp hết hạn
SELECT maThuoc, tenThuoc, hanSuDung 
FROM Thuoc 
WHERE hanSuDung BETWEEN GETDATE() AND DATEADD(MONTH, 1, GETDATE());
```

---

## Bước 2: Compile và Chạy Ứng Dụng

### Từ IntelliJ IDEA:

1. **Mở project**
   - File → Open → Chọn folder `Chuong-Trinh-Quan-Ly-Hieu-Thuoc-Tay-Nhom-7`

2. **Cấu hình JavaFX** (nếu chưa có)
   - File → Project Structure → Libraries
   - Thêm JavaFX SDK nếu thiếu

3. **Rebuild project**
   - Build → Rebuild Project

4. **Chạy ứng dụng**
   - Click phải vào `frmDangNhap.java`
   - Chọn "Run 'frmDangNhap.main()'"

### Từ Command Line:

```powershell
# Di chuyển vào thư mục project
cd "D:\Chuong-Trinh-Quan-Ly-Hieu-Thuoc-Tay-Nhom-7-Trong1\Chuong-Trinh-Quan-Ly-Hieu-Thuoc-Tay-Nhom-7"

# Compile (cần có javac trong PATH)
javac -cp "lib/*;jdbc/*" -d out src/gui/frmDangNhap.java

# Chạy (cần có java trong PATH)
java -cp "out;lib/*;jdbc/*" gui.frmDangNhap
```

---

## Bước 3: Đăng Nhập

### Tìm tài khoản QUẢN_LÝ hoặc DƯỢC_SĨ trong database:

```sql
-- Tìm tài khoản QUẢN_LÝ
SELECT TOP 1 
    tk.tenDangNhap, 
    tk.matKhau,
    tk.phanQuyen,
    nv.tenNhanVien
FROM TaiKhoan tk
JOIN NhanVien nv ON tk.tenDangNhap = nv.soDienThoai
WHERE tk.phanQuyen = 'QUAN_LY'
  AND tk.trangThai = 'DANGHOATDONG';

-- Hoặc tìm tài khoản DƯỢC_SĨ
SELECT TOP 1 
    tk.tenDangNhap, 
    tk.matKhau,
    tk.phanQuyen,
    nv.tenNhanVien
FROM TaiKhoan tk
JOIN NhanVien nv ON tk.tenDangNhap = nv.soDienThoai
WHERE tk.phanQuyen = 'DUOC_SI'
  AND tk.trangThai = 'DANGHOATDONG';
```

### Đăng nhập:
1. Nhập **tên đăng nhập** (số điện thoại nhân viên)
2. Nhập **mật khẩu**
3. Click **"Đăng nhập"**

**Lưu ý**: Dashboard hiển thị cho cả tài khoản `QUAN_LY` và `DUOC_SI`

---

## Bước 4: Xem Dashboard

### Dashboard sẽ tự động hiển thị:
- Sau khi đăng nhập thành công với quyền `QUAN_LY` hoặc `DUOC_SI`
- Dashboard là màn hình mặc định cho cả hai quyền

### Hoặc click menu "Dashboard":
- Trên sidebar bên trái
- Menu đầu tiên trong danh sách (cả QUẢN_LÝ và DƯỢC_SĨ đều có)

---

## Bước 5: Test Các Chức Năng

### Test 1: Kiểm tra 4 KPI Cards

**Mục tiêu**: Xem các chỉ số hiển thị đúng

**Các bước**:
1. Nhìn vào 4 ô KPI ở hàng trên cùng
2. Kiểm tra:
   - ✅ **Tỷ Lệ Còn Hàng**: Có phần trăm và số lượng?
   - ✅ **Cảnh Báo Tồn Kho**: Có màu đỏ và hiển thị đúng?
   - ✅ **Doanh Thu**: Có hiển thị số tiền?
   - ✅ **Số Hóa Đơn**: Có số lượng hóa đơn và khách hàng?

**Kết quả mong đợi**: Tất cả 4 thẻ đều hiển thị dữ liệu (không phải 0)

---

### Test 2: Kiểm tra Bộ Lọc Thời Gian

**Mục tiêu**: Kiểm tra dữ liệu thay đổi theo bộ lọc

**Các bước**:
1. Click vào dropdown "Xem theo:" ở góc phải trên
2. Chọn **"Hôm nay"** → Quan sát dữ liệu
3. Chọn **"7 ngày"** → Quan sát dữ liệu thay đổi
4. Chọn **"30 ngày"** → Quan sát dữ liệu thay đổi
5. Chọn **"90 ngày"** → Quan sát dữ liệu thay đổi

**Kết quả mong đợi**: 
- Doanh thu và số hóa đơn tăng khi chọn khoảng thời gian dài hơn
- Biểu đồ cập nhật theo

---

### Test 3: Kiểm tra Biểu Đồ Nguồn Doanh Thu

**Mục tiêu**: Xem phân tích doanh thu theo loại thuốc

**Các bước**:
1. Nhìn vào biểu đồ tròn bên trái (dưới KPI cards)
2. Kiểm tra:
   - ✅ Có hiển thị nhiều màu khác nhau?
   - ✅ Có tên loại thuốc và số tiền?
   - ✅ Có legend (chú thích) bên cạnh?

**Kết quả mong đợi**: Biểu đồ tròn với nhiều phần, mỗi phần là 1 loại thuốc

---

### Test 4: Kiểm tra Biểu Đồ Khung Giờ Cao Điểm

**Mục tiêu**: Xem lưu lượng bán hàng theo giờ

**Các bước**:
1. Nhìn vào biểu đồ đường ở giữa
2. Kiểm tra:
   - ✅ Có đường biểu diễn số hóa đơn?
   - ✅ Trục X có hiển thị giờ (8:00, 10:00, ...)?
   - ✅ Trục Y có hiển thị số hóa đơn?
   - ✅ Có điểm nào cao hơn (giờ cao điểm)?

**Kết quả mong đợi**: Biểu đồ đường với các điểm lên xuống theo giờ

---

### Test 5: Kiểm tra Biểu Đồ Tỷ Lệ Bán Theo Loại

**Mục tiêu**: Xem cơ cấu bán hàng OTC vs ETC

**Các bước**:
1. Nhìn vào biểu đồ tròn bên phải
2. Kiểm tra:
   - ✅ Có 2 phần chính (OTC và ETC)?
   - ✅ Có phần trăm hiển thị?
   - ✅ Có màu khác nhau?

**Kết quả mong đợi**: Biểu đồ tròn với 2 phần, hiển thị tỷ lệ phần trăm

---

### Test 6: Kiểm tra Hệ Thống Cảnh Báo

**Mục tiêu**: Xem các cảnh báo về thuốc

**Các bước**:
1. Scroll xuống dưới cùng
2. Xem mục "⚠️ CẢNH BÁO"
3. Kiểm tra:
   - ✅ Có hiển thị cảnh báo 🔴 HẾT HÀNG?
   - ✅ Có hiển thị cảnh báo 🟡 SẮP HẾT?
   - ✅ Có hiển thị cảnh báo ⏰ SẮP HẾT HẠN?
   - ✅ Mỗi cảnh báo có tên thuốc và thông tin?

**Kết quả mong đợi**: 
- Nếu có vấn đề: Hiển thị danh sách cảnh báo
- Nếu không có vấn đề: Hiển thị "✅ Không có cảnh báo"

---

### Test 7: Kiểm tra Tự Động Làm Mới

**Mục tiêu**: Dashboard tự động cập nhật

**Các bước**:
1. Để Dashboard mở
2. Đợi 30 giây
3. Quan sát xem dữ liệu có nhấp nháy/cập nhật không

**Kết quả mong đợi**: Dữ liệu tự động làm mới mỗi 30 giây

---

### Test 8: Kiểm tra Nút Làm Mới

**Mục tiêu**: Làm mới dữ liệu thủ công

**Các bước**:
1. Click nút "🔄 Làm mới" ở góc phải trên
2. Quan sát dữ liệu cập nhật

**Kết quả mong đợi**: Tất cả dữ liệu cập nhật ngay lập tức

---

## Bước 6: Kiểm Tra Các Kịch Bản Thực Tế

### Kịch bản 1: Phát hiện thuốc hết hàng

**Tình huống**: 
- Dashboard hiển thị 🔴 **HẾT HÀNG**: Paracetamol 500mg

**Hành động**:
1. Ghi nhận tên thuốc
2. Click vào menu "Quản lý kho thuốc"
3. Tìm thuốc đó
4. Kiểm tra thông tin
5. Lên kế hoạch nhập hàng

---

### Kịch bản 2: Phân tích doanh thu

**Tình huống**:
- Biểu đồ "Nguồn Doanh Thu" cho thấy:
  - Kháng sinh: 52%
  - Giảm đau: 36%
  - Vitamin: 12%

**Quyết định**:
1. Nhập nhiều Kháng sinh hơn (bán chạy)
2. Làm khuyến mãi cho Vitamin (bán ít)
3. Giữ nguyên Giảm đau

---

### Kịch bản 3: Sắp xếp nhân viên

**Tình huống**:
- Biểu đồ "Khung Giờ Cao Điểm" cho thấy:
  - 8-10h: 5 hóa đơn
  - 12-14h: 25 hóa đơn (cao điểm)
  - 18-20h: 30 hóa đơn (cao điểm nhất)

**Quyết định**:
1. Bố trí 1 nhân viên vào buổi sáng
2. Bố trí 2-3 nhân viên vào trưa
3. Bố trí 3-4 nhân viên vào tối

---

## Xử Lý Lỗi Thường Gặp

### Lỗi 1: Dashboard không hiển thị sau khi đăng nhập

**Nguyên nhân**: Đăng nhập với tài khoản không phải QUẢN_LÝ hoặc DƯỢC_SĨ

**Giải pháp**:
1. Đăng xuất
2. Đăng nhập lại với tài khoản QUẢN_LÝ hoặc DƯỢC_SĨ
3. Hoặc thử click menu "Dashboard" trên sidebar

---

### Lỗi 2: Tất cả KPI hiển thị 0

**Nguyên nhân**: Database không có dữ liệu

**Giải pháp**:
1. Chạy lại các query kiểm tra ở Bước 1
2. Thêm dữ liệu mẫu
3. Click "🔄 Làm mới"

---

### Lỗi 3: Biểu đồ trống

**Nguyên nhân**: Không có dữ liệu trong khoảng thời gian

**Giải pháp**:
1. Chuyển bộ lọc sang "30 ngày" hoặc "90 ngày"
2. Kiểm tra database có hóa đơn không
3. Thêm hóa đơn mẫu

---

### Lỗi 4: Lỗi compile

**Nguyên nhân**: Thiếu thư viện hoặc JavaFX

**Giải pháp**:
1. Kiểm tra folder `lib/` có đầy đủ JAR files
2. Kiểm tra JavaFX đã cài đặt
3. Clean và Rebuild project
4. Restart IDE

---

### Lỗi 5: Lỗi kết nối database

**Nguyên nhân**: SQL Server không chạy hoặc sai thông tin kết nối

**Giải pháp**:
1. Kiểm tra SQL Server đang chạy
2. Kiểm tra file `ConnectDB.java`:
   ```java
   // Kiểm tra URL, username, password
   private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QLThuoc";
   private static final String USER = "sa";
   private static final String PASSWORD = "your_password";
   ```
3. Test connection từ SSMS

---

## Checklist Hoàn Thành

Đánh dấu ✅ khi hoàn thành:

**Chuẩn bị**:
- [ ] Database có dữ liệu đầy đủ
- [ ] SQL Server đang chạy
- [ ] Project đã compile không lỗi

**Đăng nhập**:
- [ ] Tìm được tài khoản QUẢN_LÝ hoặc DƯỢC_SĨ
- [ ] Đăng nhập thành công

**Dashboard**:
- [ ] Dashboard hiển thị
- [ ] 4 KPI cards có dữ liệu
- [ ] 3 biểu đồ hiển thị đúng
- [ ] Cảnh báo hiển thị (hoặc "Không có cảnh báo")
- [ ] Bộ lọc thời gian hoạt động
- [ ] Nút làm mới hoạt động

**Test nâng cao**:
- [ ] Tự động làm mới sau 30s
- [ ] Thử các kịch bản thực tế
- [ ] So sánh dữ liệu với database

---

## Liên Hệ và Hỗ Trợ

### Nếu gặp vấn đề, tham khảo:

1. **DASHBOARD_README.md** - Chi tiết kỹ thuật
2. **DASHBOARD_SUMMARY.md** - Tóm tắt triển khai  
3. **DASHBOARD_QUICK_GUIDE.md** - Hướng dẫn người dùng
4. **DASHBOARD_COMPLETE.md** - Báo cáo hoàn thành

### Debug tips:

```java
// Thêm vào Dashboard.java để debug
System.out.println("Loading data...");
System.out.println("Số thuốc: " + thuocDAO.getAllTbThuoc().size());
System.out.println("Số hóa đơn: " + hoaDonDAO.getAllHoaDon().size());
```

---

## Kết Luận

✅ Dashboard đã sẵn sàng sử dụng!

**Thời gian test dự kiến**: 15-20 phút

**Kết quả**: Dashboard hoạt động đầy đủ chức năng, hiển thị dữ liệu chính xác

---

**Chúc bạn test thành công!** 🎉

*Nhóm 7 - 19/12/2025*

