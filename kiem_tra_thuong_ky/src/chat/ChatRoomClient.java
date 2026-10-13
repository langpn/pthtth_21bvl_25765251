package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Client TCP cho hệ thống Chat nhóm đa phòng.
 * Kiến trúc 2 luồng song song (Asynchronous Console UI):
 *  - Luồng 1 (Reader Thread): Chạy ngầm, liên tục đón nhận tin nhắn từ Server và in ra màn hình.
 *  - Luồng 2 (Main Thread): Đọc bàn phím người dùng và gửi gói tin lên Socket.
 */
public class ChatRoomClient {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 5006;

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : DEFAULT_HOST;
        int port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;

        System.out.println("Đang kết nối tới Chat Server tại " + host + ":" + port + "...");

        try {
            Socket socket = new Socket(host, port);
            System.out.println("[KẾT NỐI THÀNH CÔNG] Đã bắt tay với Server.\n");

            BufferedReader serverIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter serverOut = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

            // LUỒNG 1: Đọc ngầm dữ liệu từ Server gửi về và in ra console
            Thread readerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = serverIn.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    // Khi socket đóng, vòng lặp đọc sẽ kết thúc an toàn tại đây
                } finally {
                    System.out.println("\n[HỆ THỐNG] Phiên làm việc đã kết thúc.");
                }
            });
            readerThread.setDaemon(true);
            readerThread.start();

            // LUỒNG 2 (Luồng chính): Đọc bàn phím từ người dùng và gửi đi
            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                serverOut.println(input);

                if (input.trim().equalsIgnoreCase("/exit")) {
                    // Chờ nhẹ 0.3s để nhận thông điệp tạm biệt từ server trước khi đóng
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ignored) {}
                    break;
                }
            }

            // Đóng socket dọn dẹp
            if (!socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {
            System.err.println("[LỖI KẾT NỐI] Không thể kết nối tới Server: " + e.getMessage());
        }
    }
}
