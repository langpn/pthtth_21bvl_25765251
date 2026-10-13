#!/usr/bin/env bash
set -e

# Chuyển về thư mục gốc của bài kiểm tra thường kỳ
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_DIR"

echo "=== ĐANG BIÊN DỊCH DỰ ÁN CHAT TCP ĐA PHÒNG (JAVA 17 LTS) ==="
mkdir -p out

# Tìm và biên dịch toàn bộ source code với mã hóa UTF-8
javac -encoding UTF-8 -d out $(find src -name "*.java")

echo ">> BIÊN DỊCH THÀNH CÔNG 100%! Bytecode lưu tại: $PROJECT_DIR/out"
