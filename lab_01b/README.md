# Lab 01b: Ôn tập và Bổ sung Lập trình Java (Review Java)

## Cấu trúc thư mục

```text
lab_01b/
├── README.md
├── src/
│   ├── bai01/
│   │   ├── HCN.java                 # Đối tượng Hình chữ nhật (chu vi, diện tích, getter/setter)
│   │   ├── HVuong.java              # Đối tượng Hình vuông (kế thừa HCN)
│   │   ├── HTG.java                 # Đối tượng Hình tam giác (Heron formula, phân loại tam giác)
│   │   └── DemoHinh.java            # Chương trình kiểm thử các hình
│   ├── bai02/
│   │   ├── IntArray.java            # Cấu trúc mảng số nguyên tối đa n phần tử (thêm, xóa, radix sort, linear/binary search)
│   │   └── DemoArray.java           # Chương trình kiểm thử các thao tác trên mảng
│   ├── bai03/
│   │   └── Person.java              # Lớp Person (Tên, giới tính, ngày sinh, địa chỉ, inputInfo, printInfo)
│   ├── bai04/
│   │   └── Student.java             # Lớp Student kế thừa Person (MSSV, GPA, Email validation, học bổng)
│   ├── bai05/
│   │   └── StudentDemo.java         # Quản lý n sinh viên, max/min GPA, lọc học bổng
│   ├── bai06/
│   │   ├── A.java                   # Lớp A với biến thể hiện x
│   │   ├── B.java                   # Lớp B mở rộng A với biến thể hiện x
│   │   ├── C.java                   # Lớp C mở rộng B với biến thể hiện x (truy cập và gán A.x)
│   │   └── DemoABC.java             # Kiểm thử và giải thích chi tiết cơ chế che khuất biến (variable shadowing)
│   └── bai07/
│       ├── Point.java               # Đối tượng điểm 2D (tọa độ x, y, khoảng cách)
│       ├── Polygon.java             # Giao diện Polygon (area, perimeter, Shoelace formula, so sánh đồng dạng)
│       ├── Triangle.java            # Lớp Triangle triển khai Polygon
│       ├── IsoscelesTriangle.java   # Lớp IsoscelesTriangle kế thừa Triangle
│       ├── EquilateralTriangle.java # Lớp EquilateralTriangle kế thừa IsoscelesTriangle
│       ├── Quadrilateral.java       # Lớp Quadrilateral triển khai Polygon
│       ├── Rectangle.java           # Lớp Rectangle kế thừa Quadrilateral
│       ├── Square.java              # Lớp Square kế thừa Rectangle
│       ├── Pentagon.java            # Lớp Pentagon triển khai Polygon (ngũ giác)
│       ├── Hexagon.java             # Lớp Hexagon triển khai Polygon (lục giác)
│       ├── Octagon.java             # Lớp Octagon triển khai Polygon (bát giác)
│       ├── GenericPolygon.java      # Lớp GenericPolygon cho đa giác n cạnh bất kỳ
│       └── PolygonDemo.java         # Giao diện dòng lệnh tương tác, tạo hình học, tọa độ đỉnh và so sánh giống nhau
└── out/                             # Thư mục chứa các tệp bytecode sau khi biên dịch (.class)
```

---

## Hướng dẫn Biên dịch và Chạy thử

Đứng tại thư mục gốc repository (`/Users/langpn/.org/dev/pthtth_21bvl_25765251`) hoặc tại `lab_01b`:

### 1. Biên dịch toàn bộ Lab 01b:
```bash
javac -d lab_01b/out $(find lab_01b/src -name "*.java")
```

### 2. Chạy từng bài:

- **Bài 1 - Các đối tượng hình học (HCN, HVuong, HTG):**
  ```bash
  java -cp lab_01b/out bai01.DemoHinh
  ```

- **Bài 2 - Mảng số nguyên (Thao tác, Radix Sort, Linear & Binary Search):**
  ```bash
  java -cp lab_01b/out bai02.DemoArray
  ```

- **Bài 5 - Quản lý sinh viên (Kiểm thử Student kế thừa Person):**
  ```bash
  java -cp lab_01b/out bai05.StudentDemo
  ```

- **Bài 6 - Kế thừa 3 lớp A, B, C và truy cập biến x của A:**
  ```bash
  java -cp lab_01b/out bai06.DemoABC
  ```

- **Bài 7 - Giao diện Polygon và hệ thống đa giác:**
  ```bash
  java -cp lab_01b/out bai07.PolygonDemo
  ```

---

## Tóm tắt Chi tiết Từng Bài

### Bài 1: Đối tượng HCN, HVuong, HTG
- `HCN`: Khởi tạo mặc định (1, 1) và có tham số; tính chu vi `(d + r) * 2`, diện tích `d * r`.
- `HVuong`: Kế thừa `HCN`, đồng bộ hóa chiều dài và chiều rộng theo cạnh `canh`.
- `HTG`: Kiểm tra bất đẳng thức tam giác ($a+b>c, a+c>b, b+c>a$), tính chu vi $a+b+c$, diện tích theo công thức Heron $S = \sqrt{p(p-a)(p-b)(p-c)}$, phân loại tam giác (đều, cân, vuông, thường).
- `DemoHinh`: Kiểm tra đầy đủ constructor, getter, setter và hỗ trợ nhập trực tiếp từ bàn phím.

### Bài 2: Mảng số nguyên tối đa n phần tử
- `IntArray`: Quản lý mảng với sức chứa cố định `capacity` và số phần tử thực tế `size`.
- Các thao tác:
  - Thêm phần tử $Y$ vào đầu, cuối và vị trí thứ $i$.
  - Xóa phần tử có giá trị $X$ và xóa phần tử tại vị trí thứ $j$.
  - Sắp xếp **Radix Sort** (LSD): Hỗ trợ sắp xếp tăng dần và giảm dần, phân tách và xử lý chuẩn xác cả số âm lẫn số không âm.
  - Tìm kiếm phần tử $B$: Tìm kiếm tuần tự (**Linear Search**) trên mảng chưa sắp xếp và tìm kiếm nhị phân (**Binary Search**) trên mảng đã sắp xếp (tự động nhận biết thứ tự tăng hoặc giảm).

### Bài 3 & Bài 4 & Bài 5: Person, Student & StudentDemo
- `Person`: Thuộc tính `name`, `gender`, `birthDate`, `address`; phương thức `inputInfo()` và `printInfo()`.
- `Student`: Kế thừa `Person`, bổ sung `studentId`, `email` (ràng buộc chứa `@` và không có khoảng trắng) và `gpa` (0.0 đến 10.0). Phương thức `xetHocBong()` xét điều kiện GPA > 8.0.
- `StudentDemo`: Nhập danh sách $n$ sinh viên từ bàn phím (hoặc nạp bộ dữ liệu mẫu), hiển thị toàn bộ sinh viên, tìm sinh viên có GPA cao nhất/thấp nhất và lọc sinh viên đạt học bổng.

### Bài 6: Che khuất biến (Field Shadowing) và Truy xuất Biến của Ancestor
- Ba lớp $A$, $B$ ($extends\ A$), $C$ ($extends\ B$) đều định nghĩa biến thể hiện tên là `x`.
- **Vấn đề trong Java:** Biến thể hiện trong Java không có tính đa hình (non-polymorphic). Cú pháp `super.super.x` không hợp lệ trong Java.
- **Giải pháp chính:**
  1. **Ép kiểu tham chiếu `this` về lớp $A$:** `((A) this).x = value;`
     Theo đặc tả JLS §15.11.1, việc truy cập trường được xác định tại thời điểm biên dịch dựa trên kiểu tĩnh của biểu thức. Do đó, `((A) this).x` trỏ thẳng tới vùng nhớ biến `x` khai báo tại lớp $A$ mà hoàn toàn không thay đổi `B.x` hay `C.x`.
  2. **Dùng Java Reflection API:** `A.class.getDeclaredField("x")` đặt giá trị cho `this`.
  3. **Thông qua phương thức của $A$:** Gọi setter thừa kế từ $A$.
- `DemoABC`: Chạy kiểm thử xác nhận độc lập: chỉ có `A.x` thay đổi, `B.x` và `C.x` được bảo toàn nguyên vẹn.

### Bài 7: Giao diện Polygon và Hệ thống Đa giác
- `interface Polygon`:
  - Phương thức: `double area()`, `double perimeter()`.
  - Tính diện tích tổng quát bằng công thức **Shoelace (Gauss Area Formula)**.
  - Tính chu vi tổng quát bằng tổng khoảng cách Euclid giữa các đỉnh liên tiếp.
  - Kiểm tra hai đa giác giống nhau:
    + `haveSameVertices`: Kiểm tra trùng khớp hoàn toàn tọa độ các đỉnh (xét mọi phép quay vòng cyclic và thứ tự thuận/nghịch).
    + `areCongruent`: Kiểm tra tương đồng hình học (cùng số đỉnh, cùng tập cạnh, cùng chu vi và diện tích).
- Phả hệ kế thừa:
  - `Triangle` $\rightarrow$ `IsoscelesTriangle` $\rightarrow$ `EquilateralTriangle`
  - `Quadrilateral` $\rightarrow$ `Rectangle` $\rightarrow$ `Square`
  - `Pentagon`, `Hexagon`, `Octagon`
- `PolygonDemo`: Giao diện console tương tác đầy đủ các chức năng: tạo theo kích thước, tạo theo tọa độ đỉnh, so sánh 2 đa giác và bộ test tự động.
