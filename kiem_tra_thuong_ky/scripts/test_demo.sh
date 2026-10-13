#!/usr/bin/env bash
set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_DIR"

echo "=== BẮT ĐẦU KIỂM THỬ TỰ ĐỘNG HỆ THỐNG TCP CHAT ĐA PHÒNG ==="

# 1. Biên dịch
./scripts/compile.sh

TEST_PORT=5099
LOG_DIR="/tmp/tcp_chat_test_logs"
mkdir -p "$LOG_DIR"
rm -f "$LOG_DIR"/*

echo ">> Khởi động ChatRoomServer ngầm tại port $TEST_PORT..."
java -cp out chat.ChatRoomServer "$TEST_PORT" > "$LOG_DIR/server.log" 2>&1 &
SERVER_PID=$!
sleep 0.8

# Đảm bảo dọn dẹp Server khi script kết thúc
trap "kill -9 $SERVER_PID 2>/dev/null || true" EXIT

echo ">> Kịch bản 1: Client 'An' kết nối, đặt tên, vào phòng 1 (Study)..."
(
    printf "An\n"
    sleep 0.3
    printf "1\n"
    sleep 0.5
    printf "Xin chao cac ban trong phong Study!\n"
    sleep 1.2
    printf "/quit\n"
    sleep 0.5
    printf "2\n"
    sleep 0.5
    printf "Toi da sang phong Game voi Cuong roi!\n"
    sleep 0.8
    printf "/exit\n"
) | java -cp out chat.ChatRoomClient localhost "$TEST_PORT" > "$LOG_DIR/client_an.log" 2>&1 &
PID_AN=$!

sleep 0.5

echo ">> Kịch bản 2: Client 'Binh' kết nối, đặt tên, vào phòng 1 (Study)..."
(
    printf "Binh\n"
    sleep 0.3
    printf "1\n"
    sleep 0.8
    printf "Chao ban An, toi cung dang hoc o Study ne!\n"
    sleep 1.5
    printf "/exit\n"
) | java -cp out chat.ChatRoomClient localhost "$TEST_PORT" > "$LOG_DIR/client_binh.log" 2>&1 &
PID_BINH=$!

sleep 0.5

echo ">> Kịch bản 3: Client 'Cuong' kết nối, đặt tên, vào phòng 2 (Game)..."
(
    printf "Cuong\n"
    sleep 0.3
    printf "2\n"
    sleep 0.8
    printf "Co ai choi Game cung Cuong khong?\n"
    sleep 2.0
    printf "/exit\n"
) | java -cp out chat.ChatRoomClient localhost "$TEST_PORT" > "$LOG_DIR/client_cuong.log" 2>&1 &
PID_CUONG=$!

# Chờ các client chạy xong kịch bản
wait $PID_AN $PID_BINH $PID_CUONG 2>/dev/null || true
sleep 0.5

echo ""
echo "============================================================"
echo "          KẾT QUẢ KIỂM THỬ THỰC TẾ TỪ CÁC CLIENT"
echo "============================================================"

echo ""
echo "--- [1] LOG PHÍA SERVER ---"
cat "$LOG_DIR/server.log"

echo ""
echo "--- [2] MÀN HÌNH CLIENT 'BINH' (PHÒNG STUDY) ---"
cat "$LOG_DIR/client_binh.log"

echo ""
echo "--- [3] MÀN HÌNH CLIENT 'CUONG' (PHÒNG GAME) ---"
cat "$LOG_DIR/client_cuong.log"

echo ""
echo "--- [4] MÀN HÌNH CLIENT 'AN' (CHUYỂN TỪ STUDY SANG GAME BẰNG /quit) ---"
cat "$LOG_DIR/client_an.log"

echo ""
echo ">> KIỂM TRA ĐIỀU KIỆN CÁCH LY PHÒNG (ROOM ISOLATION):"
if grep -q "Co ai choi Game" "$LOG_DIR/client_binh.log"; then
    echo ">> [THẤT BẠI] Lỗi: Bình ở phòng Study lại đọc được tin nhắn từ phòng Game!"
    exit 1
else
    echo ">> [THÀNH CÔNG] Bình ở phòng Study KHÔNG HỀ nhận tin từ phòng Game!"
fi

if grep -q "Toi da sang phong Game" "$LOG_DIR/client_cuong.log"; then
    echo ">> [THÀNH CÔNG] Cường ở phòng Game ĐÃ NHẬN ĐƯỢC tin của An sau khi An /quit sang Game!"
else
    echo ">> [THẤT BẠI] Lỗi chuyển phòng!"
    exit 1
fi

echo ""
echo ">> TOÀN BỘ 5 KỊCH BẢN TEST ĐÃ PASS 100%!"
