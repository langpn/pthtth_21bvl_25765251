package bai06;

import java.lang.reflect.Field;

public class C extends B {
    // Truong x rieng cua lop C (shadows field x cua B va A)
    protected int x;

    public C() {
        super(10, 20);
        this.x = 30;
    }

    public C(int xA, int xB, int xC) {
        super(xA, xB);
        this.x = xC;
    }

    public int getXC() {
        return this.x;
    }

    public void setXC(int x) {
        this.x = x;
    }

    /*
     * CACH 1: Su dung ep kieu (Type Casting) tham chieu `this` ve lop A: ((A) this).x
     * Trong Java, viec truy cap truong (field access) duoc xac dinh o thoi diem bien dich (compile-time)
     * dua tren kieu du lieu cua bieu thuc tham chieu (JLS §15.11.1).
     * Vi the, khi ep ((A) this), Java se tro truc tiep den o nho cua truong x duoc khai bao trong lop A,
     * ma khong he anh huong den truong x cua lop B hay lop C.
     */
    public void setXA_ByCasting(int value) {
        ((A) this).x = value;
    }

    public int getXA_ByCasting() {
        return ((A) this).x;
    }

    /*
     * Truy cap va cap nhat x cua B thong qua super hoac ep kieu ((B) this).x
     */
    public void setXB(int value) {
        super.x = value;
    }

    public int getXB() {
        return super.x;
    }

    /*
     * CACH 2: Su dung Java Reflection API de truy xuat va dat gia tri cho truong x
     * duoc khai bao rieng biet trong A.class (A.class.getDeclaredField("x")).
     * Cach nay luon hoat dong ngay ca khi truong x cua lop A bi dat la private.
     */
    public void setXA_ByReflection(int value) {
        try {
            Field fieldA = A.class.getDeclaredField("x");
            fieldA.setAccessible(true);
            fieldA.setInt(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Loi khi dung Reflection de dat x cua A: " + e.getMessage(), e);
        }
    }

    public int getXA_ByReflection() {
        try {
            Field fieldA = A.class.getDeclaredField("x");
            fieldA.setAccessible(true);
            return fieldA.getInt(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Loi khi dung Reflection de doc x cua A: " + e.getMessage(), e);
        }
    }

    /*
     * CACH 3: Goi phuong thuc setter/getter ke thua tu lop A neu co.
     */
    public void setXA_ByMethod(int value) {
        super.setX(value);
    }

    public int getXA_ByMethod() {
        return super.getX();
    }

    // Hien thi dong thoi ca 3 phien ban cua bien x trong cung mot the hien
    public void printAllX(String tieuDe) {
        System.out.println("=== " + tieuDe + " ===");
        System.out.printf("  x cua lop A: %d  (thong qua ((A) this).x)%n", ((A) this).x);
        System.out.printf("  x cua lop B: %d  (thong qua super.x hoac ((B) this).x)%n", super.x);
        System.out.printf("  x cua lop C: %d  (thong qua this.x)%n", this.x);
        System.out.println("--------------------------------------------------");
    }
}
