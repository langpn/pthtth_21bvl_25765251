package datetime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class UdpDateTimeClient {
    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : UdpDateTimeServer.DEFAULT_PORT;

        try (DatagramSocket socket = new DatagramSocket();
             BufferedReader console = new BufferedReader(
                     new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            socket.setSoTimeout(3000); // 3 giây timeout
            InetAddress serverAddr = InetAddress.getByName(host);

            System.out.println("UDP DateTime Client sẵn sàng. Nhập DATE, TIME, DATETIME hoặc 'exit' để dừng:");

            String command;
            while ((command = console.readLine()) != null) {
                if (command.trim().equalsIgnoreCase("exit")) {
                    System.out.println("Kết thúc client UDP.");
                    break;
                }

                byte[] sendData = command.getBytes(StandardCharsets.UTF_8);
                DatagramPacket request = new DatagramPacket(sendData, sendData.length, serverAddr, port);
                socket.send(request);

                byte[] buffer = new byte[1024];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(reply);
                    String text = new String(reply.getData(),
                            reply.getOffset(), reply.getLength(),
                            StandardCharsets.UTF_8);
                    System.out.println("UDP Server phản hồi: " + text);
                } catch (SocketTimeoutException e) {
                    System.err.println("Cảnh báo: Hết 3 giây nhưng chưa nhận được phản hồi từ server (Server có thể đã dừng).");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi UDP Client: " + e.getMessage());
        }
    }
}
