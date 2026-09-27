package bai01;

public class HCN {
    private double chieuDai;
    private double chieuRong;

    // Khoi tao khong tham so
    public HCN() {
        this(1.0, 1.0);
    }

    // Khoi tao day du tham so
    public HCN(double chieuDai, double chieuRong) {
        if (chieuDai <= 0 || chieuRong <= 0) {
            throw new IllegalArgumentException("Chieu dai va chieu rong phai lon hon 0.");
        }
        this.chieuDai = chieuDai;
        this.chieuRong = chieuRong;
    }

    // Cac phuong thuc get / set
    public double getChieuDai() {
        return chieuDai;
    }

    public void setChieuDai(double chieuDai) {
        if (chieuDai <= 0) {
            throw new IllegalArgumentException("Chieu dai phai lon hon 0.");
        }
        this.chieuDai = chieuDai;
    }

    public double getChieuRong() {
        return chieuRong;
    }

    public void setChieuRong(double chieuRong) {
        if (chieuRong <= 0) {
            throw new IllegalArgumentException("Chieu rong phai lon hon 0.");
        }
        this.chieuRong = chieuRong;
    }

    // Phuong thuc tinh chu vi
    public double tinhChuVi() {
        return (chieuDai + chieuRong) * 2;
    }

    // Phuong thuc tinh dien tich
    public double tinhDienTich() {
        return chieuDai * chieuRong;
    }

    // Phuong thuc xuat thong tin
    public void xuatThongTin() {
        System.out.printf("HCN [Chieu dai: %.2f, Chieu rong: %.2f | Chu vi: %.2f, Dien tich: %.2f]%n",
                chieuDai, chieuRong, tinhChuVi(), tinhDienTich());
    }

    @Override
    public String toString() {
        return String.format("HCN [Dai=%.2f, Rong=%.2f, ChuVi=%.2f, DienTich=%.2f]",
                chieuDai, chieuRong, tinhChuVi(), tinhDienTich());
    }
}
