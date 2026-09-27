package bai07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface Polygon {
    // Phuong thuc bat buoc theo yeu cau de bai
    double area();
    double perimeter();

    // Cac phuong thuc bo tro
    int getNumberOfVertices();
    List<Point> getVertices();
    String getPolygonType();

    // Tinh chu vi tu danh sach cac dinh khep kin
    static double calculatePerimeter(List<Point> vertices) {
        if (vertices == null || vertices.size() < 3) return 0.0;
        double p = 0.0;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            p += vertices.get(i).distanceTo(vertices.get((i + 1) % n));
        }
        return p;
    }

    // Tinh dien tich bang cong thuc Shoelace (Gauss Area Formula)
    static double calculateShoelaceArea(List<Point> vertices) {
        if (vertices == null || vertices.size() < 3) return 0.0;
        double sum = 0.0;
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            Point current = vertices.get(i);
            Point next = vertices.get((i + 1) % n);
            sum += (current.getX() * next.getY()) - (next.getX() * current.getY());
        }
        return Math.abs(sum) / 2.0;
    }

    // Lay danh sach do dai cac canh
    default List<Double> getSideLengths() {
        List<Point> pts = getVertices();
        List<Double> sides = new ArrayList<>();
        if (pts != null && pts.size() >= 3) {
            int n = pts.size();
            for (int i = 0; i < n; i++) {
                sides.add(pts.get(i).distanceTo(pts.get((i + 1) % n)));
            }
        }
        return sides;
    }

    /*
     * Kiem tra hai da giac co cung toa do dinh hay khong.
     * Ho tro quay vong (cyclic shift) va theo chieu thuan hoac chieu nguoc kim dong ho.
     */
    static boolean haveSameVertices(Polygon p1, Polygon p2) {
        if (p1 == null || p2 == null) return false;
        List<Point> v1 = p1.getVertices();
        List<Point> v2 = p2.getVertices();
        if (v1 == null || v2 == null || v1.size() != v2.size()) return false;
        int n = v1.size();

        // Kiem tra thu tu thuan
        for (int shift = 0; shift < n; shift++) {
            boolean match = true;
            for (int i = 0; i < n; i++) {
                if (!v1.get(i).equals(v2.get((i + shift) % n))) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }

        // Kiem tra thu tu nguoc
        for (int shift = 0; shift < n; shift++) {
            boolean match = true;
            for (int i = 0; i < n; i++) {
                int revIdx = (shift - i + n) % n;
                if (!v1.get(i).equals(v2.get(revIdx))) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }

        return false;
    }

    /*
     * Kiem tra hai da giac co bang nhau / dong dang / tuong dong hinh hoc khong:
     * - Cung so dinh
     * - Cung chu vi (sai so < 1e-4)
     * - Cung dien tich (sai so < 1e-4)
     * - Tap hop cac canh bang nhau
     */
    static boolean areCongruent(Polygon p1, Polygon p2) {
        if (p1 == null || p2 == null) return false;
        if (p1.getNumberOfVertices() != p2.getNumberOfVertices()) return false;
        if (Math.abs(p1.perimeter() - p2.perimeter()) > 1e-4) return false;
        if (Math.abs(p1.area() - p2.area()) > 1e-4) return false;

        List<Double> s1 = new ArrayList<>(p1.getSideLengths());
        List<Double> s2 = new ArrayList<>(p2.getSideLengths());
        if (s1.size() == s2.size() && !s1.isEmpty()) {
            Collections.sort(s1);
            Collections.sort(s2);
            for (int i = 0; i < s1.size(); i++) {
                if (Math.abs(s1.get(i) - s2.get(i)) > 1e-4) {
                    return false;
                }
            }
        }
        return true;
    }
}
