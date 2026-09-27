package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiClientTcpServer {
    private static final int DEFAULT_PORT = 5000;
    private static final int MAX_CLIENTS = 20;

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTS);
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Multi-client server on port " + port + " (ThreadPool max: " + MAX_CLIENTS + ")");
            while (true) {
                Socket socket = server.accept();
                pool.submit(() -> {
                    String client = String.valueOf(socket.getRemoteSocketAddress());
                    System.out.println("Connected: " + client);
                    try (socket) {
                        TcpCommandServer.serve(socket);
                    } catch (IOException e) {
                        System.err.println("Client " + client + " failed: " + e.getMessage());
                    } finally {
                        System.out.println("Disconnected: " + client);
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}
