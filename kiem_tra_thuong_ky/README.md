# BÀI KIỂM TRA THƯỜNG KỲ: HỆ THỐNG CHAT NHÓM ĐA PHÒNG QUA TCP

- **Trường:** Đại học Công nghiệp TP.HCM (IUH)
- **Học phần:** Phát triển hệ thống tích hợp
- **Lớp học phần:** 21BVL
- **Sinh viên thực hiện:** Phạm Ngọc Lang
- **MSSV:** 25765251
- **GitHub Repository:** [https://github.com/langpn/pthtth_21bvl_25765251](https://github.com/langpn/pthtth_21bvl_25765251)

---

## 📖 1. YÊU CẦU ĐỀ BÀI
Xây dựng chương trình Chat nhóm qua TCP:
1. Mỗi client nhập Tên và kết nối tới server.
2. Chọn 1 phòng trong danh sách phòng do server cung cấp (vd: phòng `Study`, phòng `Game`, phòng `TraiNghiem`...) và chỉ nhận tin nhắn từ những client khác trong phòng đó. Sinh viên có thể tự đề xuất tên phòng.
3. Nhập `/quit` để rời phòng và quay lại danh sách các phòng.
4. Nhập `/exit` để ngắt kết nối tới server.

---

## 🏗️ 2. CẤU TRÚC MÃ NGUỒN (JAVA 17 LTS THUẦN)
```
kiem_tra_thuong_ky/
├── src/
│   └── chat/
│       ├── Room.java             # Quản lý danh sách thành viên phòng (ConcurrentHashMap), broadcast cô lập
│       ├── ClientHandler.java    # Luồng xử lý từng client (xử lý menu, chuyển phòng, /quit, /exit, cleanup)
│       ├── ChatRoomServer.java   # Server TCP Master-Worker (FixedThreadPool: 50, lắng nghe port 5006)
│       └── ChatRoomClient.java   # Client console 2 luồng song song (đọc socket + đọc bàn phím)
├── out/                          # Bytecode sau khi biên dịch
├── scripts/
│   ├── compile.sh                # Biên dịch mã nguồn UTF-8 bằng javac
│   ├── run_server.sh             # Chạy ChatRoomServer
│   ├── run_client.sh             # Chạy ChatRoomClient
│   ├── test_demo.sh              # Script tự động kiểm thử 5 ca thực nghiệm
│   └── gen_docx.py               # Script sinh file báo cáo Word
├── screenshots/                  # Ảnh chụp màn hình full desktop có ngày giờ làm bài
├── 25765251_PhamNgocLang_KiemTraThuongKy_BaoCao.docx # Báo cáo nộp LMS
└── 25765251_PhamNgocLang_KiemTraThuongKy.zip        # Mã nguồn nén nộp LMS
```

---

## ⚡ 3. HƯỚNG DẪN THỰC THI GÕ TAY BẰNG TERMINAL CLI (KHÔNG DÙNG SCRIPT)
*(Dành cho buổi thi thực hành và demo trực tiếp trước giảng viên)*

### Bước 0: Mở Terminal và chuyển vào thư mục dự án
```bash
cd /Users/langpn/workspace/school/phat-trien-he-thong-tich-hop/pthtth_21bvl_25765251/kiem_tra_thuong_ky
```

### Bước 1: Biên dịch bằng `javac` thuần
```bash
mkdir -p out && javac -encoding UTF-8 -d out src/chat/*.java
```

### Bước 2: Khởi động Server (Cửa sổ Terminal 1)
```bash
java -cp out chat.ChatRoomServer
```
*(Server sẽ mở cổng 5006 và hiển thị 3 phòng sẵn có: Study, Game, TraiNghiem)*

### Bước 3: Mở 3 Client song song để demo (Cửa sổ Terminal 2, 3, 4)

#### 🔹 Cửa sổ Terminal 2 (Client An vào phòng Study):
```bash
cd /Users/langpn/workspace/school/phat-trien-he-thong-tich-hop/pthtth_21bvl_25765251/kiem_tra_thuong_ky
java -cp out chat.ChatRoomClient
```
1. Ngay khi kết nối, màn hình hiện: `Vui lòng nhập Tên của bạn:` ➔ Gõ `An`
2. Menu danh sách phòng hiện ra ➔ Gõ `1` (hoặc `Study`)
3. Gõ tin nhắn: `Chao ban Binh trong phong Study!`

#### 🔹 Cửa sổ Terminal 3 (Client Binh vào phòng Study):
```bash
cd /Users/langpn/workspace/school/phat-trien-he-thong-tich-hop/pthtth_21bvl_25765251/kiem_tra_thuong_ky
java -cp out chat.ChatRoomClient
```
1. Nhập tên: `Binh`
2. Chọn phòng: `1`
3. Lúc này An và Binh thấy tin nhắn của nhau theo thời gian thực.

#### 🔹 Cửa sổ Terminal 4 (Client Cuong vào phòng Game - Test cách ly phòng):
```bash
cd /Users/langpn/workspace/school/phat-trien-he-thong-tich-hop/pthtth_21bvl_25765251/kiem_tra_thuong_ky
java -cp out chat.ChatRoomClient
```
1. Nhập tên: `Cuong`
2. Chọn phòng: `2` (Game)
3. Gõ: `Ai choi Game voi Cuong khong?` 
   ➔ **An và Binh ở phòng Study hoàn toàn không thấy tin nhắn này!**

### Bước 4: Thao tác lệnh điều hướng
1. **Lệnh `/quit` (Rời phòng):**
   - Tại Terminal của `An`, gõ `/quit`.
   - `Binh` nhận được thông báo: `[Study - HỆ THỐNG]: An đã rời phòng!`.
   - Màn hình của `An` hiện lại Menu phòng ➔ Gõ `2` để sang phòng `Game` chat với `Cuong`.
2. **Lệnh `/exit` (Ngắt kết nối):**
   - Tại bất kỳ client nào, gõ `/exit` để thoát ứng dụng an toàn.

---

## 🛠️ 4. CHẠY NHANH BẰNG SCRIPT CÓ SẴN (NẾU CẦN)
- Biên dịch: `./scripts/compile.sh`
- Khởi động Server: `./scripts/run_server.sh`
- Khởi động Client: `./scripts/run_client.sh`
- Kiểm thử tự động 5 ca: `./scripts/test_demo.sh`
