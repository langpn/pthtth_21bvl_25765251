package bai02;

import java.time.Year;

/**
 * Lớp cha Nguoi đại diện cho thông tin chung của một người trong trường đại học.
 * Chứa các thuộc tính: họ tên, năm sinh, địa chỉ.
 */
public class Nguoi {
    private String hoTen;
    private int namSinh;
    private String diaChi;

    // Constructor mặc định
    public Nguoi() {
        this("", Year.now().getValue(), "");
    }

    // Constructor đầy đủ tham số
    public Nguoi(String hoTen, int namSinh, String diaChi) {
        setHoTen(hoTen);
        setNamSinh(namSinh);
        setDiaChi(diaChi);
    }

    // Getter và Setter
    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        this.hoTen = hoTen.trim();
    }

    public int getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(int namSinh) {
        int currentYear = Year.now().getValue();
        if (namSinh < 1900 || namSinh > currentYear) {
            throw new IllegalArgumentException("Năm sinh không hợp lệ (phải từ 1900 đến " + currentYear + ").");
        }
        this.namSinh = namSinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống.");
        }
        this.diaChi = diaChi.trim();
    }

    /**
     * Phương thức tính tuổi của người dựa trên năm hiện tại.
     * @return tuổi
     */
    public int tinhTuoi() {
        return Year.now().getValue() - this.namSinh;
    }

    /**
     * Hiển thị thông tin chung của người: Họ tên, Năm sinh, Tuổi, Địa chỉ.
     */
    public void hienThiThongTin() {
        System.out.printf("| %-18s: %-50s |%n", "Họ và tên", hoTen);
        System.out.printf("| %-18s: %-50s |%n", "Năm sinh", String.format("%d (Tuổi: %d)", namSinh, tinhTuoi()));
        System.out.printf("| %-18s: %-50s |%n", "Địa chỉ", diaChi);
    }

    @Override
    public String toString() {
        return String.format("Nguoi [hoTen=%s, namSinh=%d, tuoi=%d, diaChi=%s]",
                hoTen, namSinh, tinhTuoi(), diaChi);
    }
}
