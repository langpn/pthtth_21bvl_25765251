package bai07;

public class EquilateralTriangle extends IsoscelesTriangle {

    // Khoi tao tu do dai mot canh
    public EquilateralTriangle(double side) {
        super(side, side);
    }

    // Khoi tao tu 3 dinh
    public EquilateralTriangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
        double d1 = p1.distanceTo(p2);
        double d2 = p2.distanceTo(p3);
        double d3 = p3.distanceTo(p1);
        double eps = 1e-4;
        boolean deu = Math.abs(d1 - d2) < eps && Math.abs(d2 - d3) < eps;
        if (!deu) {
            throw new IllegalArgumentException("3 diem da cho khong tao thanh tam giac deu.");
        }
    }

    @Override
    public String getPolygonType() {
        return "EquilateralTriangle";
    }
}
