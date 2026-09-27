package bai01;

/**
 * Chương trình kiểm thử lớp SanPham - Bài 1: Quản lý sản phẩm – Tính đóng gói.
 * Các kịch bản kiểm thử:
 * 1. Tạo ít nhất hai sản phẩm.
 * 2. Nhập thêm hàng cho một sản phẩm (hiển thị trước và sau).
 * 3. Thử bán hàng thành công (hiển thị trước và sau).
 * 4. Thử bán số lượng lớn hơn tồn kho (hiển thị trước và sau).
 * 5. Kiểm thử các trường hợp dữ liệu không hợp lệ (số lượng nhập/bán <= 0).
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println("            BÀI 1: QUẢN LÝ SẢN PHẨM - TÍNH ĐÓNG GÓI (ENCAPSULATION)           ");
        System.out.println("===============================================================================\n");

        // 1. Tạo ít nhất hai sản phẩm
        System.out.println("--- BƯỚC 1: KHỞI TẠO CÁC ĐỐI TƯỢNG SẢN PHẨM ---");
        SanPham sp1 = new SanPham("SP001", "Laptop ASUS Zenbook 14 OLED", 24_500_000.0, 10);
        SanPham sp2 = new SanPham("SP002", "Bàn phím cơ không dây Logitech MX", 2_890_000.0, 20);

        System.out.println("\nThông tin sản phẩm 1 ban đầu:");
        sp1.hienThiThongTin();

        System.out.println("\nThông tin sản phẩm 2 ban đầu:");
        sp2.hienThiThongTin();

        // 2. Nhập thêm hàng cho một sản phẩm (SP001)
        System.out.println("\n--- BƯỚC 2: NHẬP THÊM HÀNG CHO SẢN PHẨM 1 (SP001) ---");
        System.out.println(">>> Thông tin SP001 TRƯỚC KHI nhập hàng:");
        sp1.hienThiThongTin();

        int soLuongNhap = 5;
        System.out.printf("Thực hiện nhập thêm %d sản phẩm cho SP001...%n", soLuongNhap);
        sp1.nhapHang(soLuongNhap);

        System.out.println("\n>>> Thông tin SP001 SAU KHI nhập hàng:");
        sp1.hienThiThongTin();

        // Thử nhập số lượng không hợp lệ (kiểm soát đóng gói)
        System.out.println("\nThử nhập số lượng không hợp lệ (-3):");
        sp1.nhapHang(-3);

        // 3. Thử bán hàng thành công
        System.out.println("\n--- BƯỚC 3: THỬ BÁN HÀNG THÀNH CÔNG (SP001) ---");
        System.out.println(">>> Thông tin SP001 TRƯỚC KHI bán:");
        sp1.hienThiThongTin();

        int soLuongBanHopLe = 4;
        System.out.printf("Thực hiện bán %d sản phẩm cho SP001...%n", soLuongBanHopLe);
        boolean ketQuaBan1 = sp1.banHang(soLuongBanHopLe);
        System.out.println("Kết quả trả về từ banHang(): " + ketQuaBan1);

        System.out.println("\n>>> Thông tin SP001 SAU KHI bán thành công:");
        sp1.hienThiThongTin();

        // 4. Thử bán số lượng lớn hơn tồn kho
        System.out.println("\n--- BƯỚC 4: THỬ BÁN SỐ LƯỢNG LỚN HƠN TỒN KHO (SP001) ---");
        System.out.println(">>> Thông tin SP001 TRƯỚC KHI bán vượt tồn kho:");
        sp1.hienThiThongTin();

        int soLuongBanVuotTon = 50; // Hiện tồn kho chỉ còn 11
        System.out.printf("Thực hiện bán %d sản phẩm (tồn kho hiện tại: %d)...%n",
                soLuongBanVuotTon, sp1.getSoLuong());
        boolean ketQuaBan2 = sp1.banHang(soLuongBanVuotTon);
        System.out.println("Kết quả trả về từ banHang(): " + ketQuaBan2);

        System.out.println("\n>>> Thông tin SP001 SAU KHI bán thất bại (số lượng được bảo toàn):");
        sp1.hienThiThongTin();

        // Thử bán số lượng âm hoặc 0
        System.out.println("\nThử bán số lượng <= 0 (bán 0):");
        sp1.banHang(0);

        // 5. Thao tác bổ sung trên sản phẩm 2 (SP002)
        System.out.println("\n--- BƯỚC 5: THAO TÁC BÁN HÀNG TRÊN SẢN PHẨM 2 (SP002) ---");
        System.out.println(">>> Thông tin SP002 TRƯỚC KHI bán:");
        sp2.hienThiThongTin();

        System.out.println("Bán 10 chiếc bàn phím SP002:");
        sp2.banHang(10);

        System.out.println("\n>>> Thông tin SP002 SAU KHI bán:");
        sp2.hienThiThongTin();

        System.out.println("\n===============================================================================");
        System.out.println("                 HOÀN THÀNH KIỂM THỬ BÀI 1 - QUẢN LÝ SẢN PHẨM                 ");
        System.out.println("===============================================================================");
    }
}
