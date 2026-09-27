package bai02;

/**
 * Lớp SinhVien kế thừa từ lớp Nguoi.
 * Bổ sung các thuộc tính: mã sinh viên, ngành học, điểm trung bình.
 */
public class SinhVien extends Nguoi {
    private String maSinhVien;
    private String nganhHoc;
    private double diemTrungBinh;

    // Constructor mặc định
    public SinhVien() {
        super();
        this.maSinhVien = "";
        this.nganhHoc = "";
        this.diemTrungBinh = 0.0;
    }

    // Constructor đầy đủ tham số gọi constructor lớp cha bằng super
    public SinhVien(String hoTen, int namSinh, String diaChi,
                    String maSinhVien, String nganhHoc, double diemTrungBinh) {
        super(hoTen, namSinh, diaChi);
        setMaSinhVien(maSinhVien);
        setNganhHoc(nganhHoc);
        setDiemTrungBinh(diemTrungBinh);
    }

    // Getter và Setter
    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        if (maSinhVien == null || maSinhVien.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sinh viên không được để trống.");
        }
        this.maSinhVien = maSinhVien.trim();
    }

    // Bí danh cho mã sinh viên
    public String getMaSV() {
        return maSinhVien;
    }

    public void setMaSV(String maSV) {
        setMaSinhVien(maSV);
    }

    public String getNganhHoc() {
        return nganhHoc;
    }

    public void setNganhHoc(String nganhHoc) {
        if (nganhHoc == null || nganhHoc.trim().isEmpty()) {
            throw new IllegalArgumentException("Ngành học không được để trống.");
        }
        this.nganhHoc = nganhHoc.trim();
    }

    public double getDiemTrungBinh() {
        return diemTrungBinh;
    }

    public void setDiemTrungBinh(double diemTrungBinh) {
        if (diemTrungBinh < 0.0 || diemTrungBinh > 10.0) {
            throw new IllegalArgumentException("Điểm trung bình phải nằm trong thang điểm [0.0 - 10.0].");
        }
        this.diemTrungBinh = diemTrungBinh;
    }

    // Bí danh cho điểm trung bình
    public double getDiemTB() {
        return diemTrungBinh;
    }

    public void setDiemTB(double diemTB) {
        setDiemTrungBinh(diemTB);
    }

    /**
     * Phương thức xếp loại học lực theo thang điểm trung bình:
     * - Từ 8,5 trở lên: Giỏi
     * - Từ 7,0 đến dưới 8,5: Khá
     * - Từ 5,0 đến dưới 7,0: Trung bình
     * - Dưới 5,0: Yếu
     * @return chuỗi xếp loại
     */
    public String xepLoai() {
        if (diemTrungBinh >= 8.5) {
            return "Giỏi";
        } else if (diemTrungBinh >= 7.0) {
            return "Khá";
        } else if (diemTrungBinh >= 5.0) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }

    /**
     * Ghi đè phương thức hienThiThongTin() của lớp cha Nguoi.
     * Sử dụng super.hienThiThongTin() để hiển thị thông tin chung,
     * sau đó hiển thị các thuộc tính riêng của sinh viên cùng xếp loại.
     */
    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.printf("| %-18s: %-50s |%n", "Mã sinh viên", maSinhVien);
        System.out.printf("| %-18s: %-50s |%n", "Ngành học", nganhHoc);
        System.out.printf("| %-18s: %-50.2f |%n", "Điểm trung bình", diemTrungBinh);
        System.out.printf("| %-18s: %-50s |%n", "Xếp loại", xepLoai());
    }

    @Override
    public String toString() {
        return String.format("SinhVien [%s, maSV=%s, nganhHoc=%s, diemTB=%.2f, xepLoai=%s]",
                super.toString(), maSinhVien, nganhHoc, diemTrungBinh, xepLoai());
    }
}
