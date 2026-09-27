package bai06;

public class DemoABC {
    public static void main(String[] args) {
        System.out.println("========== MINH HOA TRUY XUAT BIEN ANCESTOR (BAI 6) ==========\n");

        System.out.println("Giai thich ly thuyet trong Java:");
        System.out.println("1. Trong Java, bien the hien (field) KHONG co tinh da hinh (non-polymorphic).");
        System.out.println("   Khi lop con khai bao bien cung ten voi lop cha, bien do goi la 'shadowed' (bi che khuat).");
        System.out.println("2. Java khong cho phep cu phap 'super.super.x'.");
        System.out.println("3. Giai phap chinh xac nhat trong Java de mot phuong thuc trong C truy cap va dat x cua A la:");
        System.out.println("   -> Ep kieu 'this' ve kieu lop to tien A: ((A) this).x = giaTriMoi;");
        System.out.println("   Viec truy cap bien duoc phan giai o thoi diem compile-time theo kieu tham chieu,");
        System.out.println("   do do chi sua dung bien x cua A ma khong he anh huong den B.x hay C.x.\n");

        // Khoi tao the hien cua lop C voi gia tri rieng biet cho tung lop:
        // A.x = 10, B.x = 20, C.x = 30
        C objC = new C(10, 20, 30);
        objC.printAllX("Trang thai ban dau cua doi tuong C");

        // Thu nghiem 1: Dat x cua A bang Casting ((A) this).x
        System.out.println("[Thao tac 1]: Goi objC.setXA_ByCasting(999)...");
        objC.setXA_ByCasting(999);
        objC.printAllX("Sau khi goi setXA_ByCasting(999)");

        // Kiem tra tinh dung dan:
        boolean test1Pass = (objC.getXA_ByCasting() == 999) && (objC.getXB() == 20) && (objC.getXC() == 30);
        System.out.println("Ket qua Kiem tra 1: " + (test1Pass ? "THANH CONG (Chi co A.x thay doi!)" : "THAT BAI!"));

        // Thu nghiem 2: Dat x cua A bang Reflection API
        System.out.println("\n[Thao tac 2]: Goi objC.setXA_ByReflection(555)...");
        objC.setXA_ByReflection(555);
        objC.printAllX("Sau khi goi setXA_ByReflection(555)");

        boolean test2Pass = (objC.getXA_ByReflection() == 555) && (objC.getXB() == 20) && (objC.getXC() == 30);
        System.out.println("Ket qua Kiem tra 2: " + (test2Pass ? "THANH CONG (Chi co A.x thay doi!)" : "THAT BAI!"));

        // Thu nghiem 3: Thay doi x cua B va C de chung minh su doc lap hoan toan
        System.out.println("\n[Thao tac 3]: Thay doi x cua B thanh 77 va x cua C thanh 88...");
        objC.setXB(77);
        objC.setXC(88);
        objC.printAllX("Sau khi thay doi B.x va C.x");

        System.out.println("Xac nhan cuoi cung:");
        System.out.printf("  A.x = %d (van giu nguyen 555)%n", objC.getXA_ByCasting());
        System.out.printf("  B.x = %d (da cap nhat thanh 77)%n", objC.getXB());
        System.out.printf("  C.x = %d (da cap nhat thanh 88)%n", objC.getXC());

        System.out.println("\nHoan thanh demo Bai 6!");
    }
}
