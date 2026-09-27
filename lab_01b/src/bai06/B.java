package bai06;

public class B extends A {
    // Truong x rieng cua lop B (shadows field x cua A)
    protected int x;

    public B() {
        super(10);
        this.x = 20;
    }

    public B(int xA, int xB) {
        super(xA);
        this.x = xB;
    }

    public int getXB() {
        return this.x;
    }

    public void setXB(int x) {
        this.x = x;
    }

    public void printB() {
        System.out.println("Gia tri x trong B: " + this.x);
    }
}
