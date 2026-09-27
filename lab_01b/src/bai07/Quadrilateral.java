package bai07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Quadrilateral implements Polygon {
    protected List<Point> vertices;

    public Quadrilateral(Point p1, Point p2, Point p3, Point p4) {
        this(Arrays.asList(p1, p2, p3, p4));
    }

    public Quadrilateral(List<Point> vertices) {
        if (vertices == null || vertices.size() != 4) {
            throw new IllegalArgumentException("Tu giac phai co dung 4 dinh.");
        }
        for (Point p : vertices) {
            if (p == null) throw new IllegalArgumentException("Dinh khong duoc null.");
        }
        this.vertices = new ArrayList<>(vertices);
        if (area() < 1e-6) {
            throw new IllegalArgumentException("Cac dinh khong tao thanh tu giac hop le.");
        }
    }

    protected Quadrilateral() {
        this.vertices = new ArrayList<>();
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
        return 4;
    }

    @Override
    public List<Point> getVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public String getPolygonType() {
        return "Quadrilateral";
    }

    @Override
    public String toString() {
        return String.format("%s [Dinh: %s, %s, %s, %s | Chu vi: %.2f, Dien tich: %.2f]",
                getPolygonType(), vertices.get(0), vertices.get(1), vertices.get(2), vertices.get(3),
                perimeter(), area());
    }
}
