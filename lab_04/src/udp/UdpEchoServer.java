package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class UdpEchoServer {
    public static final int DEFAULT_PORT = 5001;

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        byte[] buffer = new byte[4096];

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP server listening on port " + port);
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String message = new String(request.getData(),
                        request.getOffset(), request.getLength(),
                        StandardCharsets.UTF_8);

                String text = "ACK " + message.toUpperCase(Locale.ROOT);
                byte[] responseData = text.getBytes(StandardCharsets.UTF_8);

                DatagramPacket response = new DatagramPacket(
                        responseData, responseData.length,
                        request.getAddress(), request.getPort());
                socket.send(response);
            }
        } catch (IOException e) {
            System.err.println("UDP server error: " + e.getMessage());
        }
    }
}
