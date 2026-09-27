# Lab 02: Lập trình hướng đối tượng trong Java (OOP trong Java)

## Cấu trúc thư mục

```text
lab_02/
├── README.md
├── src/
│   ├── bai01/
│   │   ├── SanPham.java     # Lớp đối tượng SanPham (Tính đóng gói)
│   │   └── Main.java        # Chương trình kiểm thử nghiệp vụ sản phẩm
│   └── bai02/
│       ├── Nguoi.java        # Lớp cha Nguoi (Tính kế thừa)
│       ├── SinhVien.java     # Lớp con SinhVien kế thừa Nguoi
│       ├── GiangVien.java    # Lớp con GiangVien kế thừa Nguoi
│       └── Main.java         # Chương trình kiểm thử quản lý người
└── out/                      # Thư mục chứa các tệp bytecode sau khi biên dịch (.class)
```

---

## Bài 1: Quản lý sản phẩm – Tính đóng gói (Encapsulation)

### Mục tiêu & Thiết kế
- Che giấu trạng thái đối tượng với phạm vi truy cập `private` (`maSanPham`, `tenSanPham`, `donGia`, `soLuong`).
- Cung cấp constructor đầy đủ tham số kèm kiểm tra tính hợp lệ dữ liệu.
- Phương thức nghiệp vụ:
  - `double tinhThanhTien()`: Tính theo `donGia * soLuong`.
  - `void nhapHang(int soLuongNhap)`: Chỉ cho phép nhập số lượng > 0 và cập nhật tồn kho.
  - `boolean banHang(int soLuongBan)`: Kiểm tra số lượng bán > 0 và không vượt quá số lượng tồn kho. Trả về `true` nếu thành công, `false` nếu không đủ hàng.
  - `void hienThiThongTin()`: Xuất định dạng đẹp mắt mã, tên, đơn giá, số lượng tồn kho và thành tiền.

### Biên dịch & Chạy thử
```bash
# Đứng tại thư mục lab_02
javac -encoding UTF-8 -d out src/bai01/*.java
java -cp out bai01.Main
```

---

## Bài 2: Quản lý người trong trường đại học – Tính kế thừa (Inheritance)

### Mục tiêu & Thiết kế
- Xây dựng lớp cha `Nguoi`:
  - Thuộc tính: `hoTen`, `namSinh`, `diaChi`.
  - Phương thức: `int tinhTuoi()` (dựa theo năm hiện tại), `void hienThiThongTin()`.
- Xây dựng lớp con `SinhVien extends Nguoi`:
  - Thuộc tính bổ sung: `maSinhVien`, `nganhHoc`, `diemTrungBinh`.
  - Gọi constructor lớp cha bằng `super(hoTen, namSinh, diaChi)`.
  - Ghi đè `@Override public void hienThiThongTin()` kết hợp `super.hienThiThongTin()`.
  - `public String xepLoai()`: Xếp loại học lực theo các mốc điểm (Giỏi, Khá, Trung bình, Yếu).
- Xây dựng lớp con `GiangVien extends Nguoi`:
  - Thuộc tính bổ sung: `maGiangVien`, `chuyenMon`, `luongCoBan`, `heSoLuong`.
  - Gọi constructor lớp cha bằng `super(...)`.
  - `public double tinhLuong()`: Tính theo công thức `luongCoBan * heSoLuong`.
  - Ghi đè `@Override public void hienThiThongTin()` hiển thị đầy đủ thông tin giảng viên và lương.
- Chương trình chính `Main.java`:
  - Khởi tạo ít nhất 2 sinh viên và 2 giảng viên.
  - Sử dụng danh sách đa hình `List<Nguoi>` để duyệt và in thông tin từng đối tượng.
  - Hiển thị bảng xếp loại sinh viên.
  - Hiển thị bảng lương giảng viên.

### Biên dịch & Chạy thử
```bash
# Đứng tại thư mục lab_02
javac -encoding UTF-8 -d out src/bai02/*.java
java -cp out bai02.Main
```
