package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server TCP cho hệ thống Chat nhóm đa phòng.
 * Kiến trúc Master-Worker sử dụng ThreadPool để phục vụ nhiều Client đồng thời.
 * Quản lý danh sách phòng chat và định tuyến thông điệp chính xác.
 */
public class ChatRoomServer {
    public static final int DEFAULT_PORT = 5006;
    private static final int MAX_WORKERS = 50;

    // Quản lý danh sách phòng (key là tên thường, value là đối tượng Room)
    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();
    private final List<String> defaultOrder = new ArrayList<>();

    // Quản lý danh sách nick name đang online để chống trùng lặp tên
    private final ConcurrentHashMap<String, ClientHandler> onlineClients = new ConcurrentHashMap<>();

    public ChatRoomServer() {
        // Khởi tạo các phòng mặc định theo yêu cầu đề bài
        createPredefinedRoom("Study");
        createPredefinedRoom("Game");
        createPredefinedRoom("TraiNghiem");
    }

    private void createPredefinedRoom(String roomName) {
        String key = roomName.toLowerCase();
        rooms.put(key, new Room(roomName));
        defaultOrder.add(key);
    }

    /**
     * Đăng ký biệt danh cho Client (Thread-safe, không phân biệt hoa thường).
     */
    public boolean registerNickname(String nickname, ClientHandler handler) {
        return onlineClients.putIfAbsent(nickname.toLowerCase(), handler) == null;
    }

    /**
     * Hủy đăng ký biệt danh khi Client rời hệ thống.
     */
    public void unregisterNickname(String nickname) {
        if (nickname != null) {
            onlineClients.remove(nickname.toLowerCase());
        }
    }

    /**
     * Tạo chuỗi Menu danh sách phòng kèm sĩ số hiện tại.
     */
    public String getRoomMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("=================== DANH SÁCH PHÒNG CHAT ===================\n");
        int index = 1;
        for (String key : defaultOrder) {
            Room r = rooms.get(key);
            if (r != null) {
                sb.append(String.format("  [%d] Phòng %-12s (Hiện có: %d thành viên)\n", 
                        index++, r.getName(), r.getMemberCount()));
            }
        }

        // Hiển thị thêm các phòng do sinh viên tự đề xuất (nếu có)
        int extraIndex = index;
        for (String key : rooms.keySet()) {
            if (!defaultOrder.contains(key)) {
                Room r = rooms.get(key);
                sb.append(String.format("  [%d] Phòng %-12s (Hiện có: %d thành viên - Tự tạo)\n", 
                        extraIndex++, r.getName(), r.getMemberCount()));
            }
        }

        sb.append("------------------------------------------------------------\n");
        sb.append(">> Nhập số thứ tự [1, 2, 3...] hoặc tên phòng để tham gia.\n");
        sb.append(">> Hoặc nhập tên phòng mới bất kỳ để tự đề xuất phòng mới!\n");
        sb.append("============================================================");
        return sb.toString();
    }

    /**
     * Phân giải lựa chọn của người dùng thành đối tượng Room.
     * Hỗ trợ chọn theo số thứ tự (1, 2, 3), tên phòng có sẵn, hoặc tự tạo phòng mới.
     */
    public Room resolveRoom(String input) {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) return null;

        // 1. Kiểm tra chọn theo số thứ tự mặc định 1, 2, 3
        try {
            int num = Integer.parseInt(trimmed);
            if (num >= 1 && num <= defaultOrder.size()) {
                String key = defaultOrder.get(num - 1);
                return rooms.get(key);
            }
        } catch (NumberFormatException ignored) {}

        // 2. Kiểm tra theo tên phòng có sẵn (không phân biệt hoa thường)
        String key = trimmed.toLowerCase();
        if (rooms.containsKey(key)) {
            return rooms.get(key);
        }

        // 3. Cho phép tự tạo phòng mới nếu tên hợp lệ (không chứa ký tự cấm)
        if (!trimmed.contains(" ") && trimmed.length() <= 30) {
            Room newRoom = new Room(trimmed);
            Room existing = rooms.putIfAbsent(key, newRoom);
            return existing != null ? existing : newRoom;
        }

        return null;
    }

    /**
     * Khởi động ServerSocket và vòng lặp tiếp nhận kết nối.
     */
    public void start(int port) {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_WORKERS);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("============================================================");
            System.out.println(" [CHATTING SERVER KHỞI ĐỘNG THÀNH CÔNG]");
            System.out.println(" - Cổng lắng nghe (Port): " + port);
            System.out.println(" - Kiến trúc: Master-Worker (FixedThreadPool: " + MAX_WORKERS + ")");
            System.out.println(" - Các phòng khởi tạo sẵn: Study, Game, TraiNghiem");
            System.out.println(" - Trạng thái: Sẵn sàng đón Client kết nối...");
            System.out.println("============================================================");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.submit(new ClientHandler(clientSocket, this));
            }
        } catch (IOException e) {
            System.err.println("[LỖI SERVER] " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        ChatRoomServer server = new ChatRoomServer();
        server.start(port);
    }
}
