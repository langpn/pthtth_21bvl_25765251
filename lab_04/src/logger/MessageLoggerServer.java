package logger;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Bài tập đề xuất 7: Server lưu nhật ký tin nhắn (Message Logger Server).
 * - Multi-client sử dụng Thread Pool.
 * - Client định danh qua HELLO <clientId>.
 * - Kiểm tra bảo mật clientId: chỉ cho phép [a-zA-Z0-9_-], chống Directory Traversal.
 * - Ghi log an toàn vào data/logs/<clientId>.txt theo định dạng [timestamp] [remote_ip] <nội dung>.
 * - Lệnh QUIT đóng phiên an toàn.
 */
public class MessageLoggerServer {
    public static final int DEFAULT_PORT = 5007;
    private static final int MAX_THREADS = 30;
    private static final Path LOG_DIR = Paths.get("data", "logs");
    private static final Pattern CLIENT_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        try {
            Files.createDirectories(LOG_DIR);
        } catch (IOException e) {
            System.err.println("Không thể khởi tạo thư mục logs: " + e.getMessage());
            return;
        }

        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("MessageLoggerServer đang lắng nghe trên cổng " + port + "...");
            System.out.println("Thư mục lưu trữ nhật ký: " + LOG_DIR.toAbsolutePath());

            while (true) {
                Socket socket = server.accept();
                pool.submit(() -> handleClient(socket));
            }
        } catch (IOException e) {
            System.err.println("Lỗi Server: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void handleClient(Socket socket) {
        String remoteAddress = String.valueOf(socket.getRemoteSocketAddress());
        String clientId = null;
        Path clientLogFile = null;

        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(
                     socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            out.println("OK LOGGER_READY. Gửi HELLO <clientId> để bắt đầu:");

            String line;
            while ((line = in.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                if (trimmed.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                // Chưa xác thực clientId
                if (clientId == null) {
                    if (trimmed.regionMatches(true, 0, "HELLO ", 0, 6)) {
                        String requestedId = trimmed.substring(6).trim();
                        if (CLIENT_ID_PATTERN.matcher(requestedId).matches()) {
                            clientId = requestedId;
                            clientLogFile = LOG_DIR.resolve(clientId + ".txt");
                            out.println("OK HELLO " + clientId + " (Bắt đầu ghi nhật ký, kết thúc bằng QUIT)");
                            System.out.println("Client [" + remoteAddress + "] xác thực thành công clientId: " + clientId);
                        } else {
                            out.println("ERR INVALID_CLIENT_ID (Chỉ chấp nhận chữ, số, gạch ngang '-', gạch dưới '_')");
                        }
                    } else {
                        out.println("ERR MUST_SEND_HELLO (Vui lòng gửi lệnh HELLO <clientId> trước khi ghi log)");
                    }
                    continue;
                }

                // Đã có clientId, xử lý nội dung log
                String content = trimmed;
                if (trimmed.regionMatches(true, 0, "LOG ", 0, 4)) {
                    content = trimmed.substring(4).trim();
                }

                if (content.isEmpty()) {
                    out.println("ERR EMPTY_MESSAGE");
                    continue;
                }

                String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
                String logEntry = String.format("[%s] [%s] %s%n", timestamp, remoteAddress, content);

                // Ghi đồng bộ vào file log của client
                synchronized (MessageLoggerServer.class) {
                    Files.writeString(clientLogFile, logEntry, StandardCharsets.UTF_8,
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }

                out.println("OK LOGGED");
            }
        } catch (IOException e) {
            System.err.println("Client ngắt kết nối [" + (clientId != null ? clientId : remoteAddress) + "]: " + e.getMessage());
        } finally {
            if (clientId != null) {
                System.out.println("Phiên kết nối của [" + clientId + "] đã đóng an toàn.");
            }
        }
    }
}
