package bai07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pentagon implements Polygon {
    protected List<Point> vertices;

    // Khoi tao ngu giac deu tu do dai mot canh
    public Pentagon(double side) {
        if (side <= 0) {
            throw new IllegalArgumentException("Do dai canh phai lon hon 0.");
        }
        this.vertices = generateRegularPolygonVertices(5, side);
    }

    // Khoi tao ngu giac tu 5 dinh
    public Pentagon(List<Point> vertices) {
        if (vertices == null || vertices.size() != 5) {
            throw new IllegalArgumentException("Ngu giac phai co dung 5 dinh.");
        }
        for (Point p : vertices) {
            if (p == null) throw new IllegalArgumentException("Dinh khong duoc null.");
        }
        this.vertices = new ArrayList<>(vertices);
        if (area() < 1e-6) {
            throw new IllegalArgumentException("Cac dinh khong tao thanh ngu giac hop le.");
        }
    }

    public Pentagon(Point p1, Point p2, Point p3, Point p4, Point p5) {
        this(Arrays.asList(p1, p2, p3, p4, p5));
    }

    // Sinh toa do cac dinh cho da giac deu n canh co do dai canh side
    static List<Point> generateRegularPolygonVertices(int n, double side) {
        List<Point> list = new ArrayList<>();
        double r = side / (2.0 * Math.sin(Math.PI / n));
        for (int i = 0; i < n; i++) {
            double angle = 2.0 * Math.PI * i / n - Math.PI / 2.0;
            double px = r * Math.cos(angle);
            double py = r * Math.sin(angle);
            list.add(new Point(px, py));
        }
        return list;
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
        return 5;
    }

    @Override
    public List<Point> getVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public String getPolygonType() {
        return "Pentagon";
    }

    @Override
    public String toString() {
        return String.format("%s [So dinh: %d | Chu vi: %.2f, Dien tich: %.2f]",
                getPolygonType(), getNumberOfVertices(), perimeter(), area());
    }
}
