package bai07;

public class IsoscelesTriangle extends Triangle {

    // Khoi tao tu canh ben (leg) va canh day (base)
    public IsoscelesTriangle(double leg, double base) {
        super(leg, leg, base);
    }

    // Khoi tao tu 3 dinh
    public IsoscelesTriangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
        double d1 = p1.distanceTo(p2);
        double d2 = p2.distanceTo(p3);
        double d3 = p3.distanceTo(p1);
        double eps = 1e-4;
        boolean can = Math.abs(d1 - d2) < eps || Math.abs(d2 - d3) < eps || Math.abs(d3 - d1) < eps;
        if (!can) {
            throw new IllegalArgumentException("3 diem da cho khong tao thanh tam giac can.");
        }
    }

    @Override
    public String getPolygonType() {
        return "IsoscelesTriangle";
    }
}
