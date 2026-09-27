package file;

import java.io.File;

public class FindFileDemo {

    public void findFile(String source, String key) {
        File file = new File(source);
        if (file.exists()) {
            if (file.isFile()) {
                if (file.getName().endsWith(key) || file.getName().contains(key)) {
                    System.out.println("-> Tìm thấy tệp: " + file.getAbsolutePath());
                }
            } else if (file.isDirectory()) {
                File[] listFile = file.listFiles();
                if (listFile != null) {
                    for (File f : listFile) {
                        findFile(f.getAbsolutePath(), key);
                    }
                }
            }
        } else {
            System.err.println("Đường dẫn không tồn tại: " + source);
        }
    }

    public static void main(String[] args) {
        FindFileDemo demo = new FindFileDemo();

        // Tạo cấu trúc thư mục thử nghiệm tìm kiếm
        try {
            File searchDir = new File("data/test_search");
            File sub1 = new File(searchDir, "docs");
            File sub2 = new File(searchDir, "images");
            sub1.mkdirs();
            sub2.mkdirs();

            new File(searchDir, "readme.txt").createNewFile();
            new File(sub1, "report.pdf").createNewFile();
            new File(sub1, "notes.txt").createNewFile();
            new File(sub2, "avatar.png").createNewFile();
            new File(sub2, "banner.jpg").createNewFile();

            System.out.println("=== TÌM KIẾM CÁC TỆP CÓ ĐUÔI '.txt' ===");
            demo.findFile("data/test_search", ".txt");

            System.out.println("\n=== TÌM KIẾM CÁC TỆP CÓ ĐUÔI '.png' ===");
            demo.findFile("data/test_search", ".png");

            // Dọn dẹp thư mục test
            new DeleteFolderDemo().deleteDirectoryRecursive("data/test_search");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
