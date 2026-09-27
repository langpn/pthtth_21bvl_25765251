package basic;

import java.util.Scanner;

public class EvenOddCheck {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(">> Kiểm tra số chẳn lẽ <<");
        System.out.print("Vui lòng nhập số cần kiểm tra: ");
        int so = scanner.nextInt();
        if (so % 2 == 0) {
            System.out.println("Số " + so + " là số chẵn.");
        } else {
            System.out.println("Số " + so + " là số lẻ.");
        }
    }
}
