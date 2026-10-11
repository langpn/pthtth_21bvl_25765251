package discovery;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Bài tập đề xuất 10: UDP Discovery kết hợp TCP Service - Client.
 * - Gửi gói UDP broadcast "DISCOVER_SERVICE".
 * - Bắt phản hồi với timeout 3000ms, loại bỏ phản hồi trùng.
 * - Tự động trích xuất IP nguồn và TCP port từ thông điệp nhận được.
 * - Tự động kết nối TCP Socket tới Server và gọi thử nghiệm dịch vụ.
 */
public class DiscoveryClient {
    public static final int DISCOVERY_PORT = 5011;
    public static final int TIMEOUT_MS = 3000;

    public static void main(String[] args) {
        int discoveryPort = args.length > 0 ? Integer.parseInt(args[0]) : DISCOVERY_PORT;

        System.out.println("================================================================================");
        System.out.println("CLIENT TỰ ĐỘNG KHÁM PHÁ DỊCH VỤ (UDP DISCOVERY -> AUTO TCP CONNECT)");
        System.out.println("================================================================================");

        try (DatagramSocket udpSocket = new DatagramSocket()) {
            udpSocket.setBroadcast(true);
            udpSocket.setSoTimeout(TIMEOUT_MS);

            byte[] requestData = "DISCOVER_SERVICE".getBytes(StandardCharsets.UTF_8);

            // Gửi tới broadcast và localhost để bảo đảm nhận được trên mọi môi trường
            InetAddress broadcastAddr = InetAddress.getByName("255.255.255.255");
            InetAddress localAddr = InetAddress.getByName("localhost");

            System.out.printf("1. Đang gửi gói UDP \"DISCOVER_SERVICE\" tới cổng %d...%n", discoveryPort);
            try {
                udpSocket.send(new DatagramPacket(requestData, requestData.length, broadcastAddr, discoveryPort));
            } catch (Exception ignored) {}
            udpSocket.send(new DatagramPacket(requestData, requestData.length, localAddr, discoveryPort));

            Set<String> discoveredServices = new HashSet<>();
            byte[] recvBuffer = new byte[1024];

            System.out.println("2. Đang chờ phản hồi dịch vụ (Timeout: " + TIMEOUT_MS + " ms)...");
            long deadline = System.currentTimeMillis() + TIMEOUT_MS;

            String targetHost = null;
            int targetTcpPort = -1;

            while (System.currentTimeMillis() < deadline) {
                try {
                    DatagramPacket recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
                    udpSocket.receive(recvPacket);

                    String response = new String(recvPacket.getData(), 0, recvPacket.getLength(), StandardCharsets.UTF_8).trim();
                    String senderIp = recvPacket.getAddress().getHostAddress();
                    String serviceKey = senderIp + ":" + response;

                    if (discoveredServices.add(serviceKey)) {
                        System.out.printf("   [+] Phát hiện dịch vụ từ [%s]: \"%s\"%n", senderIp, response);

                        // Phân tích cú pháp: SERVICE <tên> <tcp_port> <phiên_bản>
                        String[] parts = response.split("\\s+");
                        if (parts.length >= 4 && "SERVICE".equalsIgnoreCase(parts[0])) {
                            targetHost = senderIp;
                            targetTcpPort = Integer.parseInt(parts[2]);
                            System.out.printf("       -> Đã phân giải: Host=%s, TCP Port=%d, Service=%s v%s%n",
                                    targetHost, targetTcpPort, parts[1], parts[3]);
                            break; // Đã tìm thấy dịch vụ, chuyển sang bước kết nối TCP
                        }
                    }
                } catch (SocketTimeoutException te) {
                    break;
                }
            }

            if (targetHost == null || targetTcpPort == -1) {
                System.out.println("Không tìm thấy dịch vụ nào đang hoạt động trong mạng.");
                return;
            }

            // 3. Tự động kết nối TCP Socket tới dịch vụ vừa khám phá
            System.out.printf("%n3. Tự động kết nối TCP tới [%s:%d]...%n", targetHost, targetTcpPort);
            try (Socket tcpSocket = new Socket(targetHost, targetTcpPort);
                 BufferedReader in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(tcpSocket.getOutputStream(), StandardCharsets.UTF_8), true)) {

                String welcome = in.readLine();
                System.out.println("   Server TCP: " + welcome);

                // Thử nghiệm gửi lệnh
                testCommand(in, out, "UPPER xin chào từ DiscoveryClient");
                testCommand(in, out, "REVERSE Hệ Thống Tích Hợp");
                testCommand(in, out, "QUIT");

                System.out.println("4. Hoàn tất quá trình tương tác dịch vụ tự động!");
            }

        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.getMessage());
        }
    }

    private static void testCommand(BufferedReader in, PrintWriter out, String command) throws IOException {
        System.out.println("   > Gửi: " + command);
        out.println(command);
        String resp = in.readLine();
        System.out.println("   < Nhận: " + resp);
    }
}
