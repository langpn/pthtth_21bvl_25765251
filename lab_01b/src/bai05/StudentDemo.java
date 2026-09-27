package bai05;

import bai04.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentDemo {

    public static void main(String[] args) {
        System.out.println("========== CHUONG TRINH QUAN LY SINH VIEN (BAI 5) ==========");
        Scanner sc = new Scanner(System.in);
        List<Student> students = new ArrayList<>();

        System.out.print("Ban co muon su dung du lieu mau de kiem tra nhanh? (y/n): ");
        String choice = "";
        if (sc.hasNextLine()) {
            choice = sc.nextLine().trim();
        }

        if (choice.equalsIgnoreCase("y")) {
            students = taoDanhSachMau();
            System.out.println("\n-> Da nap danh sach 4 sinh vien mau vao he thong!");
        } else {
            int n = 0;
            while (true) {
                System.out.print("Nhap so luong sinh vien n (n > 0): ");
                try {
                    String line = sc.nextLine().trim();
                    n = Integer.parseInt(line);
                    if (n > 0) break;
                    System.out.println("So luong sinh vien phai lon hon 0!");
                } catch (NumberFormatException e) {
                    System.out.println("Vui long nhap mot so nguyen hop le!");
                }
            }

            for (int i = 0; i < n; i++) {
                System.out.printf("%n=== NHAP THONG TIN SINH VIEN THU %d ===%n", i + 1);
                Student s = new Student();
                s.inputInfo(sc);
                students.add(s);
            }
        }

        if (students.isEmpty()) {
            System.out.println("Danh sach sinh vien rong!");
            return;
        }

        // 1. Hien thi tat ca sinh vien ra man hinh
        System.out.println("\n========================================================");
        System.out.println("1. DANH SACH TAT CA SINH VIEN:");
        for (int i = 0; i < students.size(); i++) {
            System.out.printf("[Sinh vien #%d]%n", i + 1);
            students.get(i).printInfo();
        }

        // 2. Tim SV co diem trung binh cao nhat va thap nhat
        double maxGpa = students.get(0).getGpa();
        double minGpa = students.get(0).getGpa();
        for (Student s : students) {
            if (s.getGpa() > maxGpa) maxGpa = s.getGpa();
            if (s.getGpa() < minGpa) minGpa = s.getGpa();
        }

        System.out.println("\n========================================================");
        System.out.printf("2. SINH VIEN CO DIEM TRUNG BINH CAO NHAT (Max GPA = %.2f):%n", maxGpa);
        for (Student s : students) {
            if (Double.compare(s.getGpa(), maxGpa) == 0) {
                s.printInfo();
            }
        }

        System.out.println("\n========================================================");
        System.out.printf("3. SINH VIEN CO DIEM TRUNG BINH THAP NHAT (Min GPA = %.2f):%n", minGpa);
        for (Student s : students) {
            if (Double.compare(s.getGpa(), minGpa) == 0) {
                s.printInfo();
            }
        }

        // 3. Hien thi tat ca SV duoc hoc bong (GPA > 8.0)
        System.out.println("\n========================================================");
        System.out.println("4. DANH SACH SINH VIEN DUOC HOC BONG (GPA > 8.0):");
        int hocBongCount = 0;
        for (Student s : students) {
            if (s.xetHocBong()) {
                s.printInfo();
                hocBongCount++;
            }
        }
        if (hocBongCount == 0) {
            System.out.println("Khong co sinh vien nao dat hoc bong (GPA > 8.0).");
        } else {
            System.out.printf("Tong cong: %d sinh vien dat hoc bong.%n", hocBongCount);
        }

        System.out.println("\nHoan thanh chuong trinh Bai 5!");
    }

    private static List<Student> taoDanhSachMau() {
        List<Student> list = new ArrayList<>();
        list.add(new Student("Nguyen Van An", "Nam", "15/05/2003", "123 Le Loi, Q1, TP.HCM",
                "SV001", "vanan@gmail.com", 8.8));
        list.add(new Student("Tran Thi Bich", "Nu", "20/11/2003", "456 Tran Hung Dao, Q5, TP.HCM",
                "SV002", "bich.tran@yahoo.com", 9.2));
        list.add(new Student("Le Van Cuong", "Nam", "02/02/2002", "789 Quang Trung, Go Vap, TP.HCM",
                "SV003", "cuong.le@iuh.edu.vn", 6.5));
        list.add(new Student("Pham Thi Dung", "Nu", "10/08/2003", "12 Nguyen Oanh, Go Vap, TP.HCM",
                "SV004", "dung.pham@gmail.com", 7.9));
        return list;
    }
}
