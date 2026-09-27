package bai01;

import java.util.Scanner;

public class DemoHinh {
    public static void main(String[] args) {
        System.out.println("========== DEMO HINH (BAI 1) ==========\n");

        System.out.println("1. KHOI TAO BANG CONSTRUCTOR KHONG THAM SO:");
        HCN hcn1 = new HCN();
        HVuong hv1 = new HVuong();
        HTG htg1 = new HTG();

        hcn1.xuatThongTin();
        hv1.xuatThongTin();
        htg1.xuatThongTin();

        System.out.println("\n2. KHOI TAO BANG CONSTRUCTOR DAY DU THAM SO:");
        HCN hcn2 = new HCN(7.5, 4.0);
        HVuong hv2 = new HVuong(6.5);
        HTG htg2 = new HTG(6.0, 8.0, 10.0);
        HTG htg3 = new HTG(5.0, 5.0, 5.0); // Tam giac deu
        HTG htg4 = new HTG(5.0, 5.0, 6.0); // Tam giac can

        hcn2.xuatThongTin();
        hv2.xuatThongTin();
        htg2.xuatThongTin();
        htg3.xuatThongTin();
        htg4.xuatThongTin();

        System.out.println("\n3. THAY DOI GIA TRI BANG PHUONG THUC SET VA KIEM TRA GET:");
        System.out.println("Truoc khi set: " + hcn2);
        hcn2.setChieuDai(10.0);
        hcn2.setChieuRong(5.0);
        System.out.printf("Sau khi set: Chieu dai = %.2f, Chieu rong = %.2f%n",
                hcn2.getChieuDai(), hcn2.getChieuRong());
        hcn2.xuatThongTin();

        System.out.println("\nTruoc khi set hinh vuong: " + hv2);
        hv2.setCanh(8.0);
        System.out.printf("Sau khi set: Canh = %.2f%n", hv2.getCanh());
        hv2.xuatThongTin();

        System.out.println("\n4. NHAP DU LIEU TU BAN PHIM (DEMO):");
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Ban co muon nhap hinh tuy chon tu ban phim? (y/n): ");
            if (scanner.hasNextLine()) {
                String choice = scanner.nextLine().trim();
                if (choice.equalsIgnoreCase("y")) {
                    System.out.println("\n--- Nhap HCN ---");
                    System.out.print("Nhap chieu dai: ");
                    double dai = Double.parseDouble(scanner.nextLine());
                    System.out.print("Nhap chieu rong: ");
                    double rong = Double.parseDouble(scanner.nextLine());
                    HCN userHcn = new HCN(dai, rong);
                    userHcn.xuatThongTin();

                    System.out.println("\n--- Nhap HVuong ---");
                    System.out.print("Nhap do dai canh: ");
                    double canh = Double.parseDouble(scanner.nextLine());
                    HVuong userHv = new HVuong(canh);
                    userHv.xuatThongTin();

                    System.out.println("\n--- Nhap HTG ---");
                    System.out.print("Nhap canh a: ");
                    double a = Double.parseDouble(scanner.nextLine());
                    System.out.print("Nhap canh b: ");
                    double b = Double.parseDouble(scanner.nextLine());
                    System.out.print("Nhap canh c: ");
                    double c = Double.parseDouble(scanner.nextLine());
                    HTG userHtg = new HTG(a, b, c);
                    userHtg.xuatThongTin();
                } else {
                    System.out.println("Bo qua buoc nhap tu ban phim.");
                }
            }
        } catch (Exception e) {
            System.out.println("Loi nhap lieu: " + e.getMessage());
        }

        System.out.println("\nHoan thanh demo Bai 1!");
    }
}
