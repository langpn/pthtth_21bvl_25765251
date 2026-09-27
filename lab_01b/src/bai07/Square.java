package bai07;

public class Square extends Rectangle {

    // Khoi tao tu do dai mot canh
    public Square(double side) {
        super(side, side);
    }

    // Khoi tao tu 4 dinh
    public Square(Point p1, Point p2, Point p3, Point p4) {
        super(p1, p2, p3, p4);
        if (Math.abs(this.width - this.height) > 1e-4) {
            throw new IllegalArgumentException("4 diem da cho khong tao thanh hinh vuong.");
        }
    }

    public double getSide() {
        return this.width;
    }

    @Override
    public String getPolygonType() {
        return "Square";
    }
}
