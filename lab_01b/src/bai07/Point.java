package bai07;

import java.util.Objects;

public class Point {
    private final double x;
    private final double y;

    public static final double EPSILON = 1e-5;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanceTo(Point other) {
        if (other == null) return 0.0;
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Math.abs(this.x - point.x) < EPSILON && Math.abs(this.y - point.y) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(x * 1000.0), Math.round(y * 1000.0));
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
