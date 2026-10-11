package multicast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * Bài tập đề xuất 9: Multicast thông báo - Sender.
 * - Gửi thông báo tới địa chỉ multicast 239.255.0.1:5010.
 * - Tự động phát hiện giao diện mạng chính (en0) để định tuyến gói tin không bị No route to host.
 */
public class MulticastSender {
    public static void main(String[] args) {
        String groupIp = args.length > 0 ? args[0] : MulticastReceiver.MULTICAST_IP;
        int port = args.length > 1 ? Integer.parseInt(args[1]) : MulticastReceiver.MULTICAST_PORT;
        String ifName = args.length > 2 ? args[2] : null;

        try {
            NetworkInterface netIf = (ifName != null) ? NetworkInterface.getByName(ifName) : MulticastReceiver.findMulticastInterface();
            InetAddress group = InetAddress.getByName(groupIp);

            try (MulticastSocket socket = new MulticastSocket();
                 BufferedReader console = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

                if (netIf != null) {
                    socket.setNetworkInterface(netIf);
                    System.out.printf("[MulticastSender] Định tuyến phát sóng qua giao diện: %s (%s)%n",
                            netIf.getName(), netIf.getDisplayName());
                }
                socket.setTimeToLive(2);

                System.out.println("=== MULTICAST NOTIFICATION SENDER ===");
                System.out.printf("Địa chỉ nhóm: %s:%d%n", groupIp, port);
                System.out.println("Nhập nội dung thông báo để phát sóng tới tất cả Receiver (hoặc 'QUIT' để thoát):");

                String line;
                while ((line = console.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty()) continue;

                    byte[] data = trimmed.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
                    socket.send(packet);

                    System.out.printf("-> Đã phát sóng thông báo: \"%s\" (%d bytes)%n", trimmed, data.length);
                    if (trimmed.equalsIgnoreCase("QUIT")) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi MulticastSender: " + e.getMessage());
        }
    }
}
