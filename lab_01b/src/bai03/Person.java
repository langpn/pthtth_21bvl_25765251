package bai03;

import java.util.Scanner;

public class Person {
    private String name;
    private String gender;
    private String birthDate;
    private String address;

    // Khoi tao khong tham so
    public Person() {
        this("", "", "", "");
    }

    // Khoi tao day du tham so
    public Person(String name, String gender, String birthDate, String address) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.address = address;
    }

    // Cac phuong thuc get / set
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Nhap thong tin tu ban phim
    public void inputInfo() {
        Scanner sc = new Scanner(System.in);
        inputInfo(sc);
    }

    // Phuong thuc ho tro truyen Scanner ben ngoai (tranh close System.in)
    public void inputInfo(Scanner sc) {
        System.out.print("Nhap ho ten: ");
        this.name = sc.nextLine().trim();

        System.out.print("Nhap gioi tinh (Nam/Nu/Khac): ");
        this.gender = sc.nextLine().trim();

        System.out.print("Nhap ngay sinh (dd/MM/yyyy): ");
        this.birthDate = sc.nextLine().trim();

        System.out.print("Nhap dia chi: ");
        this.address = sc.nextLine().trim();
    }

    // Hien thi tat ca thong tin Person
    public void printInfo() {
        System.out.println("----------------------------------------");
        System.out.printf("Ho ten     : %s%n", (name == null || name.isEmpty()) ? "(Chua co)" : name);
        System.out.printf("Gioi tinh  : %s%n", (gender == null || gender.isEmpty()) ? "(Chua co)" : gender);
        System.out.printf("Ngay sinh  : %s%n", (birthDate == null || birthDate.isEmpty()) ? "(Chua co)" : birthDate);
        System.out.printf("Dia chi    : %s%n", (address == null || address.isEmpty()) ? "(Chua co)" : address);
    }

    @Override
    public String toString() {
        return String.format("Person [name=%s, gender=%s, birthDate=%s, address=%s]",
                name, gender, birthDate, address);
    }
}
