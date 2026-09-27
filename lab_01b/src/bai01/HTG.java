package bai01;

public class HTG {
    private double ma;
    private double mb;
    private double mc;

    // Khoi tao khong tham so (tam giac vuong 3 - 4 - 5)
    public HTG() {
        this(3.0, 4.0, 5.0);
    }

    // Khoi tao day du tham so
    public HTG(double ma, double mb, double mc) {
        if (!kiemTraHopLe(ma, mb, mc)) {
            throw new IllegalArgumentException(
                    String.format("Ba canh (%.2f, %.2f, %.2f) khong tao thanh mot tam giac hop le.", ma, mb, mc));
        }
        this.ma = ma;
        this.mb = mb;
        this.mc = mc;
    }

    private static boolean kiemTraHopLe(double a, double b, double c) {
        return a > 0 && b > 0 && c > 0 && (a + b > c) && (a + c > b) && (b + c > a);
    }

    // Cac phuong thuc get
    public double getMa() {
        return ma;
    }

    public double getMb() {
        return mb;
    }

    public double getMc() {
        return mc;
    }

    // Cac phuong thuc set
    public void setMa(double ma) {
        if (!kiemTraHopLe(ma, this.mb, this.mc)) {
            throw new IllegalArgumentException("Gia tri canh ma moi khong tao thanh tam giac hop le.");
        }
        this.ma = ma;
    }

    public void setMb(double mb) {
        if (!kiemTraHopLe(this.ma, mb, this.mc)) {
            throw new IllegalArgumentException("Gia tri canh mb moi khong tao thanh tam giac hop le.");
        }
        this.mb = mb;
    }

    public void setMc(double mc) {
        if (!kiemTraHopLe(this.ma, this.mb, mc)) {
            throw new IllegalArgumentException("Gia tri canh mc moi khong tao thanh tam giac hop le.");
        }
        this.mc = mc;
    }

    public void setCanh(double ma, double mb, double mc) {
        if (!kiemTraHopLe(ma, mb, mc)) {
            throw new IllegalArgumentException("Ba canh khong tao thanh tam giac hop le.");
        }
        this.ma = ma;
        this.mb = mb;
        this.mc = mc;
    }

    // Phuong thuc tinh chu vi
    public double tinhChuVi() {
        return ma + mb + mc;
    }

    // Phuong thuc tinh dien tich bang cong thuc Heron
    public double tinhDienTich() {
        double p = tinhChuVi() / 2.0;
        return Math.sqrt(p * (p - ma) * (p - mb) * (p - mc));
    }

    // Phuong thuc xac dinh loai tam giac
    public String loaiTamGiac() {
        double eps = 1e-6;
        boolean deu = Math.abs(ma - mb) < eps && Math.abs(mb - mc) < eps;
        if (deu) return "Tam giac deu";

        boolean can = Math.abs(ma - mb) < eps || Math.abs(ma - mc) < eps || Math.abs(mb - mc) < eps;
        boolean vuong = Math.abs(ma * ma + mb * mb - mc * mc) < eps
                || Math.abs(ma * ma + mc * mc - mb * mb) < eps
                || Math.abs(mb * mb + mc * mc - ma * ma) < eps;

        if (vuong && can) return "Tam giac vuong can";
        if (vuong) return "Tam giac vuong";
        if (can) return "Tam giac can";
        return "Tam giac thuong";
    }

    // Phuong thuc xuat thong tin
    public void xuatThongTin() {
        System.out.printf("HTG [Canh a: %.2f, b: %.2f, c: %.2f | Loai: %s | Chu vi: %.2f, Dien tich: %.2f]%n",
                ma, mb, mc, loaiTamGiac(), tinhChuVi(), tinhDienTich());
    }

    @Override
    public String toString() {
        return String.format("HTG [a=%.2f, b=%.2f, c=%.2f, Loai=%s, ChuVi=%.2f, DienTich=%.2f]",
                ma, mb, mc, loaiTamGiac(), tinhChuVi(), tinhDienTich());
    }
}
