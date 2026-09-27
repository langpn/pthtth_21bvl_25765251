package student_binary;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class WriteBinaryFile {

    public static void saveSV(String src, ArrayList<SinhVien> listSV) throws IOException {
        File file = new File(src);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            dos.writeInt(listSV.size());
            for (SinhVien sv : listSV) {
                dos.writeUTF(sv.getMssv());
                dos.writeUTF(sv.getTen());
                dos.writeInt(sv.getTuoi());
                dos.writeInt(sv.getListMH().size());
                for (MonHoc mh : sv.getListMH()) {
                    dos.writeUTF(mh.getTenMonHoc());
                    dos.writeInt(mh.getTinChi());
                    dos.writeDouble(mh.getDiem());
                }
            }
            dos.flush();
        }
        System.out.println("-> Đã ghi thành công " + listSV.size() + " sinh viên vào tệp nhị phân: " + src);
    }

    public static void main(String[] args) throws IOException {
        MonHoc mh = new MonHoc("ltcb", 3, 6.7);
        MonHoc mh1 = new MonHoc("ltw", 3, 6.7);
        MonHoc mh2 = new MonHoc("tkhdt", 3, 6.7);

        ArrayList<MonHoc> listMH = new ArrayList<>();
        listMH.add(mh2);
        listMH.add(mh1);
        listMH.add(mh);

        ArrayList<SinhVien> listSV = new ArrayList<>();
        SinhVien sv = new SinhVien("25765251", "Pham Ngoc Lang", 21, listMH);
        SinhVien sv1 = new SinhVien("11329078", "Nguyen Van B", 23, listMH);
        listSV.add(sv);
        listSV.add(sv1);

        saveSV("data/sinhvien.bin", listSV);
    }
}
