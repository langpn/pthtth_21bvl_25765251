import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class TextFileDemo {
    public static void main(String[] args) {
        Path file = Path.of("data", "ghi_chu.txt");

        try {
            Files.createDirectories(file.getParent());

            // 1. Ghi tệp văn bản sử dụng UTF-8
            System.out.println("=== 1. GHI VĂN BẢN VÀO TỆP (UTF-8) ===");
            try (BufferedWriter writer = Files.newBufferedWriter(
                    file, StandardCharsets.UTF_8)) {
                writer.write("Java I/O làm việc với các luồng dữ liệu.");
                writer.newLine();
                writer.write("BufferedWriter giúp ghi văn bản hiệu quả.");
                writer.newLine();
                writer.write("UTF-8 hỗ trợ tiếng Việt ổn định.");
                writer.newLine();
            }
            System.out.println("Đã ghi xong vào tệp: " + file);

            // 2. Đọc tệp văn bản và đánh số thứ tự dòng
            System.out.println("\n=== 2. ĐỌC NỘI DUNG TỆP (UTF-8) ===");
            try (BufferedReader reader = Files.newBufferedReader(
                    file, StandardCharsets.UTF_8)) {
                String line;
                int number = 1;
                while ((line = reader.readLine()) != null) {
                    System.out.printf("%d. %s%n", number++, line);
                }
            }

            // 3. Nhiệm vụ mở rộng 1 & 2: Ghi nối thêm nội dung (APPEND) và in đường dẫn tuyệt đối
            System.out.println("\n=== 3. NHIỆM VỤ MỞ RỘNG ===");
            System.out.println("Đường dẫn tuyệt đối của tệp: " + file.toAbsolutePath());

            try (BufferedWriter appendWriter = Files.newBufferedWriter(
                    file, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                appendWriter.write("Dòng nối thêm: Thử nghiệm StandardOpenOption.APPEND thành công.");
                appendWriter.newLine();
            }
            System.out.println("Đã nối thêm 1 dòng thành công.");

            // 4. Nhiệm vụ mở rộng 3: Đọc bằng charset khác (ISO-8859-1) để so sánh lỗi font
            System.out.println("\nĐọc tệp bằng ISO_8859_1 (để kiểm tra hiện tượng vỡ font tiếng Việt):");
            try (BufferedReader isoReader = Files.newBufferedReader(
                    file, StandardCharsets.ISO_8859_1)) {
                String line;
                int num = 1;
                while ((line = isoReader.readLine()) != null) {
                    System.out.printf("[%d] %s%n", num++, line);
                }
            }

        } catch (IOException e) {
            System.err.println("Lỗi xử lý tệp " + file + ": " + e.getMessage());
        }
    }
}
