package multicast;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

/**
 * Bài tập đề xuất 9: Multicast thông báo - Receiver.
 * - Tham gia nhóm multicast 239.255.0.1 trên cổng 5010.
 * - Chỉ định rõ NetworkInterface khi joinGroup theo chuẩn Java 17.
 * - Tự động phát hiện giao diện mạng thực tế (bỏ qua VPN utun), hỗ trợ chỉ định qua args.
 * - Nhận thông điệp và hiển thị kèm IP nguồn và thời gian.
 * - Rời nhóm (leaveGroup) sạch sẽ trước khi đóng socket.
 */
public class MulticastReceiver {
    public static final String MULTICAST_IP = "239.255.0.1";
    public static final int MULTICAST_PORT = 5010;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        String receiverId = args.length > 0 ? args[0] : "Receiver-" + (int)(Math.random() * 1000);
        int port = args.length > 1 ? Integer.parseInt(args[1]) : MULTICAST_PORT;
        String ifName = args.length > 2 ? args[2] : null;

        try {
            InetAddress group = InetAddress.getByName(MULTICAST_IP);
            SocketAddress groupAddress = new InetSocketAddress(group, port);
            NetworkInterface netIf = (ifName != null) ? NetworkInterface.getByName(ifName) : findMulticastInterface();

            if (netIf == null) {
                System.err.println("Không tìm thấy giao diện mạng hỗ trợ Multicast trên máy tính.");
                return;
            }

            System.out.printf("[%s] Khởi động trên giao diện: %s (%s)%n", receiverId, netIf.getName(), netIf.getDisplayName());
            System.out.printf("[%s] Đang tham gia nhóm Multicast %s:%d...%n", receiverId, MULTICAST_IP, port);

            MulticastSocket socket = new MulticastSocket(port);
            socket.joinGroup(groupAddress, netIf);
            System.out.printf("[%s] Đã tham gia nhóm thành công! Đang chờ thông báo từ Sender...%n", receiverId);

            // Đăng ký Shutdown Hook để rời nhóm sạch sẽ
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.printf("%n[%s] Đang rời nhóm Multicast...%n", receiverId);
                    socket.leaveGroup(groupAddress, netIf);
                    socket.close();
                    System.out.printf("[%s] Đã đóng socket an toàn.%n", receiverId);
                } catch (IOException e) {
                    System.err.println("Lỗi khi rời nhóm: " + e.getMessage());
                }
            }));

            byte[] buffer = new byte[2048];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                String now = LocalDateTime.now().format(TIME_FMT);
                System.out.printf("[%s] [%s] Nhận từ %s:%d -> %s%n",
                        now, receiverId, packet.getAddress().getHostAddress(), packet.getPort(), message);

                if (message.equalsIgnoreCase("QUIT_ALL")) {
                    System.out.printf("[%s] Nhận lệnh dừng toàn hệ thống.%n", receiverId);
                    break;
                }
            }

            socket.leaveGroup(groupAddress, netIf);
            socket.close();
        } catch (IOException e) {
            System.err.printf("[%s] Lỗi Multicast: %s%n", receiverId, e.getMessage());
        }
    }

    public static NetworkInterface findMulticastInterface() throws SocketException {
        // Ưu tiên các giao diện mạng vật lý chính: en0, eth0, wlan0
        String[] preferred = {"en0", "eth0", "wlan0"};
        for (String name : preferred) {
            NetworkInterface ni = NetworkInterface.getByName(name);
            if (ni != null && ni.isUp() && ni.supportsMulticast()) {
                return ni;
            }
        }

        // Nếu không có, duyệt tìm giao diện bất kỳ ngoại trừ VPN tunnel
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        NetworkInterface fallbackLoopback = null;

        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            if (!ni.isUp() || !ni.supportsMulticast()) continue;
            String name = ni.getName().toLowerCase();
            if (name.startsWith("utun") || name.startsWith("tun") || name.startsWith("tap")
                    || name.startsWith("awdl") || name.startsWith("llw") || name.startsWith("anpi")) {
                continue; // Bỏ qua VPN ảo
            }
            if (ni.isLoopback()) {
                if (fallbackLoopback == null) fallbackLoopback = ni;
            } else {
                return ni;
            }
        }
        return fallbackLoopback;
    }
}
