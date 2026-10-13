package chat;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Lớp đại diện cho một phòng chat trong hệ thống.
 * Quản lý danh sách thành viên an toàn đa luồng (ConcurrentHashMap)
 * và đảm bảo cô lập tin nhắn tuyệt đối giữa các phòng.
 */
public class Room {
    private final String name;
    private final ConcurrentHashMap<String, ClientHandler> members = new ConcurrentHashMap<>();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMemberCount() {
        return members.size();
    }

    /**
     * Thêm thành viên vào phòng và thông báo cho mọi người trong phòng.
     */
    public synchronized boolean addMember(ClientHandler client) {
        if (members.putIfAbsent(client.getNickname(), client) == null) {
            broadcastSystem(client.getNickname() + " đã tham gia phòng!");
            return true;
        }
        return false;
    }

    /**
     * Rút thành viên khỏi phòng và thông báo cho các thành viên còn lại.
     */
    public synchronized void removeMember(String nickname) {
        if (members.remove(nickname) != null) {
            broadcastSystem(nickname + " đã rời phòng!");
        }
    }

    /**
     * Phát tán tin nhắn của một thành viên tới các thành viên khác TRONG CÙNG PHÒNG.
     */
    public void broadcast(String senderNickname, String message) {
        String formatted = String.format("[%s] %s: %s", name, senderNickname, message);
        for (ClientHandler client : members.values()) {
            if (!client.getNickname().equalsIgnoreCase(senderNickname)) {
                client.sendMessage(formatted);
            }
        }
    }

    /**
     * Phát tán thông báo hệ thống tới toàn bộ thành viên trong phòng.
     */
    public void broadcastSystem(String systemMessage) {
        String formatted = String.format("[%s - HỆ THỐNG]: %s", name, systemMessage);
        for (ClientHandler client : members.values()) {
            client.sendMessage(formatted);
        }
    }
}
