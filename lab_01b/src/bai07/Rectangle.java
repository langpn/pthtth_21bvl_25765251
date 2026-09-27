package bai07;

import java.util.Arrays;

public class Rectangle extends Quadrilateral {
    protected double width;
    protected double height;

    // Khoi tao tu chieu rong va chieu cao
    public Rectangle(double width, double height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Chieu rong va chieu cao phai lon hon 0.");
        }
        this.width = width;
        this.height = height;
        // Cac dinh chuan: (0, 0), (width, 0), (width, height), (0, height)
        this.vertices = Arrays.asList(
                new Point(0, 0),
                new Point(width, 0),
                new Point(width, height),
                new Point(0, height)
        );
    }

    // Khoi tao tu 4 dinh
    public Rectangle(Point p1, Point p2, Point p3, Point p4) {
        super(p1, p2, p3, p4);
        double d12 = p1.distanceTo(p2);
        double d23 = p2.distanceTo(p3);
        double d34 = p3.distanceTo(p4);
        double d41 = p4.distanceTo(p1);
        double diag1 = p1.distanceTo(p3);
        double diag2 = p2.distanceTo(p4);
        double eps = 1e-4;

        boolean oppositeEqual = Math.abs(d12 - d34) < eps && Math.abs(d23 - d41) < eps;
        boolean diagonalsEqual = Math.abs(diag1 - diag2) < eps;
        if (!oppositeEqual || !diagonalsEqual) {
            throw new IllegalArgumentException("4 diem da cho khong tao thanh hinh chu nhat hop le.");
        }
        this.width = Math.min(d12, d23);
        this.height = Math.max(d12, d23);
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }

    @Override
    public String getPolygonType() {
        return "Rectangle";
    }
}
