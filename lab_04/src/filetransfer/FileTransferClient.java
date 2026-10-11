package filetransfer;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.HexFormat;

public class FileTransferClient {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Sử dụng: java filetransfer.FileTransferClient <đường_dẫn_file> [host] [port]");
            return;
        }

        Path localPath = Paths.get(args[0]);
        if (!Files.exists(localPath) || Files.isDirectory(localPath)) {
            System.err.println("Lỗi: File nguồn không tồn tại hoặc là thư mục: " + localPath);
            return;
        }

        String host = args.length > 1 ? args[1] : "localhost";
        int port = args.length > 2 ? Integer.parseInt(args[2]) : FileTransferServer.DEFAULT_PORT;

        try {
            // 1. Tính toán kích thước và mã băm SHA-256 của file cục bộ
            long fileLength = Files.size(localPath);
            String sha256 = computeFileSha256(localPath);
            String fileName = localPath.getFileName().toString();

            System.out.printf("Chuẩn bị gửi file: %s (%d bytes)%n", fileName, fileLength);
            System.out.println("SHA-256 cục bộ: " + sha256);

            // 2. Kết nối tới Server
            try (Socket socket = new Socket(host, port);
                 BufferedOutputStream rawOut = new BufferedOutputStream(socket.getOutputStream());
                 PrintWriter lineWriter = new PrintWriter(new OutputStreamWriter(rawOut, StandardCharsets.UTF_8), true);
                 BufferedInputStream rawIn = new BufferedInputStream(socket.getInputStream());
                 BufferedReader lineReader = new BufferedReader(new InputStreamReader(rawIn, StandardCharsets.UTF_8))) {

                // Gửi metadata
                lineWriter.println("FILE " + fileName + " " + fileLength + " " + sha256);
                String ack = lineReader.readLine();
                System.out.println("Server phản hồi: " + ack);

                if (!"OK READY_FOR_STREAM".equals(ack)) {
                    System.err.println("Server từ chối nhận file.");
                    return;
                }

                // Gửi stream byte
                try (InputStream fileIn = new BufferedInputStream(Files.newInputStream(localPath))) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = fileIn.read(buffer)) != -1) {
                        rawOut.write(buffer, 0, read);
                    }
                    rawOut.flush();
                }

                // Nhận phản hồi kết quả
                String finalResult = lineReader.readLine();
                System.out.println("Kết quả truyền file: " + finalResult);
            }

        } catch (Exception e) {
            System.err.println("Lỗi truyền file: " + e.getMessage());
        }
    }

    public static String computeFileSha256(Path path) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream in = new BufferedInputStream(Files.newInputStream(path))) {
            byte[] buf = new byte[8192];
            int read;
            while ((read = in.read(buf)) != -1) {
                digest.update(buf, 0, read);
            }
        }
        return HexFormat.of().formatHex(digest.digest()).toLowerCase();
    }
}
