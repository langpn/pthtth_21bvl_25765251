package logger;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MessageLoggerClient {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : MessageLoggerServer.DEFAULT_PORT;

        try (Socket socket = new Socket(host, port);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            System.out.println("Đã kết nối MessageLoggerServer [" + host + ":" + port + "]");
            System.out.println("Server: " + in.readLine());
            System.out.println("Cú pháp: HELLO <clientId> -> sau đó nhập tin nhắn -> gõ 'QUIT' để thoát");

            String line;
            while ((line = console.readLine()) != null) {
                out.println(line);
                String resp = in.readLine();
                if (resp == null) {
                    System.out.println("Server đã đóng kết nối.");
                    break;
                }
                System.out.println("Server: " + resp);
                if (line.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi kết nối tới Server: " + e.getMessage());
        }
    }
}
