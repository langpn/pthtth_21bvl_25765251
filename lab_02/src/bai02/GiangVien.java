package bai02;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Lớp GiangVien kế thừa từ lớp Nguoi.
 * Bổ sung các thuộc tính: mã giảng viên, chuyên môn, lương cơ bản, hệ số lương.
 */
public class GiangVien extends Nguoi {
    private String maGiangVien;
    private String chuyenMon;
    private double luongCoBan;
    private double heSoLuong;

    // Định dạng tiền tệ VND
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    // Constructor mặc định
    public GiangVien() {
        super();
        this.maGiangVien = "";
        this.chuyenMon = "";
        this.luongCoBan = 0.0;
        this.heSoLuong = 1.0;
    }

    // Constructor đầy đủ tham số gọi constructor lớp cha bằng super
    public GiangVien(String hoTen, int namSinh, String diaChi,
                     String maGiangVien, String chuyenMon, double luongCoBan, double heSoLuong) {
        super(hoTen, namSinh, diaChi);
        setMaGiangVien(maGiangVien);
        setChuyenMon(chuyenMon);
        setLuongCoBan(luongCoBan);
        setHeSoLuong(heSoLuong);
    }

    // Getter và Setter
    public String getMaGiangVien() {
        return maGiangVien;
    }

    public void setMaGiangVien(String maGiangVien) {
        if (maGiangVien == null || maGiangVien.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giảng viên không được để trống.");
        }
        this.maGiangVien = maGiangVien.trim();
    }

    // Bí danh cho mã giảng viên
    public String getMaGV() {
        return maGiangVien;
    }

    public void setMaGV(String maGV) {
        setMaGiangVien(maGV);
    }

    public String getChuyenMon() {
        return chuyenMon;
    }

    public void setChuyenMon(String chuyenMon) {
        if (chuyenMon == null || chuyenMon.trim().isEmpty()) {
            throw new IllegalArgumentException("Chuyên môn không được để trống.");
        }
        this.chuyenMon = chuyenMon.trim();
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) {
        if (luongCoBan < 0) {
            throw new IllegalArgumentException("Lương cơ bản không được âm.");
        }
        this.luongCoBan = luongCoBan;
    }

    public double getHeSoLuong() {
        return heSoLuong;
    }

    public void setHeSoLuong(double heSoLuong) {
        if (heSoLuong <= 0) {
            throw new IllegalArgumentException("Hệ số lương phải lớn hơn 0.");
        }
        this.heSoLuong = heSoLuong;
    }

    /**
     * Tính tiền lương của giảng viên:
     * Công thức: Lương = Lương cơ bản * Hệ số lương
     * @return lương thực nhận
     */
    public double tinhLuong() {
        return this.luongCoBan * this.heSoLuong;
    }

    /**
     * Ghi đè phương thức hienThiThongTin() của lớp cha Nguoi.
     * Sử dụng super.hienThiThongTin() để hiển thị thông tin chung,
     * sau đó hiển thị các thuộc tính riêng của giảng viên và lương tính được.
     */
    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.printf("| %-18s: %-50s |%n", "Mã giảng viên", maGiangVien);
        System.out.printf("| %-18s: %-50s |%n", "Chuyên môn", chuyenMon);
        System.out.printf("| %-18s: %-50s |%n", "Lương cơ bản", CURRENCY_FORMAT.format(luongCoBan));
        System.out.printf("| %-18s: %-50.2f |%n", "Hệ số lương", heSoLuong);
        System.out.printf("| %-18s: %-50s |%n", "Lương thực nhận", CURRENCY_FORMAT.format(tinhLuong()));
    }

    @Override
    public String toString() {
        return String.format("GiangVien [%s, maGV=%s, chuyenMon=%s, luongCoBan=%s, heSoLuong=%.2f, luong=%s]",
                super.toString(), maGiangVien, chuyenMon, CURRENCY_FORMAT.format(luongCoBan), heSoLuong, CURRENCY_FORMAT.format(tinhLuong()));
    }
}
