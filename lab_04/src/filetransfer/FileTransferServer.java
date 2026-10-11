package filetransfer;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Bài tập đề xuất 8: Truyền file TCP an toàn (Safe TCP File Transfer).
 * - Client gửi metadata: FILE <fileName> <fileLength> <sha256>
 * - Server phòng chống Directory Traversal: Chỉ lấy tên file cơ sở, chặn '..', '/', '\'
 * - Server đọc chính xác số byte đã khai báo (không dùng EOF)
 * - Kiểm tra tính toàn vẹn bằng SHA-256, trả về OK hoặc ERR HASH_MISMATCH
 */
public class FileTransferServer {
    public static final int DEFAULT_PORT = 5008;
    private static final Path UPLOAD_DIR = Paths.get("uploads");

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        try {
            Files.createDirectories(UPLOAD_DIR);
        } catch (IOException e) {
            System.err.println("Không thể tạo thư mục uploads: " + e.getMessage());
            return;
        }

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("FileTransferServer đang lắng nghe trên cổng " + port + "...");
            System.out.println("Thư mục lưu trữ: " + UPLOAD_DIR.toAbsolutePath());

            while (true) {
                Socket socket = server.accept();
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Lỗi Server: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        String clientAddress = String.valueOf(socket.getRemoteSocketAddress());
        try (socket;
             BufferedInputStream rawIn = new BufferedInputStream(socket.getInputStream());
             BufferedReader lineReader = new BufferedReader(new InputStreamReader(rawIn, StandardCharsets.UTF_8));
             OutputStream rawOut = new BufferedOutputStream(socket.getOutputStream());
             PrintWriter lineWriter = new PrintWriter(new OutputStreamWriter(rawOut, StandardCharsets.UTF_8), true)) {

            // 1. Đọc dòng metadata
            String metadataLine = lineReader.readLine();
            if (metadataLine == null || !metadataLine.startsWith("FILE ")) {
                lineWriter.println("ERR INVALID_METADATA_FORMAT");
                return;
            }

            String[] parts = metadataLine.trim().split("\\s+");
            if (parts.length != 4) {
                lineWriter.println("ERR INVALID_METADATA_ARGUMENTS (Yêu cầu: FILE <fileName> <fileLength> <sha256>)");
                return;
            }

            String rawFileName = parts[1];
            long expectedLength;
            try {
                expectedLength = Long.parseLong(parts[2]);
                if (expectedLength < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                lineWriter.println("ERR INVALID_FILE_LENGTH");
                return;
            }

            String expectedHash = parts[3].toLowerCase();

            // Kiểm tra an toàn tên file (chống Directory Traversal)
            if (rawFileName.contains("..") || rawFileName.contains("/") || rawFileName.contains("\\")) {
                lineWriter.println("ERR PATH_TRAVERSAL_DETECTED (Tên file không được chứa ký tự điều hướng thư mục)");
                System.out.println("Cảnh báo bảo mật từ [" + clientAddress + "]: Phát hiện ký tự nguy hiểm trong tên file: " + rawFileName);
                return;
            }

            Path safeFilePath = UPLOAD_DIR.resolve(Paths.get(rawFileName).getFileName().toString()).normalize();
            if (!safeFilePath.startsWith(UPLOAD_DIR.normalize())) {
                lineWriter.println("ERR SECURITY_VIOLATION");
                return;
            }

            lineWriter.println("OK READY_FOR_STREAM");

            // 2. Nhận dữ liệu nhị phân chính xác số byte khai báo và tính SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            long bytesRemaining = expectedLength;
            byte[] buffer = new byte[8192];

            try (OutputStream fileOut = new BufferedOutputStream(Files.newOutputStream(safeFilePath))) {
                while (bytesRemaining > 0) {
                    int toRead = (int) Math.min(buffer.length, bytesRemaining);
                    int bytesRead = rawIn.read(buffer, 0, toRead);
                    if (bytesRead == -1) {
                        break; // Mất kết nối giữa chừng
                    }
                    fileOut.write(buffer, 0, bytesRead);
                    digest.update(buffer, 0, bytesRead);
                    bytesRemaining -= bytesRead;
                }
            }

            // Kiểm tra xem đã nhận đủ số byte khai báo chưa
            if (bytesRemaining > 0) {
                Files.deleteIfExists(safeFilePath);
                lineWriter.println("ERR INCOMPLETE_STREAM (Thiếu dữ liệu so với độ dài khai báo)");
                return;
            }

            // 3. So khớp mã băm SHA-256
            String computedHash = HexFormat.of().formatHex(digest.digest()).toLowerCase();
            if (computedHash.equals(expectedHash)) {
                lineWriter.println("OK FILE_SAVED " + computedHash);
                System.out.printf("Đã nhận và lưu file thành công từ [%s]: %s (%d bytes, SHA-256: %s)%n",
                        clientAddress, safeFilePath.getFileName(), expectedLength, computedHash);
            } else {
                Files.deleteIfExists(safeFilePath);
                lineWriter.println("ERR HASH_MISMATCH (Kỳ vọng: " + expectedHash + ", Thực tế: " + computedHash + ")");
                System.out.println("Lỗi toàn vẹn hash từ [" + clientAddress + "] cho file: " + rawFileName);
            }

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Thuật toán mã hóa không hỗ trợ: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Lỗi truyền nhận file từ [" + clientAddress + "]: " + e.getMessage());
        }
    }
}
