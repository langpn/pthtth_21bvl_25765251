package bai06;

public class A {
    // Truong x rieng cua lop A
    protected int x;

    public A() {
        this.x = 10;
    }

    public A(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void printA() {
        System.out.println("Gia tri x trong A: " + this.x);
    }
}
