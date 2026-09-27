package bai02;

import java.util.Scanner;

public class DemoArray {
    public static void main(String[] args) {
        System.out.println("========== DEMO INT ARRAY (BAI 2) ==========\n");

        // 1. Khoi tao mang toi da 15 phan tu voi mot so gia tri ban dau
        int maxCapacity = 15;
        int[] initial = { 45, -12, 89, 7, -3, 24, 0, 7, 56 };
        IntArray arr = new IntArray(initial, maxCapacity);

        System.out.println("1. XUAT THONG TIN MANG BAN DAU:");
        arr.xuat("Mang ban dau");

        // 2. Tim kiem phan tu tren mang chua sap xep (Linear Search)
        System.out.println("\n2. TIM KIEM TREN MANG CHUA SAP XEP (LINEAR SEARCH):");
        int target1 = 7;
        int target2 = 999;
        int idx1 = arr.timKiemChuaSapXep(target1);
        int idx2 = arr.timKiemChuaSapXep(target2);
        System.out.printf("- Tim gia tri %d trong mang chua sap xep: %s%n",
                target1, (idx1 != -1 ? "Tim thay tai vi tri " + idx1 : "Khong tim thay"));
        System.out.printf("- Tim gia tri %d trong mang chua sap xep: %s%n",
                target2, (idx2 != -1 ? "Tim thay tai vi tri " + idx2 : "Khong tim thay"));

        // 3. Them phan tu Y vao dau, cuoi va giua (vi tri thu i)
        System.out.println("\n3. THAO TAC THEM PHAN TU:");
        System.out.println("- Them 100 vao dau mang:");
        arr.themDau(100);
        arr.xuat();

        System.out.println("- Them 200 vao cuoi mang:");
        arr.themCuoi(200);
        arr.xuat();

        int viTriChen = 3;
        System.out.printf("- Them 555 vao giua mang tai vi tri thu %d:%n", viTriChen);
        arr.themTaiViTri(555, viTriChen);
        arr.xuat();

        // 4. Xoa phan tu X trong mang
        System.out.println("\n4. THAO TAC XOA PHAN TU CO GIA TRI X:");
        int xoaX = 555;
        System.out.printf("- Xoa phan tu co gia tri %d:%n", xoaX);
        arr.xoaPhanTuX(xoaX);
        arr.xuat();

        // 5. Xoa phan tu thu j trong mang
        System.out.println("\n5. THAO TAC XOA PHAN TU TAI VI TRI THU J:");
        int viTriXoa = 0; // xoa dau mang
        System.out.printf("- Xoa phan tu tai vi tri j = %d:%n", viTriXoa);
        arr.xoaTaiViTri(viTriXoa);
        arr.xuat();

        // 6. Sap xep mang tang dan theo phuong phap Radix Sort
        System.out.println("\n6. SAP XEP RADIX SORT TANG DAN:");
        arr.sapXepRadix(true);
        arr.xuat("Mang sau khi Radix Sort TANG DAN");

        // 7. Tim kiem tren mang da sap xep tang dan (Binary Search)
        System.out.println("\n7. TIM KIEM TREN MANG DA SAP XEP TANG DAN (BINARY SEARCH):");
        int[] searchKeys = { 24, -12, 200, 123 };
        for (int key : searchKeys) {
            int foundIdx = arr.timKiemDaSapXep(key);
            System.out.printf("- Tim %d bang Binary Search: %s%n",
                    key, (foundIdx != -1 ? "Tim thay tai vi tri " + foundIdx : "Khong tim thay"));
        }

        // 8. Sap xep mang giam dan theo phuong phap Radix Sort
        System.out.println("\n8. SAP XEP RADIX SORT GIAM DAN:");
        arr.sapXepRadix(false);
        arr.xuat("Mang sau khi Radix Sort GIAM DAN");

        // 9. Tim kiem tren mang da sap xep giam dan (Binary Search)
        System.out.println("\n9. TIM KIEM TREN MANG DA SAP XEP GIAM DAN (BINARY SEARCH):");
        for (int key : searchKeys) {
            int foundIdx = arr.timKiemDaSapXep(key);
            System.out.printf("- Tim %d bang Binary Search tren mang giam dan: %s%n",
                    key, (foundIdx != -1 ? "Tim thay tai vi tri " + foundIdx : "Khong tim thay"));
        }

        // 10. Menu nhap tu ban phim (tuy chon)
        System.out.println("\n10. TUY CHON: NHAP MANG TU BAN PHIM DE THU NGHIEM:");
        Scanner sc = new Scanner(System.in);
        try {
            System.out.print("Ban co muon nhap mang moi tu ban phim? (y/n): ");
            if (sc.hasNextLine()) {
                String ans = sc.nextLine().trim();
                if (ans.equalsIgnoreCase("y")) {
                    System.out.print("Nhap dung luong toi da cua mang n: ");
                    int n = Integer.parseInt(sc.nextLine());
                    IntArray userArr = new IntArray(n);
                    userArr.nhap(sc);
                    userArr.xuat("Mang vua nhap");

                    System.out.println("Thuc hien Radix Sort tang dan tren mang vua nhap:");
                    userArr.sapXepRadix(true);
                    userArr.xuat("Sau khi sap xep tang dan");

                    System.out.print("Nhap gia tri B can tim kiem (Binary Search): ");
                    int b = sc.nextInt();
                    int res = userArr.timKiemDaSapXep(b);
                    System.out.printf("Ket qua tim %d: %s%n",
                            b, (res != -1 ? "Tim thay tai index " + res : "Khong tim thay"));
                } else {
                    System.out.println("Bo qua buoc nhap tu ban phim.");
                }
            }
        } catch (Exception e) {
            System.out.println("Ket thuc thao tac nhap tu ban phim (" + e.getMessage() + ").");
        }

        System.out.println("\nHoan thanh demo Bai 2!");
    }
}
