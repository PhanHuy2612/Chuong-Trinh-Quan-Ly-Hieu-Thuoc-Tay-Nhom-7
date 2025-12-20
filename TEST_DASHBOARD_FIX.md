# ✅ HƯỚNG DẪN TEST FIX DASHBOARD

## 📋 TÓMLƯỢC FIX

Các vấn đề đã sửa:
1. ✅ Dashboard không hiển thị dữ liệu → Thêm Platform.runLater()
2. ✅ Chuyển menu khác rồi quay lại không hoạt động → Quản lý scheduler tốt hơn
3. ✅ KPI Card không update → Fix cấu trúc lưu labels

---

## 🧪 TEST STEP-BY-STEP

### TEST 1: Dashboard Hiển Thị Dữ Liệu

**Bước 1: Rebuild Project**
```
File → Project Structure → Modules
(Hoặc Build → Rebuild Project)
```

**Bước 2: Chạy Ứng Dụng**
```
Right-click frmDangNhap.java → Run 'frmDangNhap.main()'
```

**Bước 3: Đăng Nhập**
- Username: Tài khoản QUẢN_LÝ hoặc DƯỢC_SĨ
- Password: Mật khẩu của tài khoản

**Bước 4: Kiểm tra Dashboard**
```
✅ Màn hình chính là Dashboard
✅ Có 4 KPI Cards (Tồn kho, Cảnh báo, Doanh thu, Hóa đơn)
✅ Có 3 Biểu đồ (Nguồn DT, Giờ cao điểm, Loại bán)
✅ Có Alert box dưới cùng
✅ Có các giá trị số (không phải 0 hoặc trống)
```

**Kết quả mong đợi:**
```
TỶ LỆ CÒN HÀNG: 85.5% hoặc số% nào đó (KHÔNG PHẢI 0%)
DOANH THU: 15.1M VNĐ hoặc số tiền nào đó
SỐ HÓA ĐƠN: 72 hoặc số nào đó (KHÔNG PHẢI 0)
```

---

### TEST 2: Chuyển Menu Rồi Quay Lại

**Bước 1: Từ Dashboard**
```
Kích vào Dashboard menu trước
→ Xem dữ liệu hiển thị đầy đủ
```

**Bước 2: Click Menu Khác**
```
Kích vào "Quản lý khách hàng" hoặc "Quản lý bán thuốc"
→ Màn hình chuyển sang chức năng đó
→ Console sẽ có message: "currentDashboard.stop()"
```

**Bước 3: Click Dashboard Lại**
```
Kích vào "Dashboard" menu
→ Dashboard hiển thị lại
→ Dữ liệu vẫn đầy đủ (không trống)
→ Console sẽ có [DEBUG] messages
```

**Kết quả mong đợi:**
```
✅ Dashboard hiển thị mỗi lần click
✅ Dữ liệu KHÔNG TRỐNG
✅ Tất cả 4 KPI Cards có giá trị
✅ Biểu đồ có data
```

---

### TEST 3: Kiểm tra Console Logs

**Bước 1: Mở Console**
- Nếu dùng IntelliJ: View → Tool Windows → Run
- Hoặc nhấn Alt+4

**Bước 2: Tìm Debug Messages**
```
Tìm những dòng sau:
[DEBUG] loadInventoryStatus: total=..., conHang=..., sapHet=...
[DEBUG] loadRevenueAndInvoices: soHD=..., soKH=..., doanhThu=...
```

**Ý nghĩa:**
- Nếu thấy những dòng này = Database connection OK ✅
- Nếu không thấy = Có lỗi trong load data

---

### TEST 4: Bộ Lọc Thời Gian

**Bước 1: Click Dropdown "Xem theo"**
```
Mặc định: "Hôm nay"
```

**Bước 2: Thay Đổi**
```
Kích "7 ngày"
→ Dữ liệu update (số phải tăng)
```

**Kích "30 ngày"**
```
→ Dữ liệu update lớn hơn
```

**Kích "90 ngày"**
```
→ Dữ liệu update lớn nhất
```

**Kết quả mong đợi:**
```
✅ KPI Cards update mỗi lần thay bộ lọc
✅ Biểu đồ update
✅ Dữ liệu phải tăng theo khoảng thời gian dài hơn
```

---

## 🔍 NẾU DASHBOARD VẪN TRỐNG

### Kiểm tra 1: Database Có Dữ Liệu?

Mở SQL Server Management Studio:
```sql
USE QLThuoc;
SELECT COUNT(*) as SoThuoc FROM Thuoc;
SELECT COUNT(*) as SoHoaDon FROM HoaDon;
SELECT COUNT(*) as SoChiTiet FROM ChiTietHoaDon;
```

**Phải có:**
- SoThuoc > 50
- SoHoaDon > 20
- SoChiTiet > 50

Nếu = 0 → Thêm dữ liệu test trước

---

### Kiểm tra 2: Console Có Error Không?

```
Tìm trong Console:
[ERROR]
Exception

Nếu có → Báo lỗi chi tiết gì?
```

---

### Kiểm tra 3: Connection Hoạt Động?

Mở ConnectDB.java:
```java
// Kiểm tra:
private static final String URL = "jdbc:sqlserver://...";
private static final String USER = "sa";
private static final String PASSWORD = "...";
```

Phải match với SQL Server connection của bạn

---

### Kiểm tra 4: DAO Hoạt Động?

Debug tại `loadInventoryStatus()`:
```java
List<Thuoc> dsThuoc = thuocDAO.getAllTbThuoc();
System.out.println("Size: " + dsThuoc.size()); // Phải > 0
```

---

## 📊 CHIA SẺ CONSOLE LOG

Nếu vẫn có vấn đề, copy toàn bộ console log:
```
1. Chạy lại
2. Xem Console tab
3. Select All (Ctrl+A)
4. Copy (Ctrl+C)
5. Gửi cho tôi
```

Từ log tôi sẽ biết ngay vấn đề ở đâu

---

## ✅ FINAL CHECKLIST

Trước khi báo fix xong, kiểm tra:

- [ ] Dashboard hiển thị khi đăng nhập
- [ ] 4 KPI Cards có dữ liệu (không phải 0)
- [ ] 3 Biểu đồ có dữ liệu
- [ ] Alert box hiển thị
- [ ] Bộ lọc thời gian hoạt động
- [ ] Chuyển menu khác được
- [ ] Click Dashboard lại dữ liệu vẫn có
- [ ] Console không có error (chỉ warning OK)
- [ ] Nút "🔄 Làm mới" hoạt động

Nếu tất cả ✅ = **FIX THÀNH CÔNG!**

---

## 🎯 EXPECTED BEHAVIOR

### Lần Đầu Tiên Mở Dashboard
```
1. Hiển thị các thành phần (KPI, Charts, Alerts)
2. Tất cả ban đầu là 0 hoặc placeholder
3. Sau 1-2 giây, data xuất hiện
4. Console in: [DEBUG] messages
5. Dashboard đầy đủ dữ liệu
```

### Mỗi Lần Click Dashboard Menu
```
1. Dashboard mới được tạo
2. Scheduler cũ stop
3. Scheduler mới start
4. Data load lại
5. Console in: [DEBUG] messages
```

### Khi Filter Thay Đổi
```
1. loadData() được gọi
2. Data query lại
3. UI update
4. Console in: [DEBUG] messages
```

---

## 🚀 NEXT STEPS

1. **Rebuild** Project
2. **Run** Application
3. **Test** theo hướng dẫn trên
4. **Check** Console Log
5. **Report** kết quả cho tôi

---

**Status**: ✅ FIX COMPLETE - READY FOR TEST
**Date**: 20/12/2025

