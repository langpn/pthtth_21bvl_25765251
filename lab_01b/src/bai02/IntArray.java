package bai02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class IntArray {
    private int[] a;
    private int size;
    private final int capacity;

    // Khoi tao mang voi kich thuoc toi da la capacity
    public IntArray(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Kich thuoc toi da phai lon hon 0.");
        }
        this.capacity = capacity;
        this.a = new int[capacity];
        this.size = 0;
    }

    // Khoi tao tu du lieu co san
    public IntArray(int[] initialData, int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Kich thuoc toi da phai lon hon 0.");
        }
        if (initialData != null && initialData.length > capacity) {
            throw new IllegalArgumentException("Du lieu ban dau vuot qua dung luong toi da.");
        }
        this.capacity = capacity;
        this.a = new int[capacity];
        if (initialData != null) {
            System.arraycopy(initialData, 0, this.a, 0, initialData.length);
            this.size = initialData.length;
        } else {
            this.size = 0;
        }
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Chi so khong hop le: " + index);
        }
        return a[index];
    }

    // Nhap gia tri cho mang tu ban phim
    public void nhap(Scanner sc) {
        System.out.printf("Nhap so luong phan tu muon nhap (toi da %d): ", capacity);
        int n = sc.nextInt();
        while (n < 0 || n > capacity) {
            System.out.printf("So luong khong hop le! Vui long nhap lai (0 <= n <= %d): ", capacity);
            n = sc.nextInt();
        }
        this.size = n;
        for (int i = 0; i < n; i++) {
            System.out.printf("a[%d] = ", i);
            a[i] = sc.nextInt();
        }
    }

    // Xuat thong tin cua mang
    public void xuat() {
        xuat("Mang hien tai");
    }

    public void xuat(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(message).append(" [so phan tu: ").append(size).append("/").append(capacity).append("]: [");
        for (int i = 0; i < size; i++) {
            sb.append(a[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    // Them mot phan tu Y vao dau mang
    public boolean themDau(int y) {
        return themTaiViTri(y, 0);
    }

    // Them mot phan tu Y vao cuoi mang
    public boolean themCuoi(int y) {
        return themTaiViTri(y, size);
    }

    // Them mot phan tu Y vao vi tri thu i cua mang (0 <= i <= size)
    public boolean themTaiViTri(int y, int i) {
        if (size >= capacity) {
            System.out.println("Loi: Mang da day, khong the them phan tu moi!");
            return false;
        }
        if (i < 0 || i > size) {
            System.out.printf("Loi: Vi tri chen %d khong hop le (0 <= i <= %d)!%n", i, size);
            return false;
        }

        // Dich cac phan tu tu vi tri i sang phai
        for (int k = size; k > i; k--) {
            a[k] = a[k - 1];
        }
        a[i] = y;
        size++;
        return true;
    }

    // Xoa phan tu dau tien co gia tri X trong mang
    public boolean xoaPhanTuX(int x) {
        int index = timKiemChuaSapXep(x);
        if (index == -1) {
            System.out.printf("Khong tim thay phan tu %d de xoa!%n", x);
            return false;
        }
        return xoaTaiViTri(index);
    }

    // Xoa tat ca cac phan tu co gia tri X trong mang
    public int xoaTatCaX(int x) {
        int count = 0;
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (a[i] == x) {
                count++;
            } else {
                a[newSize++] = a[i];
            }
        }
        this.size = newSize;
        return count;
    }

    // Xoa phan tu thu j trong mang (0 <= j < size)
    public boolean xoaTaiViTri(int j) {
        if (size == 0) {
            System.out.println("Loi: Mang dang rong, khong the xoa!");
            return false;
        }
        if (j < 0 || j >= size) {
            System.out.printf("Loi: Vi tri xoa %d khong hop le (0 <= j < %d)!%n", j, size);
            return false;
        }

        // Dich cac phan tu tu vi tri j+1 sang trai
        for (int k = j; k < size - 1; k++) {
            a[k] = a[k + 1];
        }
        size--;
        return true;
    }

    // Tim kiem phan tu B trong mang chua sap xep (Linear Search)
    // Tra ve chi so dau tien tim thay hoac -1
    public int timKiemChuaSapXep(int b) {
        for (int i = 0; i < size; i++) {
            if (a[i] == b) {
                return i;
            }
        }
        return -1;
    }

    // Kiem tra xem mang da sap xep theo huong nao
    // Tra ve 1 neu tang dan, -1 neu giam dan, 0 neu chua sap xep hoac tat ca phan tu bang nhau
    public int huongSapXep() {
        if (size <= 1) return 1;
        boolean tang = true;
        boolean giam = true;
        for (int i = 0; i < size - 1; i++) {
            if (a[i] > a[i + 1]) tang = false;
            if (a[i] < a[i + 1]) giam = false;
        }
        if (tang) return 1;
        if (giam) return -1;
        return 0;
    }

    // Tim kiem mot phan tu B trong mang da sap xep (Binary Search)
    // Tra ve vi tri tim thay hoac -1
    public int timKiemDaSapXep(int b) {
        int direction = huongSapXep();
        if (direction == 0) {
            // Neu chua sap xep thi canh bao nhung van co the dung binary search
            System.out.println("Canh bao: Mang chua duoc sap xep, ket qua Binary Search co the khong chinh xac!");
        }

        int low = 0;
        int high = size - 1;

        if (direction == -1) {
            // Mang sap xep giam dan
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (a[mid] == b) {
                    return mid;
                }
                if (b < a[mid]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        } else {
            // Mang sap xep tang dan (hoac mac dinh)
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (a[mid] == b) {
                    return mid;
                }
                if (b > a[mid]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return -1;
    }

    // Sap xep mang tang dan hoac giam dan theo phuong phap Radix Sort
    // Ho tro ca so am va so duong
    public void sapXepRadix(boolean tangDan) {
        if (size <= 1) return;

        // Phan loai so am va so khong am de xu ly Radix Sort
        List<Long> negatives = new ArrayList<>();
        List<Long> nonNegatives = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (a[i] < 0) {
                negatives.add((long) -a[i]); // luu tri tuyet doi duoi dang long
            } else {
                nonNegatives.add((long) a[i]);
            }
        }

        // Radix sort cho tap khong am
        long[] posArr = nonNegatives.stream().mapToLong(Long::longValue).toArray();
        radixSortLsd(posArr);

        // Radix sort cho tap am (theo tri tuyet doi)
        long[] negArr = negatives.stream().mapToLong(Long::longValue).toArray();
        radixSortLsd(negArr);

        int index = 0;
        if (tangDan) {
            // Tang dan:
            // So am: tri tuyet doi cang lon thi gia tri cang nho -> duyet negArr tu cuoi ve dau, doi lai dau am
            for (int i = negArr.length - 1; i >= 0; i--) {
                a[index++] = (int) -negArr[i];
            }
            // So khong am: duyet tu dau den cuoi tang dan
            for (int i = 0; i < posArr.length; i++) {
                a[index++] = (int) posArr[i];
            }
        } else {
            // Giam dan:
            // So khong am: tu lon den be (duyet tu cuoi ve dau cua posArr)
            for (int i = posArr.length - 1; i >= 0; i--) {
                a[index++] = (int) posArr[i];
            }
            // So am: tri tuyet doi cang nho thi gia tri cang lon -> duyet negArr tu dau den cuoi
            for (int i = 0; i < negArr.length; i++) {
                a[index++] = (int) -negArr[i];
            }
        }
    }

    // Radix sort LSD (Least Significant Digit) cho mang so khong am
    private void radixSortLsd(long[] arr) {
        if (arr.length <= 1) return;

        long max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }

        for (long exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(arr, exp);
        }
    }

    // Counting sort dua tren chu so tai hang exp
    private void countingSortByDigit(long[] arr, long exp) {
        int n = arr.length;
        long[] output = new long[n];
        int[] count = new int[10];

        // Dem tan suat chu so
        for (int i = 0; i < n; i++) {
            int digit = (int) ((arr[i] / exp) % 10);
            count[digit]++;
        }

        // Tinh vi tri tich luy
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        // Xay dung mang ket qua on dinh tu cuoi ve dau
        for (int i = n - 1; i >= 0; i--) {
            int digit = (int) ((arr[i] / exp) % 10);
            output[count[digit] - 1] = arr[i];
            count[digit]--;
        }

        // Sao chep tro lai mang arr
        System.arraycopy(output, 0, arr, 0, n);
    }

    public int[] toArray() {
        return Arrays.copyOf(a, size);
    }
}
