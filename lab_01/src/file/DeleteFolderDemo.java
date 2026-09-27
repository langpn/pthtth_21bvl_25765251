package file;

import java.io.File;
import java.io.IOException;

public class DeleteFolderDemo {

    // TH1: Xóa thư mục rỗng
    public boolean deleteEmptyFolder(String source) {
        File folder = new File(source);
        if (folder.exists() && folder.isDirectory()) {
            boolean deleted = folder.delete();
            if (deleted) {
                System.out.println("[TH1] Thư mục rỗng tồn tại -> Xóa thư mục thành công: " + source);
                return true;
            } else {
                System.err.println("[TH1] Không thể xóa thư mục (có thể không rỗng): " + source);
                return false;
            }
        } else {
            System.out.println("[TH1] Thư mục không tồn tại: " + source);
            return false;
        }
    }

    // TH2: Xóa thư mục chỉ chứa các file đơn giản (không có thư mục con)
    public boolean deleteListFileInfolder(String source) {
        File folder = new File(source);
        if (folder.exists() && folder.isDirectory()) {
            File[] listFile = folder.listFiles();
            if (listFile != null && listFile.length != 0) {
                for (File f : listFile) {
                    if (f.isFile()) {
                        f.delete();
                    }
                }
            }
            boolean deleted = folder.delete();
            System.out.println("[TH2] Xóa thư mục chứa file thành công: " + source);
            return deleted;
        } else {
            System.out.println("[TH2] Thư mục không tồn tại: " + source);
            return false;
        }
    }

    // TH3: Xóa đệ quy thư mục chứa cây thư mục con và file lồng nhau
    public boolean deleteDirectoryRecursive(String source) {
        File folder = new File(source);
        if (folder.exists() && folder.isDirectory()) {
            File[] listFile = folder.listFiles();
            if (listFile != null && listFile.length != 0) {
                for (File f : listFile) {
                    if (f.isFile()) {
                        f.delete();
                    } else if (f.isDirectory()) {
                        deleteDirectoryRecursive(f.getAbsolutePath());
                    }
                }
            }
            boolean deleted = folder.delete();
            System.out.println("[TH3] Xóa đệ quy thư mục thành công: " + source);
            return deleted;
        } else {
            System.out.println("[TH3] Thư mục không tồn tại: " + source);
            return false;
        }
    }

    public static void main(String[] args) {
        DeleteFolderDemo demo = new DeleteFolderDemo();

        try {
            // Thử nghiệm TH1: Thư mục rỗng
            File emptyDir = new File("data/test_empty_dir");
            emptyDir.mkdirs();
            demo.deleteEmptyFolder(emptyDir.getPath());

            // Thử nghiệm TH2: Thư mục chỉ chứa file
            File filesDir = new File("data/test_files_dir");
            filesDir.mkdirs();
            new File(filesDir, "file1.txt").createNewFile();
            new File(filesDir, "file2.txt").createNewFile();
            demo.deleteListFileInfolder(filesDir.getPath());

            // Thử nghiệm TH3: Thư mục chứa thư mục con và file đệ quy
            File complexDir = new File("data/test_recursive_dir");
            File subDir = new File(complexDir, "sub_dir/deep_dir");
            subDir.mkdirs();
            new File(complexDir, "root.txt").createNewFile();
            new File(complexDir, "sub_dir/sub.txt").createNewFile();
            new File(subDir, "deep.txt").createNewFile();
            demo.deleteDirectoryRecursive(complexDir.getPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
