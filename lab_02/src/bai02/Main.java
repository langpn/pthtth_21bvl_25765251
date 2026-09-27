package bai02;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Chương trình kiểm thử Bài 2: Quản lý người trong trường đại học – Tính kế thừa.
 * Yêu cầu:
 * 1. Tạo hai sinh viên.
 * 2. Tạo hai giảng viên.
 * 3. Hiển thị thông tin từng đối tượng (sử dụng đa hình và ghi đè phương thức).
 * 4. Hiển thị xếp loại sinh viên.
 * 5. Hiển thị lương giảng viên.
 */
public class Main {
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public static void main(String[] args) {
        System.out.println("==========================================================================================");
        System.out.println("     BÀI 2: QUẢN LÝ NGƯỜI TRONG TRƯỜNG ĐẠI HỌC - TÍNH KẾ THỪA (INHERITANCE)              ");
        System.out.println("==========================================================================================\n");

        // 1. Tạo hai sinh viên
        SinhVien sv1 = new SinhVien(
                "Nguyễn Văn An",
                2004,
                "12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, TP.HCM",
                "21000101",
                "Kỹ thuật phần mềm",
                8.8
        );

        SinhVien sv2 = new SinhVien(
                "Trần Thị Bích",
                2005,
                "45 Lê Duẩn, Bến Nghé, Quận 1, TP.HCM",
                "21000202",
                "Hệ thống thông tin",
                7.4
        );

        // 2. Tạo hai giảng viên
        GiangVien gv1 = new GiangVien(
                "TS. Lê Hoàng Nam",
                1982,
                "78 Quang Trung, Phường 10, Gò Vấp, TP.HCM",
                "GV001",
                "Khoa học máy tính & Trí tuệ nhân tạo",
                15_000_000.0,
                3.5
        );

        GiangVien gv2 = new GiangVien(
                "ThS. Phạm Thu Trang",
                1990,
                "102 Điện Biên Phủ, Phường 15, Bình Thạnh, TP.HCM",
                "GV002",
                "Công nghệ phần mềm & Hệ thống phân tán",
                12_000_000.0,
                2.8
        );

        // Danh sách lưu trữ tổng quát (thể hiện tính đa hình Polymorphism của OOP)
        List<Nguoi> danhSach = new ArrayList<>();
        danhSach.add(sv1);
        danhSach.add(sv2);
        danhSach.add(gv1);
        danhSach.add(gv2);

        // 3. Hiển thị thông tin từng đối tượng
        System.out.println("============================== HIỂN THỊ THÔNG TIN CHI TIẾT ==============================");
        for (int i = 0; i < danhSach.size(); i++) {
            Nguoi nguoi = danhSach.get(i);
            String loaiDoiTuong = (nguoi instanceof SinhVien) ? "SINH VIÊN" : "GIẢNG VIÊN";
            System.out.println("\n+----------------------------------------------------------------------------------------+");
            System.out.printf("| [%d] %-81s |%n", (i + 1), loaiDoiTuong);
            System.out.println("+----------------------------------------------------------------------------------------+");
            nguoi.hienThiThongTin();
            System.out.println("+----------------------------------------------------------------------------------------+");
        }

        // 4. Hiển thị xếp loại sinh viên
        System.out.println("\n\n================================ BẢNG XẾP LOẠI SINH VIÊN ================================");
        System.out.println("+------------+----------------------+--------------------+----------+------------+");
        System.out.printf("| %-10s | %-20s | %-18s | %-8s | %-10s |%n",
                "Mã SV", "Họ và tên", "Ngành học", "Điểm TB", "Xếp loại");
        System.out.println("+------------+----------------------+--------------------+----------+------------+");

        List<SinhVien> dsSinhVien = List.of(sv1, sv2);
        for (SinhVien sv : dsSinhVien) {
            System.out.printf("| %-10s | %-20s | %-18s | %-8.2f | %-10s |%n",
                    sv.getMaSinhVien(), sv.getHoTen(), sv.getNganhHoc(), sv.getDiemTrungBinh(), sv.xepLoai());
        }
        System.out.println("+------------+----------------------+--------------------+----------+------------+");

        // 5. Hiển thị lương giảng viên
        System.out.println("\n\n================================ BẢNG LƯƠNG GIẢNG VIÊN =================================");
        System.out.println("+------------+----------------------+----------------+-------+--------------------+");
        System.out.printf("| %-10s | %-20s | %-14s | %-5s | %-18s |%n",
                "Mã GV", "Họ và tên", "Lương cơ bản", "Hệ số", "Lương thực nhận");
        System.out.println("+------------+----------------------+----------------+-------+--------------------+");

        List<GiangVien> dsGiangVien = List.of(gv1, gv2);
        for (GiangVien gv : dsGiangVien) {
            System.out.printf("| %-10s | %-20s | %-14s | %-5.2f | %-18s |%n",
                    gv.getMaGiangVien(), gv.getHoTen(), CURRENCY_FORMAT.format(gv.getLuongCoBan()),
                    gv.getHeSoLuong(), CURRENCY_FORMAT.format(gv.tinhLuong()));
        }
        System.out.println("+------------+----------------------+----------------+-------+--------------------+");

        System.out.println("\n==========================================================================================");
        System.out.println("                 HOÀN THÀNH KIỂM THỬ BÀI 2 - QUẢN LÝ NGƯỜI                                ");
        System.out.println("==========================================================================================");
    }
}
