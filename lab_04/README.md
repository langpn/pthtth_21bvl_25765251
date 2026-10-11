# HƯỚNG DẪN THỰC THI & KIỂM THỬ MÃ NGUỒN LAB 04
## LẬP TRÌNH SOCKET TCP VÀ UDP TRONG JAVA 17 LTS

> **Môn học:** Phát triển hệ thống tích hợp (PTH TTH)  
> **Lớp:** 21BVL - Khoa Công nghệ Thông tin - Trường Đại học Công nghiệp TP.HCM (IUH)  
> **Sinh viên:** Phạm Ngọc Lang - MSSV: 25765251  
> **Môi trường:** Java 17 LTS thuần (OpenJDK Temurin LTS), không dùng công cụ build bên ngoài (không dùng Maven/Gradle).

---

## ⚠️ QUY TẮC BẮT BUỘC KHI THỰC THI (RULES)

1. **100% Lệnh Java thuần**: Mọi thao tác biên dịch (`javac`) và chạy (`java`) bắt buộc chỉ dùng công cụ Java chuẩn và đường ống shell cơ bản (`printf "..." | java ...`, chạy nền `&`). Tuyệt đối không dùng script phụ trợ như Python, Node.js để bọc hay chạy code Java.
2. **Thư mục làm việc (Working Directory)**: Mọi câu lệnh bên dưới đều đứng từ thư mục gốc bài tập:
   ```bash
   cd /Users/langpn/workspace/school/iuh-documents/phat-trien-he-thong-tich-hop/pthtth_21bvl_25765251
   ```
3. **Thư mục bytecode output**: Toàn bộ file `.class` được biên dịch vào `lab_04/out`.

---

## I. LỆNH BIÊN DỊCH TOÀN BỘ MÃ NGUỒN (COMPILE ALL)

Mở terminal trong VS Code và chạy lệnh sau để biên dịch toàn bộ tất cả các bài:

```bash
mkdir -p lab_04/out
javac -encoding UTF-8 -d lab_04/out $(find lab_04/src -name "*.java")
```

---

## II. HƯỚNG DẪN CHẠY VÀ KIỂM THỬ CHI TIẾT TỪ BÀI 1 ĐẾN BÀI 10

---

### BÀI 1: KHẢO SÁT HOST VÀ URI INSPECTOR (`HostAndUriInspector`)
* **Lớp chính:** `network.HostAndUriInspector`
* **Mục tiêu:** Phân giải địa chỉ IP (IPv4, IPv6) của hostname và bóc tách các thành phần của chuỗi URI (scheme, host, port, path, query, fragment).

#### 1. Kiểm thử các ca hợp lệ:
```bash
# Ca 1: Localhost và URI có port, query, fragment
java -cp lab_04/out network.HostAndUriInspector localhost "http://localhost:8080/api/users?page=1#profile"

# Ca 2: Domain công cộng HTTPS cổng mặc định
java -cp lab_04/out network.HostAndUriInspector example.com "https://example.com/docs/guide?lang=vi#chapter1"

# Ca 3: Giao thức FTP cổng 21
java -cp lab_04/out network.HostAndUriInspector iuh.edu.vn "ftp://files.iuh.edu.vn:21/public/tailieu.pdf"
```

#### 2. Kiểm thử các ca lỗi & ngoại lệ:
```bash
# Ca lỗi 1: Không truyền đủ tham số
java -cp lab_04/out network.HostAndUriInspector

# Ca lỗi 2: Host không tồn tại (Bắt UnknownHostException)
java -cp lab_04/out network.HostAndUriInspector host-khong-ton-tai.invalid "http://test.com"

# Ca lỗi 3: URI sai cú pháp chứa khoảng trắng (Bắt URISyntaxException)
java -cp lab_04/out network.HostAndUriInspector localhost "http://invalid uri with spaces:8080/test"
```

---

### BÀI 2: ĐỔI CHỮ SỐ THÀNH CHỮ TỪ XA QUA TCP (`DigitToWord`)
* **Cổng dịch vụ:** `5002`
* **Lớp Server:** `tcp.DigitToWordServer`
* **Lớp Client:** `tcp.DigitToWordClient`

#### Cách 1: Chạy Server ngầm và test tự động bằng pipe shell (Một terminal)
```bash
# 1. Khởi động Server chạy nền
java -cp lab_04/out tcp.DigitToWordServer 5002 &
SERVER_PID=$!

# 2. Chạy Client kiểm thử đầy đủ các ca biên:
#    Gửi '0' -> OK KHÔNG, '9' -> OK CHÍN, '' -> ERR, '10' -> ERR, 'a' -> ERR, ' 5 ' -> ERR, 'QUIT' -> Thoát
(printf "0\n"; sleep 0.3; printf "9\n"; sleep 0.3; printf "\n"; sleep 0.3; printf "10\n"; sleep 0.3; printf "a\n"; sleep 0.3; printf " 5 \n"; sleep 0.3; printf "QUIT\n") | java -cp lab_04/out tcp.DigitToWordClient localhost 5002

# 3. Dọn dẹp tiến trình Server sau khi test xong
kill $SERVER_PID
```

#### Cách 2: Chạy tương tác qua 2 Terminal
* **Terminal 1 (Server):**
  ```bash
  java -cp lab_04/out tcp.DigitToWordServer 5002
  ```
* **Terminal 2 (Client):**
  ```bash
  java -cp lab_04/out tcp.DigitToWordClient localhost 5002
  ```
  Nhập lần lượt: `0`, `9`, chuỗi rỗng (nhấn Enter), `10`, `a`, `QUIT`.

---

### BÀI 3: DỊCH VỤ NGÀY GIỜ TCP VÀ UDP (`DateTimeService`)
* **Cổng TCP:** `5003` | **Cổng UDP:** `5004`
* **Lớp TCP:** `datetime.TcpDateTimeServer` & `datetime.TcpDateTimeClient`
* **Lớp UDP:** `datetime.UdpDateTimeServer` & `datetime.UdpDateTimeClient`

#### 1. Kiểm thử Dịch vụ TCP:
```bash
# Chạy TCP Server nền
java -cp lab_04/out datetime.TcpDateTimeServer 5003 &
TCP_PID=$!

# Test lệnh DATE, TIME, DATETIME và QUIT
(printf "DATE\n"; sleep 0.3; printf "TIME\n"; sleep 0.3; printf "DATETIME\n"; sleep 0.3; printf "QUIT\n") | java -cp lab_04/out datetime.TcpDateTimeClient localhost 5003

kill $TCP_PID
```

#### 2. Kiểm thử Dịch vụ UDP & So sánh Server dừng:
```bash
# Chạy UDP Server nền
java -cp lab_04/out datetime.UdpDateTimeServer 5004 &
UDP_PID=$!

# Test UDP Client gửi DATE, TIME, DATETIME
(printf "DATE\n"; sleep 0.3; printf "TIME\n"; sleep 0.3; printf "DATETIME\n"; sleep 0.3; printf "QUIT\n") | java -cp lab_04/out datetime.UdpDateTimeClient localhost 5004

# Dừng UDP Server để kiểm thử cơ chế timeout
kill $UDP_PID

# Chạy lại UDP Client khi Server đã dừng -> Sau 3 giây sẽ ném SocketTimeoutException an toàn:
(printf "DATE\n"; sleep 4; printf "QUIT\n") | java -cp lab_04/out datetime.UdpDateTimeClient localhost 5004
```

---

### BÀI 4: MÁY TÍNH TỪ XA TCP (`Remote Calculator`)
* **Cổng dịch vụ:** `5005`
* **Lớp Server:** `calc.CalcServer`
* **Lớp Client:** `calc.CalcClient`
* **Giao thức:** `CALC <toán_tử> <a_hạng> <b_hạng>`

```bash
# Khởi động CalcServer nền
java -cp lab_04/out calc.CalcServer 5005 &
CALC_PID=$!

# Test 8 ca kiểm thử: cộng, trừ, nhân, chia, chia cho 0, toán tử lạ, số lỗi, format lỗi
(printf "CALC + 100 200\n"; sleep 0.3; \
 printf "CALC - 50 15\n"; sleep 0.3; \
 printf "CALC * 12 8\n"; sleep 0.3; \
 printf "CALC / 100 4\n"; sleep 0.3; \
 printf "CALC / 10 0\n"; sleep 0.3; \
 printf "CALC %% 10 3\n"; sleep 0.3; \
 printf "CALC + a 2\n"; sleep 0.3; \
 printf "CALC 10 20\n"; sleep 0.3; \
 printf "QUIT\n") | java -cp lab_04/out calc.CalcClient localhost 5005

kill $CALC_PID
```

---

### BÀI 5: PHÒNG CHAT TCP NHIỀU CLIENT ĐA LUỒNG (`ChatServer`)
* **Cổng dịch vụ:** `5006` (hoặc `5056` nếu tránh xung đột cổng)
* **Lớp Server:** `chat.ChatServer`
* **Lớp Client:** `chat.ChatClient`
* **Tính năng:** Quản lý ThreadPool, broadcast tin nhắn, đồng bộ `ConcurrentHashMap`, kiểm tra trùng biệt danh.

#### Hướng dẫn chạy và chụp ảnh thực tế cho Bài 5:
* **Terminal 1 (Chạy Server):**
  ```bash
  java -cp lab_04/out chat.ChatServer 5056
  ```
* **Terminal 2 (Client 1 - Alice):**
  ```bash
  java -cp lab_04/out chat.ChatClient localhost 5056
  # Gõ:
  NICK Alice
  MSG Xin chao ca phong chat!
  USERS
  ```
* **Terminal 3 (Client 2 - Bob):**
  ```bash
  java -cp lab_04/out chat.ChatClient localhost 5056
  # Gõ:
  NICK Bob
  MSG Chao Alice nhe!
  USERS
  QUIT
  ```

*(Chụp ảnh toàn màn hình VS Code chia đôi/ba terminal chạy trên để lưu vào `screenshots/bai_tap_5_chat_multiclient.jpg`)*.

---

### BÀI 6: THỰC NGHIỆM ĐO LƯỜNG SO SÁNH TCP VÀ UDP (`TcpUdpBenchmark`)
* **Lớp chính:** `benchmark.TcpUdpBenchmark`
* **Mục tiêu:** Gửi 1000 gói tin x 256 bytes qua TCP và UDP trên localhost, lặp lại 5 vòng đo, tính độ trễ trung bình và thông lượng.

```bash
# Chạy chương trình benchmark đo lường:
java -cp lab_04/out benchmark.TcpUdpBenchmark
```
*Kết quả xuất ra bảng số liệu thực nghiệm khoa học so sánh chi tiết giữa TCP và UDP.*

---

### BÀI 7: SERVER LƯU NHẬT KÝ TIN NHẮN THEO CLIENT (`MessageLogger`)
* **Cổng dịch vụ:** `5007`
* **Lớp Server:** `logger.MessageLoggerServer`
* **Lớp Client:** `logger.MessageLoggerClient`
* **Tính năng bảo mật:** Chặn tấn công Path Traversal (`../`), ghi nhật ký an toàn vào `data/logs/<clientId>.txt`.

```bash
# 1. Khởi động Logger Server nền
java -cp lab_04/out logger.MessageLoggerServer 5007 &
LOG_PID=$!

# 2. Test Client gửi tin nhắn hợp lệ
(printf "HELLO client_01\n"; sleep 0.3; \
 printf "LOG Giao dich chuyen tien thanh cong 500.000d\n"; sleep 0.3; \
 printf "LOG He thong hoat dong binh thuong\n"; sleep 0.3; \
 printf "QUIT\n") | java -cp lab_04/out logger.MessageLoggerClient localhost 5007

# 3. Test Client cố ý tấn công Path Traversal -> Server sẽ từ chối
(printf "HELLO ../hacker_inject\n"; sleep 0.3; \
 printf "QUIT\n") | java -cp lab_04/out logger.MessageLoggerClient localhost 5007

kill $LOG_PID

# 4. Kiểm tra file log đã được ghi thực tế:
cat data/logs/client_01.txt
```

---

### BÀI 8: TRUYỀN FILE TCP AN TOÀN XÁC THỰC SHA-256 (`FileTransfer`)
* **Cổng dịch vụ:** `5008`
* **Lớp Server:** `filetransfer.FileTransferServer`
* **Lớp Client:** `filetransfer.FileTransferClient`
* **Tính năng:** Gửi metadata, stream nhị phân byte, kiểm tra tính toàn vẹn bằng SHA-256 Digest.

```bash
# 1. Tạo file mẫu để truyền thử
mkdir -p data/uploads
echo "Day la file tai lieu quan trong kiem thu truyen file Socket Java 17 SHA-256" > data/sample_upload.txt

# 2. Khởi động FileTransferServer nền (lưu vào data/uploads)
java -cp lab_04/out filetransfer.FileTransferServer 5008 data/uploads &
FT_PID=$!

# 3. Chạy Client gửi file và xác thực mã băm SHA-256
java -cp lab_04/out filetransfer.FileTransferClient localhost 5008 data/sample_upload.txt

kill $FT_PID

# 4. So sánh mã SHA-256 giữa file nguồn và file đã nhận:
shasum -a 256 data/sample_upload.txt
shasum -a 256 data/uploads/sample_upload.txt
```

---

### BÀI 9: PHÁT SÓNG THÔNG BÁO MULTICAST (`Multicast Notification`)
* **Địa chỉ nhóm Multicast:** `239.255.0.1:5010`
* **Lớp Receiver:** `multicast.MulticastReceiver`
* **Lớp Sender:** `multicast.MulticastSender`
* **Tính năng:** Tự động phát hiện card mạng chính (`en0`), hỗ trợ đa Receiver gia nhập (`joinGroup`) và rời nhóm sạch sẽ (`leaveGroup`).

#### Cách chạy kiểm thử qua 2 Terminal:
* **Terminal 1 (Chạy Receiver lắng nghe thông báo):**
  ```bash
  java -cp lab_04/out multicast.MulticastReceiver
  ```
* **Terminal 2 (Chạy Sender phát thông báo):**
  ```bash
  (printf "Thong bao khan: He thong bao tri luc 22h00\n"; sleep 0.5; \
   printf "Thong bao so 2: Kiem tra ket noi Multicast thanh cong\n"; sleep 0.5; \
   printf "QUIT\n") | java -cp lab_04/out multicast.MulticastSender
  ```

---

### BÀI 10: TỰ ĐỘNG KHÁM PHÁ DỊCH VỤ VÀ KẾT NỐI TCP (`Service Discovery`)
* **Cổng UDP Broadcast Discovery:** `5011`
* **Cổng TCP Service:** `5012`
* **Lớp Server:** `discovery.DiscoveryServer`
* **Lớp Client:** `discovery.DiscoveryClient`
* **Quy trình:** Client phát sóng UDP tìm kiếm -> Server phản hồi thông tin IP & TCP Port -> Client tự động mở kết nối TCP đến Server để xử lý chuỗi (`UPPER`, `REVERSE`).

```bash
# 1. Khởi động DiscoveryServer (chạy cả dịch vụ UDP discovery và TCP service)
java -cp lab_04/out discovery.DiscoveryServer 5011 5012 &
DISC_PID=$!

# 2. Chạy Client tự động khám phá và tương tác dịch vụ:
(printf "UPPER xin chao tu Discovery Client\n"; sleep 0.5; \
 printf "REVERSE He Thong Tich Hop\n"; sleep 0.5; \
 printf "QUIT\n") | java -cp lab_04/out discovery.DiscoveryClient 5011

kill $DISC_PID
```

---

## III. BẢNG TỔNG HỢP CÁC CỔNG MẠNG SỬ DỤNG TRONG LAB 04

| Bài | Tên chức năng | Giao thức | Cổng mặc định | Ghi chú |
| :--- | :--- | :---: | :---: | :--- |
| **Ví dụ 4.2** | TCP Command Server | TCP | `5050` / `5000` | Cổng 5050 tránh AirPlay của macOS |
| **Ví dụ 4.4** | UDP Echo Server | UDP | `5001` | Timeout 3 giây |
| **Bài 1** | Host & URI Inspector | DNS / HTTP | - | Phân giải IP & URL |
| **Bài 2** | Digit to Word | TCP | `5002` | Chuyển đổi số 0 - 9 |
| **Bài 3** | DateTime Service | TCP & UDP | `5003` (TCP), `5004` (UDP) | So sánh server dừng |
| **Bài 4** | Remote Calculator | TCP | `5005` | 8 ca kiểm thử máy tính |
| **Bài 5** | Multi-client Chat | TCP | `5006` / `5056` | ThreadPool + Broadcast |
| **Bài 6** | Benchmark TCP vs UDP | TCP & UDP | Tự động | 1000 gói tin |
| **Bài 7** | Message Logger | TCP | `5007` | Chống path traversal |
| **Bài 8** | File Transfer SHA-256 | TCP | `5008` | Stream nhị phân + SHA-256 |
| **Bài 9** | Multicast Notification | UDP Multicast | `5010` | Nhóm 239.255.0.1 |
| **Bài 10** | Service Discovery | UDP & TCP | `5011` (UDP), `5012` (TCP) | Broadcast khám phá dịch vụ |
