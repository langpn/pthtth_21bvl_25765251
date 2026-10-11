package discovery;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Bài tập đề xuất 10: UDP Discovery kết hợp TCP Service - Server.
 * - Chạy UDP Discovery Listener trên cổng 5011: Lắng nghe yêu cầu "DISCOVER_SERVICE"
 *   và phản hồi "SERVICE EchoTCP 5012 1.0" về cho client.
 * - Chạy TCP Service trên cổng 5012: Dịch vụ Echo và xử lý chuỗi (UPPER, REVERSE, TIME).
 */
public class DiscoveryServer {
    public static final int UDP_DISCOVERY_PORT = 5011;
    public static final int TCP_SERVICE_PORT = 5012;
    public static final String SERVICE_NAME = "EchoTCP";
    public static final String SERVICE_VERSION = "1.0";

    public static void main(String[] args) {
        int udpPort = args.length > 0 ? Integer.parseInt(args[0]) : UDP_DISCOVERY_PORT;
        int tcpPort = args.length > 1 ? Integer.parseInt(args[1]) : TCP_SERVICE_PORT;
        AtomicBoolean running = new AtomicBoolean(true);

        System.out.println("================================================================================");
        System.out.println("KHỞI ĐỘNG HỆ THỐNG: UDP DISCOVERY & TCP SERVICE INTEGRATED");
        System.out.println("================================================================================");

        // 1. Khởi chạy luồng TCP Service
        Thread tcpThread = new Thread(() -> startTcpService(tcpPort, running));
        tcpThread.start();

        // 2. Chạy luồng UDP Discovery Listener
        startUdpDiscoveryListener(udpPort, tcpPort, running);
    }

    private static void startUdpDiscoveryListener(int udpPort, int tcpPort, AtomicBoolean running) {
        try (DatagramSocket udpSocket = new DatagramSocket(udpPort)) {
            System.out.printf("[UDP Discovery] Đang lắng nghe yêu cầu khám phá trên cổng %d...%n", udpPort);

            byte[] buffer = new byte[1024];
            while (running.get()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(packet);

                String request = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
                System.out.printf("[UDP Discovery] Nhận từ %s:%d -> \"%s\"%n",
                        packet.getAddress().getHostAddress(), packet.getPort(), request);

                if ("DISCOVER_SERVICE".equalsIgnoreCase(request)) {
                    String response = String.format("SERVICE %s %d %s", SERVICE_NAME, tcpPort, SERVICE_VERSION);
                    byte[] respData = response.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket replyPacket = new DatagramPacket(
                            respData, respData.length, packet.getAddress(), packet.getPort());
                    udpSocket.send(replyPacket);
                    System.out.printf("[UDP Discovery] -> Đã gửi phản hồi: \"%s\" tới %s:%d%n",
                            response, packet.getAddress().getHostAddress(), packet.getPort());
                }
            }
        } catch (IOException e) {
            System.err.println("[UDP Discovery] Lỗi: " + e.getMessage());
        }
    }

    private static void startTcpService(int tcpPort, AtomicBoolean running) {
        try (ServerSocket server = new ServerSocket(tcpPort)) {
            System.out.printf("[TCP Service] Đang phục vụ dịch vụ %s v%s trên cổng %d...%n",
                    SERVICE_NAME, SERVICE_VERSION, tcpPort);

            while (running.get()) {
                Socket socket = server.accept();
                new Thread(() -> handleTcpClient(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("[TCP Service] Lỗi: " + e.getMessage());
        }
    }

    private static void handleTcpClient(Socket socket) {
        String remote = String.valueOf(socket.getRemoteSocketAddress());
        System.out.println("[TCP Service] Kết nối mới từ: " + remote);

        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            out.println("OK WELCOME_TO_" + SERVICE_NAME + "_v" + SERVICE_VERSION + " (Hỗ trợ: ECHO <text>, UPPER <text>, REVERSE <text>, QUIT)");

            String line;
            while ((line = in.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                if (trimmed.regionMatches(true, 0, "ECHO ", 0, 5)) {
                    out.println("OK " + trimmed.substring(5));
                } else if (trimmed.regionMatches(true, 0, "UPPER ", 0, 6)) {
                    out.println("OK " + trimmed.substring(6).toUpperCase());
                } else if (trimmed.regionMatches(true, 0, "REVERSE ", 0, 8)) {
                    String txt = trimmed.substring(8);
                    out.println("OK " + new StringBuilder(txt).reverse());
                } else {
                    out.println("ERR UNKNOWN_COMMAND");
                }
            }
        } catch (IOException e) {
            System.err.println("[TCP Service] Ngắt kết nối từ " + remote + ": " + e.getMessage());
        }
    }
}
