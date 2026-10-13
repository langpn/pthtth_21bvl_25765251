#!/usr/bin/env bash
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_DIR"

if [ ! -d "out/chat" ]; then
    echo ">> Chưa có bytecode, đang biên dịch..."
    ./scripts/compile.sh
fi

PORT=${1:-5006}
echo ">> KHỞI ĐỘNG CHAT SERVER TRÊN PORT $PORT..."
java -cp out chat.ChatRoomServer "$PORT"
