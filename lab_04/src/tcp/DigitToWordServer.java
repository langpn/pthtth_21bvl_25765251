package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitToWordServer {
    public static final int DEFAULT_PORT = 5002;
    private static final String[] DIGIT_WORDS = {
            "KHÔNG", "MỘT", "HAI", "BA", "BỐN",
            "NĂM", "SÁU", "BẢY", "TÁM", "CHÍN"
    };

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("DigitToWordServer đang lắng nghe trên cổng " + port + "...");
            while (true) {
                Socket socket = server.accept();
                System.out.println("Kết nối mới từ: " + socket.getRemoteSocketAddress());
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Lỗi Server: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(
                     socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String line;
            while ((line = in.readLine()) != null) {
                // Kiểm tra lệnh QUIT
                if (line.trim().equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                String response = processDigit(line);
                out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Lỗi giao tiếp client: " + e.getMessage());
        } finally {
            System.out.println("Đã ngắt kết nối: " + socket.getRemoteSocketAddress());
        }
    }

    public static String processDigit(String rawInput) {
        // Đề bài yêu cầu: "Nếu dữ liệu không phải đúng một chữ số, server trả ERR INVALID_DIGIT"
        // Ca biên bắt buộc: 0, 9, chuỗi rỗng, 10, ký tự a, dữ liệu có khoảng trắng
        if (rawInput == null || rawInput.length() != 1) {
            return "ERR INVALID_DIGIT";
        }

        char c = rawInput.charAt(0);
        if (c >= '0' && c <= '9') {
            int digit = c - '0';
            return "OK " + DIGIT_WORDS[digit];
        }

        return "ERR INVALID_DIGIT";
    }
}
