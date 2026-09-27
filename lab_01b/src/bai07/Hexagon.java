package bai07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hexagon implements Polygon {
    protected List<Point> vertices;

    // Khoi tao luc giac deu tu do dai canh
    public Hexagon(double side) {
        if (side <= 0) {
            throw new IllegalArgumentException("Do dai canh phai lon hon 0.");
        }
        this.vertices = Pentagon.generateRegularPolygonVertices(6, side);
    }

    // Khoi tao luc giac tu 6 dinh
    public Hexagon(List<Point> vertices) {
        if (vertices == null || vertices.size() != 6) {
            throw new IllegalArgumentException("Luc giac phai co dung 6 dinh.");
        }
        for (Point p : vertices) {
            if (p == null) throw new IllegalArgumentException("Dinh khong duoc null.");
        }
        this.vertices = new ArrayList<>(vertices);
        if (area() < 1e-6) {
            throw new IllegalArgumentException("Cac dinh khong tao thanh luc giac hop le.");
        }
    }

    public Hexagon(Point p1, Point p2, Point p3, Point p4, Point p5, Point p6) {
        this(Arrays.asList(p1, p2, p3, p4, p5, p6));
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
        return 6;
    }

    @Override
    public List<Point> getVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public String getPolygonType() {
        return "Hexagon";
    }

    @Override
    public String toString() {
        return String.format("%s [So dinh: %d | Chu vi: %.2f, Dien tich: %.2f]",
                getPolygonType(), getNumberOfVertices(), perimeter(), area());
    }
}
