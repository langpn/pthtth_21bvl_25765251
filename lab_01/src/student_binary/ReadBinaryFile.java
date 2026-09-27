package student_binary;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ReadBinaryFile {

    public static ArrayList<SinhVien> loadSV(String src) throws IOException {
        File file = new File(src);
        if (!file.exists()) {
            System.err.println("Lỗi: Tệp nhị phân không tồn tại: " + src);
            return new ArrayList<>();
        }

        ArrayList<SinhVien> listSV = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                String mssv = dis.readUTF();
                String name = dis.readUTF();
                int age = dis.readInt();
                int sizemh = dis.readInt();
                ArrayList<MonHoc> listMH = new ArrayList<>();
                for (int j = 0; j < sizemh; j++) {
                    String tenMonHoc = dis.readUTF();
                    int tinChi = dis.readInt();
                    double diem = dis.readDouble();
                    MonHoc mh1 = new MonHoc(tenMonHoc, tinChi, diem);
                    listMH.add(mh1);
                }
                listSV.add(new SinhVien(mssv, name, age, listMH));
            }
        }

        System.out.println("=== DANH SÁCH SINH VIÊN ĐỌC TỪ FILE NHỊ PHÂN ===");
        for (SinhVien sv : listSV) {
            System.out.println(sv);
        }
        return listSV;
    }

    public static void main(String[] args) throws IOException {
        loadSV("data/sinhvien.bin");
    }
}
