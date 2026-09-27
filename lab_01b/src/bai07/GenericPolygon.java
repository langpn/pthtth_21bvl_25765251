package bai07;

import java.util.ArrayList;
import java.util.List;

public class GenericPolygon implements Polygon {
    private final List<Point> vertices;

    public GenericPolygon(List<Point> vertices) {
        if (vertices == null || vertices.size() < 3) {
            throw new IllegalArgumentException("Da giac phai co it nhat 3 dinh.");
        }
        for (Point p : vertices) {
            if (p == null) throw new IllegalArgumentException("Dinh khong duoc null.");
        }
        this.vertices = new ArrayList<>(vertices);
        if (area() < 1e-6) {
            throw new IllegalArgumentException("Cac dinh da cho khong tao thanh da giac hop le.");
        }
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
        return vertices.size();
    }

    @Override
    public List<Point> getVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public String getPolygonType() {
        return "Polygon-" + vertices.size();
    }

    @Override
    public String toString() {
        return String.format("%s [So dinh: %d | Chu vi: %.2f, Dien tich: %.2f]",
                getPolygonType(), vertices.size(), perimeter(), area());
    }
}
