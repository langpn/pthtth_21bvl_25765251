package bai07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Triangle implements Polygon {
    protected List<Point> vertices;

    // Khoi tao tu 3 dinh
    public Triangle(Point p1, Point p2, Point p3) {
        if (p1 == null || p2 == null || p3 == null) {
            throw new IllegalArgumentException("Cac dinh khong duoc null.");
        }
        this.vertices = new ArrayList<>(Arrays.asList(p1, p2, p3));
        if (area() < 1e-6) {
            throw new IllegalArgumentException("3 diem thang hang khong the tao thanh tam giac.");
        }
    }

    // Khoi tao tu do dai 3 canh
    public Triangle(double a, double b, double c) {
        if (!kiemTraHopLe(a, b, c)) {
            throw new IllegalArgumentException(
                    String.format("Do dai ba canh (%.2f, %.2f, %.2f) khong hop le.", a, b, c));
        }
        // Tao toa do chuan hoa cho 3 dinh
        // Dinh A tai (0, 0), Dinh B tai (c, 0)
        // Dinh C tai (b * cos(A), b * sin(A))
        double cosA = (b * b + c * c - a * a) / (2 * b * c);
        double sinA = Math.sqrt(Math.max(0.0, 1.0 - cosA * cosA));
        Point pA = new Point(0, 0);
        Point pB = new Point(c, 0);
        Point pC = new Point(b * cosA, b * sinA);
        this.vertices = new ArrayList<>(Arrays.asList(pA, pB, pC));
    }

    protected Triangle() {
        this.vertices = new ArrayList<>();
    }

    private static boolean kiemTraHopLe(double a, double b, double c) {
        return a > 0 && b > 0 && c > 0 && (a + b > c) && (a + c > b) && (b + c > a);
    }

    @Override
    public double area() {
        return Polygon.calculateShoelaceArea(vertices);
    }

    @Override
    public double perimeter() {
        return Polygon.calculatePerimeter(vertices);
    }

    @Override
    public int getNumberOfVertices() {
        return 3;
    }

    @Override
    public List<Point> getVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public String getPolygonType() {
        return "Triangle";
    }

    @Override
    public String toString() {
        return String.format("%s [Dinh: %s, %s, %s | Chu vi: %.2f, Dien tich: %.2f]",
                getPolygonType(), vertices.get(0), vertices.get(1), vertices.get(2), perimeter(), area());
    }
}
