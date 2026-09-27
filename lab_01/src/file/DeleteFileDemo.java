package file;

import java.io.File;

public class DeleteFileDemo {
    public boolean deleteFile(String source) {
        File file = new File(source);
        if (file.exists()) {
            System.out.println("Tệp tồn tại: " + source);
            boolean result = file.delete();
            if (result) {
                System.out.println("-> Xóa tệp thành công.");
                return true;
            } else {
                System.err.println("-> Không thể xóa tệp.");
                return false;
            }
        } else {
            System.out.println("-> Tệp không tồn tại: " + source);
            return false;
        }
    }

    public static void main(String[] args) {
        DeleteFileDemo demo = new DeleteFileDemo();
        String testFile = "data/test_delete.txt";

        try {
            // Tạo tệp thử nghiệm
            File f = new File(testFile);
            f.getParentFile().mkdirs();
            f.createNewFile();
            System.out.println("Đã tạo tệp thử nghiệm: " + testFile);

            // Thực hiện xóa tệp
            demo.deleteFile(testFile);

            // Thử xóa lại khi tệp không còn tồn tại
            demo.deleteFile(testFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
