#!/usr/bin/env bash
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_DIR"

if [ ! -d "out/chat" ]; then
    echo ">> Chưa có bytecode, đang biên dịch..."
    ./scripts/compile.sh
fi

HOST=${1:-localhost}
PORT=${2:-5006}

echo ">> KHỞI ĐỘNG CHAT CLIENT KẾT NỐI TỚI $HOST:$PORT..."
java -cp out chat.ChatRoomClient "$HOST" "$PORT"
