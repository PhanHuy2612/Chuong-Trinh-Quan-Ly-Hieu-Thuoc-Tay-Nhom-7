# DASHBOARD - TỔNG QUAN HOẠT ĐỘNG NHÀ THUỐC

## 📊 Tổng quan

Dashboard được thiết kế dựa trên mô hình quản lý khách sạn nhưng được tùy chỉnh cho hệ thống quản lý nhà thuốc, hiển thị các chỉ số cốt lõi giúp người quản lý nắm bắt tình hình kinh doanh nhanh chóng và ra quyết định kịp thời.

## 🎯 Các Chỉ Số Chính (KPI Cards)

### 1. **Tỷ Lệ Còn Hàng** 📦
- **Mục đích**: Giám sát tình trạng tồn kho tổng thể
- **Hiển thị**: 
  - Phần trăm thuốc còn hàng / tổng số thuốc
  - Số lượng thuốc còn hàng
- **Ứng dụng**: Đánh giá khả năng đáp ứng nhu cầu khách hàng, tránh tình trạng thiếu hàng

### 2. **Cảnh Báo Tồn Kho** ⚠️
- **Mục đích**: Cảnh báo sớm về thuốc sắp hết / hết hàng
- **Hiển thị**:
  - Phần trăm thuốc cần nhập khẩu gấp
  - Số lượng thuốc trong tình trạng nguy hiểm
- **Ứng dụng**: Lập kế hoạch nhập hàng kịp thời, tránh gián đoạn kinh doanh

### 3. **Doanh Thu** 💰
- **Mục đích**: Theo dõi hiệu quả kinh doanh
- **Hiển thị**:
  - Tổng doanh thu theo khoảng thời gian đã chọn
  - So sánh với cùng kỳ (%)
- **Ứng dụng**: Đánh giá xu hướng tăng trưởng, điều chỉnh chiến lược kinh doanh

### 4. **Số Hóa Đơn** 🧾
- **Mục đích**: Theo dõi hoạt động bán hàng
- **Hiển thị**:
  - Tổng số hóa đơn
  - Số lượng khách hàng
- **Ứng dụng**: Đánh giá mức độ nhộn nhịp của nhà thuốc

## 📈 Các Biểu Đồ Phân Tích

### 5. **Nguồn Doanh Thu** (Biểu đồ tròn)
- **Mục đích**: Phân tích cơ cấu doanh thu theo loại thuốc
- **Nội dung**:
  - Doanh thu từ từng nhóm thuốc (Kháng sinh, Giảm đau, Vitamin, v.v.)
  - Phần trăm đóng góp của mỗi loại
- **Ứng dụng**: 
  - Xác định nhóm thuốc sinh lời cao nhất
  - Tập trung phát triển các sản phẩm có lợi nhuận tốt
  - Điều chỉnh tồn kho phù hợp với nhu cầu

### 6. **Khung Giờ Cao Điểm** (Biểu đồ đường)
- **Mục đích**: Phân tích lưu lượng khách hàng theo giờ
- **Nội dung**:
  - Số lượng hóa đơn theo từng giờ trong ngày
  - Đường xu hướng cao điểm
- **Ứng dụng**:
  - Phân bổ nhân sự hợp lý (tăng cường vào giờ cao điểm)
  - Sắp xếp ca làm việc hiệu quả
  - Dự trú hàng hóa cho giờ đông khách

### 7. **Tỷ Lệ Bán Theo Loại Thuốc** (Biểu đồ tròn)
- **Mục đích**: Phân tích cơ cấu bán hàng theo phân loại (OTC, ETC)
- **Nội dung**:
  - Số lượng bán theo từng loại thuốc
  - Tỷ lệ phần trăm
- **Ứng dụng**:
  - Đánh giá nhu cầu thị trường
  - Điều chỉnh chiến lược marketing
  - Đào tạo nhân viên tư vấn phù hợp

## ⚠️ Hệ Thống Cảnh Báo

Dashboard tích hợp hệ thống cảnh báo thời gian thực với 3 mức độ:

### 🔴 Cảnh báo HẾT HÀNG
- Thuốc đã hết trong kho
- **Hành động**: Nhập hàng khẩn cấp hoặc tìm thuốc thay thế

### 🟡 Cảnh báo SẮP HẾT
- Thuốc có số lượng dưới ngưỡng tối thiểu
- **Hành động**: Đặt hàng sớm để tránh gián đoạn

### ⏰ Cảnh báo SẮP HẾT HẠN
- Thuốc có hạn sử dụng < 1 tháng
- **Hành động**: Khuyến mãi xả hàng hoặc trả lại nhà cung cấp

## 🔄 Bộ Lọc Thời Gian

Dashboard hỗ trợ 4 chế độ xem:
- **Hôm nay**: Theo dõi hoạt động trong ngày
- **7 ngày**: Xu hướng tuần
- **30 ngày**: Xu hướng tháng
- **90 ngày**: Xu hướng quý

## ⚙️ Tính Năng Kỹ Thuật

### Tự Động Làm Mới
- Dashboard tự động cập nhật dữ liệu mỗi 30 giây
- Đảm bảo thông tin luôn chính xác và mới nhất

### Tối Ưu Hiệu Suất
- Truy vấn dữ liệu được tối ưu hóa
- Sử dụng thread pool để tránh blocking UI
- Cache dữ liệu thông minh

### Responsive Design
- Giao diện tự động điều chỉnh theo kích thước màn hình
- Hỗ trợ đa độ phân giải

## 🎨 Thiết Kế UI/UX

### Màu Sắc
- **Xanh dương (#3B82F6)**: Doanh thu - tích cực
- **Xanh lá (#10B981)**: Còn hàng - an toàn
- **Đỏ (#EF4444)**: Cảnh báo - cần hành động
- **Tím (#8B5CF6)**: Hóa đơn - trung tính

### Layout
- Grid 4 cột cho KPI cards
- Grid 3 cột cho biểu đồ
- Cảnh báo full-width để dễ nhìn thấy

## 📖 Hướng Dẫn Sử Dụng

### Truy cập Dashboard
1. Đăng nhập với tài khoản **QUẢN LÝ**
2. Dashboard sẽ tự động hiển thị là màn hình mặc định
3. Hoặc click vào menu **"Dashboard"** trên sidebar

### Đọc Hiểu Dữ Liệu
1. **Nhìn trước KPI Cards**: Nắm bắt tổng quan trong 5 giây
2. **Kiểm tra Cảnh báo**: Xử lý vấn đề khẩn cấp
3. **Phân tích Biểu đồ**: Hiểu sâu xu hướng và cơ cấu
4. **Điều chỉnh Bộ lọc**: Xem dữ liệu theo khoảng thời gian phù hợp

### Ra Quyết Định
- **Tồn kho cao**: Xem xét giảm nhập hàng, khuyến mãi
- **Cảnh báo nhiều**: Lập kế hoạch nhập khẩu
- **Doanh thu giảm**: Điều tra nguyên nhân, tăng cường marketing
- **Giờ cao điểm rõ ràng**: Tối ưu lịch làm việc nhân viên

## 🔧 Cấu Hình

File: `Dashboard.java`

### Thay đổi tần suất làm mới
```java
scheduler.scheduleAtFixedRate(() -> {
    Platform.runLater(this::loadData);
}, 30, 30, TimeUnit.SECONDS); // Đổi 30 thành số giây mong muốn
```

### Thay đổi số lượng cảnh báo hiển thị
```java
.limit(5) // Đổi 5 thành số lượng mong muốn
```

## 📊 Ví Dụ Kịch Bản Sử Dụng

### Kịch bản 1: Sáng thứ 2 đầu tuần
Quản lý mở Dashboard, chọn bộ lọc "7 ngày" để xem:
- Doanh thu tuần trước
- Các thuốc cần nhập
- Giờ cao điểm để sắp xếp nhân viên tuần này

### Kịch bản 2: Cuối ngày
Quản lý xem Dashboard với bộ lọc "Hôm nay":
- So sánh doanh thu với mục tiêu
- Kiểm tra số hóa đơn và khách hàng
- Ghi nhận giờ nào đông khách nhất

### Kịch bản 3: Họp cuối tháng
Quản lý sử dụng bộ lọc "30 ngày":
- Báo cáo doanh thu tháng
- Phân tích nhóm thuốc bán chạy
- Lập kế hoạch nhập hàng tháng sau

## 🚀 Tính Năng Tương Lai

- [ ] Xuất báo cáo PDF
- [ ] So sánh với cùng kỳ năm trước
- [ ] Dự đoán xu hướng bằng AI
- [ ] Cảnh báo qua email/SMS
- [ ] Dashboard cho từng danh mục thuốc
- [ ] Tích hợp dữ liệu thời tiết (ảnh hưởng đến bán hàng)

## 📝 Ghi Chú Kỹ Thuật

- **Database**: SQL Server
- **Framework**: JavaFX
- **Charts**: JavaFX Chart API
- **Threading**: ScheduledExecutorService
- **Design Pattern**: MVC

---

**Phát triển bởi**: Nhóm 7
**Ngày tạo**: 19/12/2025
**Version**: 1.0.0

