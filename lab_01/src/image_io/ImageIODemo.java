package image_io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImageIODemo {
    private static final Logger LOGGER = Logger.getLogger(ImageIODemo.class.getName());

    // Đọc file ảnh thành mảng byte
    public static byte[] readFile(File path) {
        if (!path.exists()) {
            System.err.println("Lỗi: Tệp ảnh nguồn không tồn tại: " + path.getAbsolutePath());
            return null;
        }

        try (FileInputStream fis = new FileInputStream(path);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buf = new byte[1024];
            int readNum;
            while ((readNum = fis.read(buf)) != -1) {
                bos.write(buf, 0, readNum);
            }
            byte[] bytes = bos.toByteArray();
            System.out.println("-> Đã đọc tệp ảnh thành mảng byte: " + bytes.length + " bytes.");
            return bytes;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi khi đọc file ảnh: " + ex.getMessage(), ex);
            return null;
        }
    }

    // Ghi mảng byte thành file ảnh mới
    public static boolean saveFile(File path, String formatName, byte[] bfile) {
        if (bfile == null || bfile.length == 0) {
            System.err.println("Lỗi: Dữ liệu mảng byte rỗng.");
            return false;
        }

        try {
            if (path.getParentFile() != null) {
                path.getParentFile().mkdirs();
            }
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bfile));
            if (img == null) {
                System.err.println("Lỗi: Không thể giải mã ảnh từ mảng byte.");
                return false;
            }
            boolean success = ImageIO.write(img, formatName, path);
            if (success) {
                System.out.println("-> Đã lưu ảnh thành công vào: " + path.getAbsolutePath() + " (Định dạng: " + formatName + ")");
                return true;
            } else {
                System.err.println("Lỗi: Không tìm thấy writer phù hợp cho định dạng: " + formatName);
                return false;
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi khi ghi file ảnh: " + ex.getMessage(), ex);
            return false;
        }
    }

    public static void main(String[] args) {
        File srcImage = new File("data/source.jpg");
        File destImage = new File("data/output_from_bytes.png");

        System.out.println("=== 1. ĐỌC FILE ẢNH SANG BYTE ARRAY ===");
        byte[] imageBytes = readFile(srcImage);

        System.out.println("\n=== 2. GHI MẢNG BYTE RA FILE ẢNH MỚI (PNG) ===");
        if (imageBytes != null) {
            saveFile(destImage, "png", imageBytes);
            System.out.println("Kiểm tra kích thước tệp ảnh mới: " + destImage.length() + " bytes.");
        }
    }
}
