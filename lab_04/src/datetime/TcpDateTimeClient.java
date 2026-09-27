package datetime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpDateTimeClient {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : TcpDateTimeServer.DEFAULT_PORT;

        try (Socket socket = new Socket(host, port);
             BufferedReader console = new BufferedReader(
                     new InputStreamReader(System.in, StandardCharsets.UTF_8));
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(
                     socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            System.out.println("Đã kết nối TCP DateTime Server [" + host + ":" + port + "]. Nhập DATE, TIME, DATETIME hoặc QUIT:");

            String command;
            while ((command = console.readLine()) != null) {
                out.println(command);
                String response = in.readLine();
                if (response == null) {
                    System.out.println("Server đã đóng kết nối hoặc bị ngắt đột ngột.");
                    break;
                }
                System.out.println("Server phản hồi: " + response);
                if (command.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi TCP Client: " + e.getMessage());
        }
    }
}
