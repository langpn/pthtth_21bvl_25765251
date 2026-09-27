package bai07;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PolygonDemo {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=================================================================");
        System.out.println("       CHUONG TRINH QUAN LY VA XU LY DA GIAC (BAI 7)             ");
        System.out.println("=================================================================");

        while (true) {
            System.out.println("\n---------------------------- MENU ----------------------------");
            System.out.println("1. Tao da giac bang KICH THUOC HINH HOC (Xuat chu vi & dien tich)");
            System.out.println("2. Tao da giac bang TOA DO CAC DINH (Xuat chu vi & dien tich)");
            System.out.println("3. Kiem tra xem 2 da giac nhap bang toa do co GIONG NHAU khong");
            System.out.println("4. Chay kiem thu tu dong (Automated Demo) tat ca cac loai hinh");
            System.out.println("0. Thoat chuong trinh");
            System.out.print("Moi ban chon (0-4): ");

            String line = "";
            if (sc.hasNextLine()) {
                line = sc.nextLine().trim();
            } else {
                break;
            }

            if (line.equals("0")) {
                System.out.println("Cam on ban da su dung chuong trinh! Tam biet.");
                break;
            }

            switch (line) {
                case "1":
                    menuTaoBangKichThuoc(sc);
                    break;
                case "2":
                    menuTaoBangToaDo(sc);
                    break;
                case "3":
                    menuSoSanhHaiDaGiac(sc);
                    break;
                case "4":
                    chayDemoTuDong();
                    break;
                default:
                    System.out.println("Lua chon khong hop le! Vui long chon lai.");
            }
        }
    }

    // 1. Tao da giac bang kich thuoc hinh hoc
    private static void menuTaoBangKichThuoc(Scanner sc) {
        System.out.println("\n--- CHON LOAI DA GIAC DE TAO THEO KICH THUOC ---");
        System.out.println("  1. Tam giac thuong (Triangle - 3 canh)");
        System.out.println("  2. Tam giac can (IsoscelesTriangle - canh ben, canh day)");
        System.out.println("  3. Tam giac deu (EquilateralTriangle - canh)");
        System.out.println("  4. Hinh chu nhat (Rectangle - dai, rong)");
        System.out.println("  5. Hinh vuong (Square - canh)");
        System.out.println("  6. Ngu giac deu (Regular Pentagon - canh)");
        System.out.println("  7. Luc giac deu (Regular Hexagon - canh)");
        System.out.println("  8. Bat giac deu (Regular Octagon - canh)");
        System.out.print("Chon loai hinh (1-8): ");

        try {
            int subChoice = Integer.parseInt(sc.nextLine().trim());
            Polygon p = null;
            switch (subChoice) {
                case 1:
                    System.out.print("Nhap do dai canh a: ");
                    double a = Double.parseDouble(sc.nextLine().trim());
                    System.out.print("Nhap do dai canh b: ");
                    double b = Double.parseDouble(sc.nextLine().trim());
                    System.out.print("Nhap do dai canh c: ");
                    double c = Double.parseDouble(sc.nextLine().trim());
                    p = new Triangle(a, b, c);
                    break;
                case 2:
                    System.out.print("Nhap do dai canh ben (leg): ");
                    double leg = Double.parseDouble(sc.nextLine().trim());
                    System.out.print("Nhap do dai canh day (base): ");
                    double base = Double.parseDouble(sc.nextLine().trim());
                    p = new IsoscelesTriangle(leg, base);
                    break;
                case 3:
                    System.out.print("Nhap do dai canh tam giac deu: ");
                    double sideTri = Double.parseDouble(sc.nextLine().trim());
                    p = new EquilateralTriangle(sideTri);
                    break;
                case 4:
                    System.out.print("Nhap chieu dai (width): ");
                    double w = Double.parseDouble(sc.nextLine().trim());
                    System.out.print("Nhap chieu rong (height): ");
                    double h = Double.parseDouble(sc.nextLine().trim());
                    p = new Rectangle(w, h);
                    break;
                case 5:
                    System.out.print("Nhap do dai canh hinh vuong: ");
                    double sideSq = Double.parseDouble(sc.nextLine().trim());
                    p = new Square(sideSq);
                    break;
                case 6:
                    System.out.print("Nhap do dai canh ngu giac deu: ");
                    double sidePent = Double.parseDouble(sc.nextLine().trim());
                    p = new Pentagon(sidePent);
                    break;
                case 7:
                    System.out.print("Nhap do dai canh luc giac deu: ");
                    double sideHex = Double.parseDouble(sc.nextLine().trim());
                    p = new Hexagon(sideHex);
                    break;
                case 8:
                    System.out.print("Nhap do dai canh bat giac deu: ");
                    double sideOct = Double.parseDouble(sc.nextLine().trim());
                    p = new Octagon(sideOct);
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
                    return;
            }

            System.out.println("\n=> KET QUA KHOI TAO THANH CONG:");
            System.out.println("Loai da giac : " + p.getPolygonType());
            System.out.printf("Chu vi       : %.4f%n", p.perimeter());
            System.out.printf("Dien tich    : %.4f%n", p.area());
            System.out.println("Cac dinh toa do: " + p.getVertices());
        } catch (Exception e) {
            System.out.println("Loi tao da giac: " + e.getMessage());
        }
    }

    // 2. Tao da giac bang toa do cac dinh
    private static void menuTaoBangToaDo(Scanner sc) {
        System.out.println("\n--- TAO DA GIAC BANG TOA DO CAC DINH ---");
        try {
            Polygon p = nhapDaGiacTuBanPhim(sc, "Nhap da giac");
            if (p != null) {
                System.out.println("\n=> THONG TIN DA GIAC VUA NHAP:");
                System.out.println("Loai da giac   : " + p.getPolygonType());
                System.out.println("Danh sach dinh : " + p.getVertices());
                System.out.printf("Chu vi         : %.4f%n", p.perimeter());
                System.out.printf("Dien tich      : %.4f%n", p.area());
            }
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    // 3. So sanh 2 da giac nhap bang toa do
    private static void menuSoSanhHaiDaGiac(Scanner sc) {
        System.out.println("\n--- SO SANH HAI DA GIAC NHAP BANG TOA DO DINH ---");
        try {
            System.out.println("[Buoc 1] Nhap da giac thu 1:");
            Polygon p1 = nhapDaGiacTuBanPhim(sc, "Da giac 1");

            System.out.println("\n[Buoc 2] Nhap da giac thu 2:");
            Polygon p2 = nhapDaGiacTuBanPhim(sc, "Da giac 2");

            System.out.println("\n---------------- KET QUA SO SANH ----------------");
            System.out.println("Da giac 1: " + p1);
            System.out.println("Da giac 2: " + p2);

            boolean sameVertices = Polygon.haveSameVertices(p1, p2);
            boolean congruent = Polygon.areCongruent(p1, p2);

            System.out.printf("- 1. Kiem tra trung khop toa do dinh (cung vi tri hinh hoc tren mat phang): %s%n",
                    (sameVertices ? "GIONG NHAU (Cung tap hop toa do dinh)" : "KHAC NHAU (Toa do dinh khong trung khop)"));

            System.out.printf("- 2. Kiem tra bang nhau / tuong dong hinh hoc (cung chu vi, dien tich va cac canh): %s%n",
                    (congruent ? "GIONG NHAU (Hai da giac bang nhau ve kich thuoc va hinh dang)" : "KHAC NHAU"));

            if (sameVertices) {
                System.out.println("=> KET LUAN: Hai da giac hoan toan GIONG NHAU (trung khop ca toa do tren mat phang)!");
            } else if (congruent) {
                System.out.println("=> KET LUAN: Hai da giac GIONG NHAU ve hinh hoc (bang nhau ve kich thuoc, chu vi va dien tich) nhung nam o vi tri khac nhau.");
            } else {
                System.out.println("=> KET LUAN: Hai da giac KHAC NHAU hoan toan!");
            }
        } catch (Exception e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    // Ham ho tro nhap toa do da giac tu ban phim
    public static Polygon nhapDaGiacTuBanPhim(Scanner sc, String tenGoi) {
        int n = 0;
        while (true) {
            System.out.printf("%s - Nhap so luong dinh (>= 3): ", tenGoi);
            try {
                n = Integer.parseInt(sc.nextLine().trim());
                if (n >= 3) break;
                System.out.println("Da giac phai co it nhat 3 dinh!");
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so nguyen hop le!");
            }
        }

        List<Point> vertices = new ArrayList<>();
        System.out.println("Nhap lan luot toa do (x, y) theo thu tu vong quanh da giac:");
        for (int i = 0; i < n; i++) {
            System.out.printf("  Dinh %d - Nhap x: ", i + 1);
            double x = Double.parseDouble(sc.nextLine().trim());
            System.out.printf("  Dinh %d - Nhap y: ", i + 1);
            double y = Double.parseDouble(sc.nextLine().trim());
            vertices.add(new Point(x, y));
        }

        return createPolygonFromVertices(vertices);
    }

    // Factory tao doi tuong cu the theo so luong dinh
    public static Polygon createPolygonFromVertices(List<Point> vertices) {
        int n = vertices.size();
        switch (n) {
            case 3:
                return new Triangle(vertices.get(0), vertices.get(1), vertices.get(2));
            case 4:
                return new Quadrilateral(vertices);
            case 5:
                return new Pentagon(vertices);
            case 6:
                return new Hexagon(vertices);
            case 8:
                return new Octagon(vertices);
            default:
                return new GenericPolygon(vertices);
        }
    }

    // 4. Kiem thu tu dong toan dien
    public static void chayDemoTuDong() {
        System.out.println("\n========== CHAY KIEM THU TU DONG (BAI 7) ==========\n");

        System.out.println("1. KHOI TAO CAC DA GIAC BANG KICH THUOC HINH HOC:");
        Polygon t1 = new Triangle(3.0, 4.0, 5.0);
        Polygon it1 = new IsoscelesTriangle(5.0, 6.0);
        Polygon et1 = new EquilateralTriangle(6.0);
        Polygon r1 = new Rectangle(8.0, 4.0);
        Polygon sq1 = new Square(5.0);
        Polygon p1 = new Pentagon(4.0);
        Polygon h1 = new Hexagon(4.0);
        Polygon o1 = new Octagon(4.0);

        List<Polygon> list = List.of(t1, it1, et1, r1, sq1, p1, h1, o1);
        for (Polygon p : list) {
            System.out.printf("- %-20s: Chu vi = %8.4f | Dien tich = %8.4f | So dinh = %d%n",
                    p.getPolygonType(), p.perimeter(), p.area(), p.getNumberOfVertices());
        }

        System.out.println("\n2. KIEM TRA TINH KE THUA OOP:");
        System.out.println("- EquilateralTriangle instanceof IsoscelesTriangle: " + (et1 instanceof IsoscelesTriangle));
        System.out.println("- IsoscelesTriangle instanceof Triangle:           " + (it1 instanceof Triangle));
        System.out.println("- Triangle instanceof Polygon:                     " + (t1 instanceof Polygon));
        System.out.println("- Square instanceof Rectangle:                     " + (sq1 instanceof Rectangle));
        System.out.println("- Rectangle instanceof Quadrilateral:              " + (r1 instanceof Quadrilateral));
        System.out.println("- Quadrilateral instanceof Polygon:                " + (r1 instanceof Polygon));

        System.out.println("\n3. KIEM TRA 2 DA GIAC NHAP BANG TOA DO CO GIONG NHAU KHONG:");

        // Test 3.1: Hai tam giac co cung toa do nhung nhap xoay vong va doi thu tu
        List<Point> vA = List.of(new Point(0, 0), new Point(4, 0), new Point(0, 3));
        List<Point> vB = List.of(new Point(4, 0), new Point(0, 3), new Point(0, 0)); // cyclic shift
        Polygon polyA = createPolygonFromVertices(vA);
        Polygon polyB = createPolygonFromVertices(vB);

        System.out.println("Test 3.1 - Hai tam giac cung toa do dinh (xoay vong dinh):");
        System.out.println("  Tam giac A: " + polyA.getVertices());
        System.out.println("  Tam giac B: " + polyB.getVertices());
        System.out.println("  -> haveSameVertices(A, B): " + Polygon.haveSameVertices(polyA, polyB) + " (EXPECTED: true)");
        System.out.println("  -> areCongruent(A, B):     " + Polygon.areCongruent(polyA, polyB) + " (EXPECTED: true)");

        // Test 3.2: Hai hinh chu nhat cung kich thuoc nhung toa do khac nhau (dich chuyen)
        List<Point> vR1 = List.of(new Point(0, 0), new Point(6, 0), new Point(6, 3), new Point(0, 3));
        List<Point> vR2 = List.of(new Point(10, 10), new Point(16, 10), new Point(16, 13), new Point(10, 13));
        Polygon rectA = createPolygonFromVertices(vR1);
        Polygon rectB = createPolygonFromVertices(vR2);

        System.out.println("\nTest 3.2 - Hai hinh chu nhat cung kich thuoc o 2 vi tri khac nhau:");
        System.out.println("  HCN A: " + rectA.getVertices());
        System.out.println("  HCN B: " + rectB.getVertices());
        System.out.println("  -> haveSameVertices(A, B): " + Polygon.haveSameVertices(rectA, rectB) + " (EXPECTED: false - khac vi tri)");
        System.out.println("  -> areCongruent(A, B):     " + Polygon.areCongruent(rectA, rectB) + " (EXPECTED: true - bang nhau ve hinh dang & kich thuoc)");

        // Test 3.3: Hai da giac hoan toan khac nhau
        List<Point> vDiff = List.of(new Point(0, 0), new Point(5, 0), new Point(2, 6));
        Polygon polyDiff = createPolygonFromVertices(vDiff);
        System.out.println("\nTest 3.3 - Hai da giac khac nhau hoan toan:");
        System.out.println("  -> haveSameVertices(A, Diff): " + Polygon.haveSameVertices(polyA, polyDiff) + " (EXPECTED: false)");
        System.out.println("  -> areCongruent(A, Diff):     " + Polygon.areCongruent(polyA, polyDiff) + " (EXPECTED: false)");

        System.out.println("\n=> Kiem thu tu dong Bai 7 da hoan thanh xuat sac!");
    }
}
