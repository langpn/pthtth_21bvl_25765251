package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    public static final int DEFAULT_PORT = 5006;
    private static final int MAX_CLIENTS = 50;

    // Quản lý danh sách client trực tuyến: nickname -> PrintWriter (Thread-safe)
    private static final ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTS);

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("ChatServer đang lắng nghe trên cổng " + port + " (ThreadPool max: " + MAX_CLIENTS + ")...");

            while (true) {
                Socket socket = server.accept();
                pool.submit(() -> handleClient(socket));
            }
        } catch (IOException e) {
            System.err.println("Lỗi ChatServer: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void handleClient(Socket socket) {
        String clientAddress = String.valueOf(socket.getRemoteSocketAddress());
        System.out.println("Kết nối mới từ: " + clientAddress);

        String nickname = null;
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(
                     socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            out.println("OK CHAT_SERVER_READY. Vui lòng nhập lệnh NICK <tên_của_bạn>:");

            String line;
            while ((line = in.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                if (trimmed.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                // Nếu chưa đăng ký nickname
                if (nickname == null) {
                    if (trimmed.regionMatches(true, 0, "NICK ", 0, 5)) {
                        String requestedNick = trimmed.substring(5).trim();
                        if (requestedNick.isEmpty() || requestedNick.contains(" ")) {
                            out.println("ERR INVALID_NICK (Biệt danh không được rỗng hoặc chứa khoảng trắng)");
                        } else if (clients.putIfAbsent(requestedNick, out) == null) {
                            nickname = requestedNick;
                            out.println("OK WELCOME " + nickname + " (Nhập MSG <nội dung>, USERS, QUIT)");
                            broadcast("[SYSTEM]: " + nickname + " đã tham gia phòng chat!", nickname);
                            System.out.println("Client [" + clientAddress + "] đã đăng ký biệt danh: " + nickname);
                        } else {
                            out.println("ERR NICK_TAKEN (Biệt danh đã tồn tại, vui lòng chọn tên khác)");
                        }
                    } else {
                        out.println("ERR MUST_SET_NICK (Bạn cần đăng ký biệt danh trước bằng lệnh NICK <tên>)");
                    }
                    continue;
                }

                // Đã có nickname, xử lý các lệnh: USERS, MSG, QUIT
                if (trimmed.equalsIgnoreCase("USERS")) {
                    String userList = String.join(", ", clients.keySet());
                    out.println("OK USERS " + clients.size() + ": [" + userList + "]");
                } else if (trimmed.regionMatches(true, 0, "MSG ", 0, 4)) {
                    String message = trimmed.substring(4).trim();
                    if (!message.isEmpty()) {
                        broadcast("[" + nickname + "]: " + message, nickname);
                        out.println("OK SENT");
                    } else {
                        out.println("ERR EMPTY_MESSAGE");
                    }
                } else {
                    out.println("ERR UNKNOWN_COMMAND (Hỗ trợ: MSG <nội dung>, USERS, QUIT)");
                }
            }
        } catch (IOException e) {
            System.err.println("Client ngắt kết nối [" + (nickname != null ? nickname : clientAddress) + "]: " + e.getMessage());
        } finally {
            if (nickname != null) {
                clients.remove(nickname);
                broadcast("[SYSTEM]: " + nickname + " đã rời phòng chat.", null);
                System.out.println("Client [" + nickname + "] đã rời phòng chat. Số người còn lại: " + clients.size());
            }
        }
    }

    // Broadcast thông điệp tới tất cả các client (ngoại trừ excludeNick nếu cần)
    private static void broadcast(String message, String excludeNick) {
        for (var entry : clients.entrySet()) {
            if (excludeNick != null && entry.getKey().equalsIgnoreCase(excludeNick)) {
                continue;
            }
            try {
                entry.getValue().println(message);
            } catch (Exception e) {
                System.err.println("Lỗi gửi tới " + entry.getKey() + ": " + e.getMessage());
            }
        }
    }
}
