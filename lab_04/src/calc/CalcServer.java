package calc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalcServer {
    public static final int DEFAULT_PORT = 5005;

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("CalcServer đang lắng nghe trên cổng " + port + "...");
            while (true) {
                Socket socket = server.accept();
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
                String trimmed = line.trim();
                if (trimmed.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }
                out.println(processCalculation(trimmed));
            }
        } catch (IOException e) {
            System.err.println("Lỗi phiên client: " + e.getMessage());
        }
    }

    public static String processCalculation(String request) {
        if (request == null || request.isBlank()) {
            return "ERR INVALID_FORMAT";
        }

        String[] parts = request.split("\\s+");
        if (parts.length != 4 || !parts[0].equalsIgnoreCase("CALC")) {
            return "ERR INVALID_FORMAT";
        }

        String op = parts[1];
        double num1, num2;

        try {
            num1 = Double.parseDouble(parts[2]);
            num2 = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            return "ERR INVALID_NUMBER";
        }

        double result;
        switch (op) {
            case "+" -> result = num1 + num2;
            case "-" -> result = num1 - num2;
            case "*" -> result = num1 * num2;
            case "/" -> {
                if (Math.abs(num2) < 1e-9) {
                    return "ERR DIVIDE_BY_ZERO";
                }
                result = num1 / num2;
            }
            default -> {
                return "ERR UNSUPPORTED_OPERATOR";
            }
        }

        // Định dạng kết quả gọn gàng: nếu là số nguyên thì in dạng số nguyên
        if (result == (long) result) {
            return "OK " + (long) result;
        } else {
            return "OK " + result;
        }
    }
}
