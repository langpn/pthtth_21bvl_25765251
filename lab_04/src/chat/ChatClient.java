package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : ChatServer.DEFAULT_PORT;

        try {
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), StandardCharsets.UTF_8), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(
                    System.in, StandardCharsets.UTF_8));

            // Luồng phụ nhận tin nhắn bất đồng bộ từ Server và hiển thị ra màn hình
            Thread receiverThread = new Thread(() -> {
                try {
                    String serverMsg;
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println(serverMsg);
                    }
                } catch (IOException e) {
                    // Socket đóng khi thoát
                }
            });
            receiverThread.setDaemon(true);
            receiverThread.start();

            // Luồng chính đọc lệnh từ bàn phím và gửi lên Server
            String input;
            while ((input = console.readLine()) != null) {
                out.println(input);
                if (input.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

            socket.close();
            System.out.println("Đã ngắt kết nối phòng chat.");
        } catch (IOException e) {
            System.err.println("Không thể kết nối tới ChatServer [" + host + ":" + port + "]: " + e.getMessage());
        }
    }
}
