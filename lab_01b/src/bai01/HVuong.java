package bai01;

public class HVuong extends HCN {

    // Khoi tao khong tham so
    public HVuong() {
        super(1.0, 1.0);
    }

    // Khoi tao day du tham so
    public HVuong(double canh) {
        super(canh, canh);
    }

    // Cac phuong thuc get / set
    public double getCanh() {
        return getChieuDai();
    }

    public void setCanh(double canh) {
        if (canh <= 0) {
            throw new IllegalArgumentException("Canh hinh vuong phai lon hon 0.");
        }
        super.setChieuDai(canh);
        super.setChieuRong(canh);
    }

    @Override
    public void setChieuDai(double chieuDai) {
        setCanh(chieuDai);
    }

    @Override
    public void setChieuRong(double chieuRong) {
        setCanh(chieuRong);
    }

    @Override
    public double tinhChuVi() {
        return getCanh() * 4;
    }

    @Override
    public double tinhDienTich() {
        return getCanh() * getCanh();
    }

    @Override
    public void xuatThongTin() {
        System.out.printf("HVuong [Canh: %.2f | Chu vi: %.2f, Dien tich: %.2f]%n",
                getCanh(), tinhChuVi(), tinhDienTich());
    }

    @Override
    public String toString() {
        return String.format("HVuong [Canh=%.2f, ChuVi=%.2f, DienTich=%.2f]",
                getCanh(), tinhChuVi(), tinhDienTich());
    }
}
