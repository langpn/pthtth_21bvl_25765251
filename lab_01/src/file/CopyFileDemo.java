package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFileDemo {

    public boolean copyFile(String source, String dest) {
        File sourceFile = new File(source);
        File destFile = new File(dest);

        if (!sourceFile.exists()) {
            System.err.println("Lỗi: Tệp nguồn không tồn tại -> " + source);
            return false;
        }

        if (destFile.getParentFile() != null) {
            destFile.getParentFile().mkdirs();
        }

        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destFile)) {

            byte[] arr = new byte[1024];
            int readNum;
            long totalBytes = 0;

            while ((readNum = fis.read(arr)) != -1) {
                fos.write(arr, 0, readNum);
                fos.flush();
                totalBytes += readNum;
            }

            System.out.println("-> Sao chép thành công [" + source + " -> " + dest + "] (" + totalBytes + " bytes)");
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("Lỗi không tìm thấy tệp: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.err.println("Lỗi I/O khi sao chép tệp: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        CopyFileDemo demo = new CopyFileDemo();

        try {
            // Tạo tệp thử nghiệm a.txt
            File src = new File("data/test_copy_a.txt");
            src.getParentFile().mkdirs();
            try (FileOutputStream fos = new FileOutputStream(src)) {
                fos.write("Nội dung thử nghiệm sao chép tệp tin bằng Java I/O Streams.".getBytes());
            }

            // Thực hiện sao chép a.txt sang b.txt
            System.out.println("=== THỬ NGHIỆM 1: SAO CHÉP TỆP HỢP LỆ ===");
            demo.copyFile("data/test_copy_a.txt", "data/test_copy_b.txt");

            // Thử nghiệm khi tệp nguồn không tồn tại
            System.out.println("\n=== THỬ NGHIỆM 2: TỆP NGUỒN KHÔNG TỒN TẠI ===");
            demo.copyFile("data/file_khong_ton_tai.txt", "data/dest.txt");

            // Dọn dẹp
            src.delete();
            new File("data/test_copy_b.txt").delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
