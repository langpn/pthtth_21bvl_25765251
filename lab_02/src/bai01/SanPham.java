package bai01;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Lớp SanPham đại diện cho thông tin sản phẩm trong cửa hàng.
 * Áp dụng tính đóng gói (Encapsulation) với các thuộc tính private,
 * constructor đầy đủ tham số, getter/setter và các phương thức nghiệp vụ
 * có kiểm soát tính hợp lệ của dữ liệu trước khi thay đổi trạng thái đối tượng.
 */
public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private double donGia;
    private int soLuong;

    // Định dạng tiền tệ VND
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    // Constructor mặc định
    public SanPham() {
        this("", "", 0.0, 0);
    }

    // Constructor đầy đủ tham số
    public SanPham(String maSanPham, String tenSanPham, double donGia, int soLuong) {
        if (maSanPham == null || maSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sản phẩm không được để trống.");
        }
        if (tenSanPham == null || tenSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }
        if (donGia < 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn hoặc bằng 0.");
        }
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho không được âm.");
        }

        this.maSanPham = maSanPham.trim();
        this.tenSanPham = tenSanPham.trim();
        this.donGia = donGia;
        this.soLuong = soLuong;
    }

    // Getters and Setters
    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        if (maSanPham == null || maSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sản phẩm không được để trống.");
        }
        this.maSanPham = maSanPham.trim();
    }

    // Bí danh ngắn gọn cho mã sản phẩm
    public String getMaSP() {
        return maSanPham;
    }

    public void setMaSP(String maSP) {
        setMaSanPham(maSP);
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        if (tenSanPham == null || tenSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }
        this.tenSanPham = tenSanPham.trim();
    }

    // Bí danh ngắn gọn cho tên sản phẩm
    public String getTenSP() {
        return tenSanPham;
    }

    public void setTenSP(String tenSP) {
        setTenSanPham(tenSP);
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn hoặc bằng 0.");
        }
        this.donGia = donGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm.");
        }
        this.soLuong = soLuong;
    }

    // Bí danh cho số lượng tồn kho
    public int getSoLuongTonKho() {
        return soLuong;
    }

    public void setSoLuongTonKho(int soLuongTonKho) {
        setSoLuong(soLuongTonKho);
    }

    /**
     * Tính thành tiền của sản phẩm đang tồn kho:
     * thanhTien = donGia * soLuong
     * @return thành tiền
     */
    public double tinhThanhTien() {
        return this.donGia * this.soLuong;
    }

    /**
     * Nhập thêm hàng vào kho:
     * - Số lượng nhập phải lớn hơn 0.
     * - Cộng số lượng nhập vào tồn kho.
     * @param soLuongNhap số lượng cần nhập thêm
     */
    public void nhapHang(int soLuongNhap) {
        if (soLuongNhap > 0) {
            this.soLuong += soLuongNhap;
            System.out.printf("[Thành công] Đã nhập thêm %d đơn vị sản phẩm '%s'. Số lượng tồn kho mới: %d%n",
                    soLuongNhap, this.tenSanPham, this.soLuong);
        } else {
            System.out.println("[Thất bại] Lỗi nhập hàng: Số lượng nhập phải lớn hơn 0 (nhập vào: " + soLuongNhap + ").");
        }
    }

    /**
     * Bán hàng từ kho:
     * - Số lượng bán phải lớn hơn 0.
     * - Không được bán quá số lượng tồn kho.
     * - Nếu bán thành công, cập nhật số lượng và trả về true.
     * - Nếu không đủ hàng, giữ nguyên số lượng và trả về false.
     * @param soLuongBan số lượng cần bán
     * @return true nếu bán thành công, false nếu thất bại
     */
    public boolean banHang(int soLuongBan) {
        if (soLuongBan <= 0) {
            System.out.println("[Thất bại] Lỗi bán hàng: Số lượng bán phải lớn hơn 0 (yêu cầu bán: " + soLuongBan + ").");
            return false;
        }

        if (soLuongBan > this.soLuong) {
            System.out.printf("[Thất bại] Không đủ hàng để bán: Yêu cầu bán %d, nhưng tồn kho chỉ còn %d! Giữ nguyên số lượng.%n",
                    soLuongBan, this.soLuong);
            return false;
        }

        this.soLuong -= soLuongBan;
        System.out.printf("[Thành công] Đã bán %d đơn vị sản phẩm '%s'. Số lượng tồn kho còn lại: %d%n",
                soLuongBan, this.tenSanPham, this.soLuong);
        return true;
    }

    /**
     * Hiển thị thông tin chi tiết:
     * Mã sản phẩm, Tên sản phẩm, Đơn giá, Số lượng và Thành tiền.
     */
    public void hienThiThongTin() {
        System.out.println("+----------------------+-----------------------------------------------+");
        System.out.printf("| %-20s | %-45s |%n", "Mã sản phẩm", maSanPham);
        System.out.printf("| %-20s | %-45s |%n", "Tên sản phẩm", tenSanPham);
        System.out.printf("| %-20s | %-45s |%n", "Đơn giá", CURRENCY_FORMAT.format(donGia));
        System.out.printf("| %-20s | %-45d |%n", "Số lượng tồn kho", soLuong);
        System.out.printf("| %-20s | %-45s |%n", "Thành tiền", CURRENCY_FORMAT.format(tinhThanhTien()));
        System.out.println("+----------------------+-----------------------------------------------+");
    }

    @Override
    public String toString() {
        return String.format("SanPham [Mã: %s, Tên: %s, Đơn giá: %s, Tồn kho: %d, Thành tiền: %s]",
                maSanPham, tenSanPham, CURRENCY_FORMAT.format(donGia), soLuong, CURRENCY_FORMAT.format(tinhThanhTien()));
    }
}
