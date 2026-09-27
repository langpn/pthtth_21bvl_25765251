package datetime;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UdpDateTimeServer {
    public static final int DEFAULT_PORT = 5004;

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        byte[] buffer = new byte[1024];

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP DateTime Server đang lắng nghe trên cổng " + port + " (Không cần lệnh QUIT)...");

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String cmd = new String(request.getData(),
                        request.getOffset(), request.getLength(),
                        StandardCharsets.UTF_8).trim();

                String response = TcpDateTimeServer.processCommand(cmd);
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

                DatagramPacket reply = new DatagramPacket(
                        responseBytes, responseBytes.length,
                        request.getAddress(), request.getPort());
                socket.send(reply);
            }
        } catch (IOException e) {
            System.err.println("Lỗi UDP Server: " + e.getMessage());
        }
    }
}
