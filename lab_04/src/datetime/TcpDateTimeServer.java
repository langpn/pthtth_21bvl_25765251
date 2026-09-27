package datetime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TcpDateTimeServer {
    public static final int DEFAULT_PORT = 5003;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH mm ss");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd MM yyyy HH mm ss");

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("TCP DateTime Server đang lắng nghe trên cổng " + port + "...");
            while (true) {
                Socket socket = server.accept();
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Lỗi TCP Server: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(
                     socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String command;
            while ((command = in.readLine()) != null) {
                String trimmed = command.trim();
                if (trimmed.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }
                String response = processCommand(trimmed);
                out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Client ngắt kết nối: " + e.getMessage());
        }
    }

    public static String processCommand(String cmd) {
        if (cmd.equalsIgnoreCase("DATE")) {
            return "OK " + LocalDate.now().format(DATE_FMT);
        }
        if (cmd.equalsIgnoreCase("TIME")) {
            return "OK " + LocalTime.now().format(TIME_FMT);
        }
        if (cmd.equalsIgnoreCase("DATETIME")) {
            return "OK " + LocalDateTime.now().format(DATETIME_FMT);
        }
        return "ERR UNKNOWN_COMMAND";
    }
}
