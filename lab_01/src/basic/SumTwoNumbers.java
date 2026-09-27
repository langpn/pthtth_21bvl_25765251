package basic;

import java.util.Scanner;

public class SumTwoNumbers {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Vui lòng nhập số hạng thứ nhất: ");
        int soA = scanner.nextInt();
        System.out.print("Vui lòng nhập số hạng thứ hai: ");
        int soB = scanner.nextInt();
        int kq = soA + soB;
        System.out.println("Tính tổng [" + soA + " + " + soB + "] = " + kq);
    }
}
