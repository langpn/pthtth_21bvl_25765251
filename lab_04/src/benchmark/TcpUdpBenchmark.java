package benchmark;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Bài tập đề xuất 6: Thực nghiệm đo lường so sánh hiệu năng TCP và UDP.
 * - Gửi 1000 thông điệp kích thước cố định (256 bytes) trên localhost.
 * - Đo tổng thời gian, số phản hồi thành công, độ trễ và thông lượng.
 * - Lặp lại 5 vòng đo để tính giá trị trung bình chính xác.
 */
public class TcpUdpBenchmark {
    public static final int TCP_PORT = 5060;
    public static final int UDP_PORT = 5061;
    public static final int NUM_MESSAGES = 1000;
    public static final int MESSAGE_SIZE = 256;
    public static final int NUM_ROUNDS = 5;
    public static final int UDP_TIMEOUT_MS = 1000;

    public static void main(String[] args) throws Exception {
        System.out.println("================================================================================");
        System.out.println("BÀI TẬP 6: THỰC NGHIỆM SO SÁNH HIỆU NĂNG TCP VÀ UDP (1000 GÓI TIN / VÒNG)");
        System.out.println("Môi trường: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") - Java " + System.getProperty("java.version"));
        System.out.println("Cấu hình: " + NUM_MESSAGES + " gói tin x " + MESSAGE_SIZE + " bytes | Số vòng đo: " + NUM_ROUNDS);
        System.out.println("================================================================================");

        // Chuẩn bị payload 256 bytes cố định
        byte[] payload = new byte[MESSAGE_SIZE];
        Arrays.fill(payload, (byte) 'X');
        payload[payload.length - 1] = '\n';

        // 1. Chạy Server nền cho TCP và UDP
        AtomicBoolean running = new AtomicBoolean(true);
        Thread tcpServerThread = new Thread(() -> startTcpServer(running));
        Thread udpServerThread = new Thread(() -> startUdpServer(running));
        tcpServerThread.start();
        udpServerThread.start();

        // Chờ 500ms để server sẵn sàng
        Thread.sleep(500);

        long[] tcpTimes = new long[NUM_ROUNDS];
        int[] tcpSuccess = new int[NUM_ROUNDS];
        long[] udpTimes = new long[NUM_ROUNDS];
        int[] udpSuccess = new int[NUM_ROUNDS];

        System.out.println("\n--- BẮT ĐẦU ĐO LƯỜNG TCP (Echo Request-Response qua Socket) ---");
        for (int r = 0; r < NUM_ROUNDS; r++) {
            System.out.print("Vòng " + (r + 1) + ": Đang gửi " + NUM_MESSAGES + " thông điệp... ");
            long start = System.nanoTime();
            int success = runTcpClientRound(payload);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            tcpTimes[r] = elapsedMs;
            tcpSuccess[r] = success;
            System.out.printf("Xong trong %d ms | Phản hồi: %d/%d (%.1f%%)\n",
                    elapsedMs, success, NUM_MESSAGES, (success * 100.0 / NUM_MESSAGES));
            Thread.sleep(100);
        }

        System.out.println("\n--- BẮT ĐẦU ĐO LƯỜNG UDP (DatagramPacket Request-Response) ---");
        for (int r = 0; r < NUM_ROUNDS; r++) {
            System.out.print("Vòng " + (r + 1) + ": Đang gửi " + NUM_MESSAGES + " gói tin... ");
            long start = System.nanoTime();
            int success = runUdpClientRound(payload);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            udpTimes[r] = elapsedMs;
            udpSuccess[r] = success;
            System.out.printf("Xong trong %d ms | Phản hồi: %d/%d (%.1f%%)\n",
                    elapsedMs, success, NUM_MESSAGES, (success * 100.0 / NUM_MESSAGES));
            Thread.sleep(100);
        }

        // Tắt server
        running.set(false);
        // Gửi gói kích hoạt tắt UDP
        try (DatagramSocket s = new DatagramSocket()) {
            byte[] exit = "SHUTDOWN".getBytes(StandardCharsets.UTF_8);
            s.send(new DatagramPacket(exit, exit.length, InetAddress.getByName("localhost"), UDP_PORT));
        } catch (Exception ignored) {}

        // 2. Tính toán thống kê và in bảng kết quả
        printSummaryTable(tcpTimes, tcpSuccess, udpTimes, udpSuccess);
    }

    private static int runTcpClientRound(byte[] payload) {
        int success = 0;
        try (Socket socket = new Socket("localhost", TCP_PORT);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {

            byte[] recvBuf = new byte[MESSAGE_SIZE];
            for (int i = 0; i < NUM_MESSAGES; i++) {
                out.write(payload);
                out.flush();
                int totalRead = 0;
                while (totalRead < MESSAGE_SIZE) {
                    int read = in.read(recvBuf, totalRead, MESSAGE_SIZE - totalRead);
                    if (read == -1) break;
                    totalRead += read;
                }
                if (totalRead == MESSAGE_SIZE) {
                    success++;
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi TCP Client: " + e.getMessage());
        }
        return success;
    }

    private static int runUdpClientRound(byte[] payload) {
        int success = 0;
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(UDP_TIMEOUT_MS);
            InetAddress addr = InetAddress.getByName("localhost");
            byte[] recvBuf = new byte[MESSAGE_SIZE + 64];

            for (int i = 0; i < NUM_MESSAGES; i++) {
                DatagramPacket sendPkt = new DatagramPacket(payload, payload.length, addr, UDP_PORT);
                socket.send(sendPkt);

                DatagramPacket recvPkt = new DatagramPacket(recvBuf, recvBuf.length);
                try {
                    socket.receive(recvPkt);
                    if (recvPkt.getLength() == MESSAGE_SIZE) {
                        success++;
                    }
                } catch (SocketTimeoutException te) {
                    // Mất gói hoặc timeout
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi UDP Client: " + e.getMessage());
        }
        return success;
    }

    private static void startTcpServer(AtomicBoolean running) {
        try (ServerSocket server = new ServerSocket(TCP_PORT)) {
            server.setSoTimeout(1000);
            while (running.get()) {
                try {
                    Socket client = server.accept();
                    new Thread(() -> {
                        try (client;
                             InputStream in = client.getInputStream();
                             OutputStream out = client.getOutputStream()) {
                            byte[] buf = new byte[MESSAGE_SIZE];
                            int read;
                            while ((read = in.read(buf)) != -1) {
                                out.write(buf, 0, read);
                                out.flush();
                            }
                        } catch (IOException ignored) {}
                    }).start();
                } catch (SocketTimeoutException ignored) {}
            }
        } catch (IOException e) {
            if (running.get()) System.err.println("TCP Server Error: " + e.getMessage());
        }
    }

    private static void startUdpServer(AtomicBoolean running) {
        try (DatagramSocket socket = new DatagramSocket(UDP_PORT)) {
            socket.setSoTimeout(1000);
            byte[] buf = new byte[MESSAGE_SIZE + 64];
            while (running.get()) {
                try {
                    DatagramPacket pkt = new DatagramPacket(buf, buf.length);
                    socket.receive(pkt);
                    String msg = new String(pkt.getData(), 0, pkt.getLength(), StandardCharsets.UTF_8);
                    if ("SHUTDOWN".equals(msg)) break;

                    DatagramPacket reply = new DatagramPacket(pkt.getData(), pkt.getLength(), pkt.getAddress(), pkt.getPort());
                    socket.send(reply);
                } catch (SocketTimeoutException ignored) {}
            }
        } catch (IOException e) {
            if (running.get()) System.err.println("UDP Server Error: " + e.getMessage());
        }
    }

    private static void printSummaryTable(long[] tcpTimes, int[] tcpSuccess, long[] udpTimes, int[] udpSuccess) {
        double avgTcpTime = Arrays.stream(tcpTimes).average().orElse(0);
        double avgUdpTime = Arrays.stream(udpTimes).average().orElse(0);
        double avgTcpSuccess = Arrays.stream(tcpSuccess).average().orElse(0);
        double avgUdpSuccess = Arrays.stream(udpSuccess).average().orElse(0);

        System.out.println("\n================================================================================");
        System.out.println("BẢNG TỔNG KẾT KẾT QUẢ ĐO LƯỜNG HIỆU NĂNG (5 VÒNG ĐO TRUNG BÌNH)");
        System.out.println("================================================================================");
        System.out.printf("%-18s | %-25s | %-25s\n", "Chỉ số đo lường", "TCP (Hướng kết nối)", "UDP (Phi kết nối)");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-18s | %-25s | %-25s\n", "Thời gian TB (ms)", String.format("%.2f ms", avgTcpTime), String.format("%.2f ms", avgUdpTime));
        System.out.printf("%-18s | %-25s | %-25s\n", "Độ trễ TB/gói", String.format("%.3f ms", avgTcpTime / NUM_MESSAGES), String.format("%.3f ms", avgUdpTime / NUM_MESSAGES));
        System.out.printf("%-18s | %-25s | %-25s\n", "Thông lượng (msg/s)", String.format("%.0f gói/s", (NUM_MESSAGES * 1000.0) / avgTcpTime), String.format("%.0f gói/s", (NUM_MESSAGES * 1000.0) / avgUdpTime));
        System.out.printf("%-18s | %-25s | %-25s\n", "Tỉ lệ nhận phản hồi", String.format("%.1f%% (%d/1000)", (avgTcpSuccess / NUM_MESSAGES) * 100, (int) avgTcpSuccess), String.format("%.1f%% (%d/1000)", (avgUdpSuccess / NUM_MESSAGES) * 100, (int) avgUdpSuccess));
        System.out.printf("%-18s | %-25s | %-25s\n", "Tỉ lệ mất gói", String.format("%.1f%%", 100.0 - (avgTcpSuccess / NUM_MESSAGES) * 100), String.format("%.1f%%", 100.0 - (avgUdpSuccess / NUM_MESSAGES) * 100));
        System.out.println("================================================================================");
        System.out.println("Nhận xét thực nghiệm:");
        System.out.println("1. Trên localhost, UDP có xu hướng đạt độ trễ thấp hơn do không tốn chi phí kiểm tra ACK/Flow Control.");
        System.out.println("2. TCP bảo đảm độ tin cậy tuyệt đối 100% nhận đủ dữ liệu theo thứ tự nhờ cơ chế bắt tay 3 bước và truyền lại (retransmission).");
        System.out.println("3. Không thể kết luận UDP luôn tốt hơn TCP: Khi tải cao hoặc môi trường mạng có độ trễ lớn, UDP có thể bị rớt gói nếu buffer socket bị tràn, trong khi TCP tự động điều tiết tốc độ (Congestion Control).");
    }
}
