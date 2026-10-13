package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Luồng xử lý độc lập cho từng Client kết nối tới Server.
 * Chịu trách nhiệm quản lý phiên làm việc, đọc/ghi dữ liệu,
 * xử lý logic chọn phòng, chuyển phòng (/quit), và ngắt kết nối (/exit).
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ChatRoomServer server;
    private String nickname;
    private Room currentRoom;
    private BufferedReader in;
    private PrintWriter out;
    private volatile boolean running = true;

    public ClientHandler(Socket socket, ChatRoomServer server) {
        this.socket = socket;
        this.server = server;
    }

    public String getNickname() {
        return nickname;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    @Override
    public void run() {
        String clientIp = String.valueOf(socket.getRemoteSocketAddress());
        System.out.println("[KẾT NỐI MỚI] Từ địa chỉ: " + clientIp);

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            out.println("==================================================");
            out.println("   CHÀO MỪNG BẠN ĐẾN VỚI HỆ THỐNG CHAT TCP NHÓM   ");
            out.println("==================================================");

            // 1. Bước đăng ký Tên (Nickname)
            while (running) {
                out.println("Vui lòng nhập Tên của bạn:");
                String input = in.readLine();
                if (input == null) return;

                String trimmed = input.trim();
                if (trimmed.isEmpty()) {
                    out.println(">> [LỖI] Tên không được để trống!");
                    continue;
                }
                if (trimmed.contains(" ")) {
                    out.println(">> [LỖI] Tên không được chứa khoảng trắng!");
                    continue;
                }

                if (server.registerNickname(trimmed, this)) {
                    this.nickname = trimmed;
                    out.println(">> [XÁC NHẬN] Xin chào " + nickname + "! Bạn đã kết nối thành công.");
                    System.out.println("[ĐĂNG KÝ] Client " + clientIp + " đặt tên là: " + nickname);
                    break;
                } else {
                    out.println(">> [LỖI] Tên '" + trimmed + "' đã có người sử dụng. Vui lòng chọn tên khác!");
                }
            }

            // 2. Vòng lặp điều hướng: Menu phòng và Chat trong phòng
            while (running) {
                if (currentRoom == null) {
                    // TRẠNG THÁI 1: Ở ngoài sảnh chờ -> Chọn phòng
                    out.println();
                    out.println(server.getRoomMenu());
                    out.println(">> Chọn phòng (nhập Số hoặc Tên phòng), hoặc /exit để thoát:");

                    String choice = in.readLine();
                    if (choice == null) break;
                    choice = choice.trim();

                    if (choice.equalsIgnoreCase("/exit")) {
                        out.println(">> Tạm biệt bạn!");
                        break;
                    }

                    if (choice.isEmpty()) continue;

                    Room selectedRoom = server.resolveRoom(choice);
                    if (selectedRoom != null) {
                        this.currentRoom = selectedRoom;
                        this.currentRoom.addMember(this);
                        out.println();
                        out.println("==================================================");
                        out.println("  BẠN ĐÃ VÀO PHÒNG: " + currentRoom.getName().toUpperCase());
                        out.println("  - Gõ tin nhắn để trò chuyện với các bạn trong phòng.");
                        out.println("  - Nhập /quit để rời phòng và quay lại danh sách.");
                        out.println("  - Nhập /exit để ngắt kết nối và thoát chương trình.");
                        out.println("==================================================");
                    } else {
                        out.println(">> [LỖI] Lựa chọn không hợp lệ. Vui lòng thử lại!");
                    }
                } else {
                    // TRẠNG THÁI 2: Đang ở trong phòng chat -> Nhận và phát tán tin nhắn
                    String chatLine = in.readLine();
                    if (chatLine == null) break;

                    String trimmedMsg = chatLine.trim();
                    if (trimmedMsg.isEmpty()) continue;

                    if (trimmedMsg.equalsIgnoreCase("/quit")) {
                        Room roomLeft = currentRoom;
                        currentRoom = null;
                        roomLeft.removeMember(nickname);
                        out.println();
                        out.println(">> [HỆ THỐNG] Bạn đã rời khỏi phòng " + roomLeft.getName() + ".");
                    } else if (trimmedMsg.equalsIgnoreCase("/exit")) {
                        Room roomLeft = currentRoom;
                        currentRoom = null;
                        roomLeft.removeMember(nickname);
                        out.println(">> Tạm biệt bạn!");
                        break;
                    } else {
                        // Tin nhắn trò chuyện bình thường
                        currentRoom.broadcast(nickname, chatLine);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("[NGẮT KẾT NỐI] Client " + (nickname != null ? nickname : clientIp) + " ngắt đột ngột: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    /**
     * Dọn dẹp tài nguyên an toàn khi Client thoát hoặc bị ngắt kết nối.
     */
    private void cleanup() {
        running = false;
        if (currentRoom != null) {
            currentRoom.removeMember(nickname);
            currentRoom = null;
        }
        if (nickname != null) {
            server.unregisterNickname(nickname);
            System.out.println("[DỌN DẸP] Đã giải phóng nickname: " + nickname);
        }
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {}
    }
}
