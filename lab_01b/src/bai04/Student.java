package bai04;

import bai03.Person;

import java.util.Scanner;

public class Student extends Person {
    private String studentId;
    private String email;
    private double gpa;

    // Khoi tao khong tham so
    public Student() {
        super();
        this.studentId = "";
        this.email = "";
        this.gpa = 0.0;
    }

    // Khoi tao day du tham so
    public Student(String name, String gender, String birthDate, String address,
                   String studentId, String email, double gpa) {
        super(name, gender, birthDate, address);
        setStudentId(studentId);
        setEmail(email);
        setGpa(gpa);
    }

    // Kiem tra tinh hop le cua email
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String trimmed = email.trim();
        return trimmed.contains("@") && !trimmed.contains(" ") && !trimmed.isEmpty();
    }

    // Kiem tra tinh hop le cua GPA
    public static boolean isValidGpa(double gpa) {
        return gpa >= 0.0 && gpa <= 10.0;
    }

    // Getters va Setters co validation
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId == null ? "" : studentId.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email phai chua ky tu '@' va khong ton tai khoang trang!");
        }
        this.email = email.trim();
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        if (!isValidGpa(gpa)) {
            throw new IllegalArgumentException("Diem trung binh phai trong khoang tu 0.0 den 10.0!");
        }
        this.gpa = gpa;
    }

    // Phuong thuc xet xem Student co duoc hoc bong khong?
    // Diem trung binh tren 8.0 duoc hoc bong
    public boolean xetHocBong() {
        return this.gpa > 8.0;
    }

    // Override phuong thuc inputInfo()
    @Override
    public void inputInfo() {
        Scanner sc = new Scanner(System.in);
        inputInfo(sc);
    }

    @Override
    public void inputInfo(Scanner sc) {
        // Goi nhap thong tin co ban tu lop cha Person
        super.inputInfo(sc);

        System.out.print("Nhap ma so sinh vien (MSSV): ");
        this.studentId = sc.nextLine().trim();

        // Nhap email voi kiem tra hop le
        while (true) {
            System.out.print("Nhap email (chua '@' va khong khoang trang): ");
            String inputEmail = sc.nextLine().trim();
            if (isValidEmail(inputEmail)) {
                this.email = inputEmail;
                break;
            } else {
                System.out.println("Loi: Email khong hop le! Vui long nhap lai.");
            }
        }

        // Nhap diem trung binh voi kiem tra hop le
        while (true) {
            System.out.print("Nhap diem trung binh (0.0 - 10.0): ");
            String inputGpaStr = sc.nextLine().trim();
            try {
                double inputGpa = Double.parseDouble(inputGpaStr);
                if (isValidGpa(inputGpa)) {
                    this.gpa = inputGpa;
                    break;
                } else {
                    System.out.println("Loi: Diem trung binh phai tu 0.0 den 10.0!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Loi: Diem trung binh phai la so thuc!");
            }
        }
    }

    // Override phuong thuc printInfo()
    @Override
    public void printInfo() {
        super.printInfo();
        System.out.printf("MSSV       : %s%n", (studentId == null || studentId.isEmpty()) ? "(Chua co)" : studentId);
        System.out.printf("Email      : %s%n", (email == null || email.isEmpty()) ? "(Chua co)" : email);
        System.out.printf("Diem TB    : %.2f%n", gpa);
        System.out.printf("Hoc bong   : %s%n", (xetHocBong() ? "DUOC HOC BONG (GPA > 8.0)" : "Khong duoc"));
        System.out.println("----------------------------------------");
    }

    @Override
    public String toString() {
        return String.format("Student [MSSV=%s, Ten=%s, GioiTinh=%s, NgaySinh=%s, DiaChi=%s, Email=%s, GPA=%.2f, HocBong=%s]",
                studentId, getName(), getGender(), getBirthDate(), getAddress(), email, gpa, (xetHocBong() ? "Co" : "Khong"));
    }
}
