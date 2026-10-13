import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
import os

def set_cell_background(cell, fill_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = OxmlElement('w:shd')
    shd.set(qn('w:val'), 'clear')
    shd.set(qn('w:color'), 'auto')
    shd.set(qn('w:fill'), fill_hex)
    tcPr.append(shd)

def create_report(output_path):
    doc = docx.Document()

    # Set margins
    for section in doc.sections:
        section.top_margin = Inches(0.8)
        section.bottom_margin = Inches(0.8)
        section.left_margin = Inches(0.8)
        section.right_margin = Inches(0.8)

    # Header section
    title = doc.add_paragraph()
    title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run_univ = title.add_run("BỘ CÔNG THƯƠNG\nTRƯỜNG ĐẠI HỌC CÔNG NGHIỆP TP. HỒ CHÍ MINH\nKHOA CÔNG NGHỆ THÔNG TIN\n")
    run_univ.bold = True
    run_univ.font.size = Pt(13)
    run_univ.font.name = "Times New Roman"

    p_div = doc.add_paragraph()
    p_div.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_div = p_div.add_run("------------------------------------***------------------------------------")
    r_div.font.size = Pt(10)

    # Document Title
    p_title = doc.add_paragraph()
    p_title.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_doc_title = p_title.add_run("BÁO CÁO BÀI KIỂM TRA THƯỜNG KỲ\nHỌC PHẦN: PHÁT TRIỂN HỆ THỐNG TÍCH HỢP\n")
    r_doc_title.bold = True
    r_doc_title.font.size = Pt(16)
    r_doc_title.font.color.rgb = RGBColor(0, 51, 153)
    r_doc_title.font.name = "Times New Roman"

    p_sub = doc.add_paragraph()
    p_sub.alignment = WD_ALIGN_PARAGRAPH.CENTER
    r_sub = p_sub.add_run("ĐỀ TÀI: XÂY DỰNG CHƯƠNG TRÌNH CHAT NHÓM ĐA PHÒNG QUA TCP SOCKET\n")
    r_sub.bold = True
    r_sub.font.size = Pt(13)
    r_sub.font.name = "Times New Roman"

    # Student Info Box
    tbl_info = doc.add_table(rows=6, cols=2)
    tbl_info.alignment = WD_TABLE_ALIGNMENT.CENTER
    info_data = [
        ("Sinh viên thực hiện:", "Phạm Ngọc Lang"),
        ("Mã số sinh viên (MSSV):", "25765251"),
        ("Lớp học phần:", "21BVL / 21PVL"),
        ("Giảng viên hướng dẫn:", "ThS. Giảng viên phụ trách môn"),
        ("Link GitHub Repository cá nhân:", "https://github.com/langpn/pthtth_21bvl_25765251"),
        ("Link Source Code bài kiểm tra:", "https://github.com/langpn/pthtth_21bvl_25765251/tree/main/kiem_tra_thuong_ky")
    ]
    for i, (k, v) in enumerate(info_data):
        row = tbl_info.rows[i]
        c0 = row.cells[0]
        c1 = row.cells[1]
        c0.text = k
        c1.text = v
        c0.paragraphs[0].runs[0].bold = True
        c0.paragraphs[0].runs[0].font.name = "Times New Roman"
        c1.paragraphs[0].runs[0].font.name = "Times New Roman"
        if i >= 4:
            c1.paragraphs[0].runs[0].bold = True
            c1.paragraphs[0].runs[0].font.color.rgb = RGBColor(0, 102, 204)
        set_cell_background(c0, "F2F2F2")
        set_cell_background(c1, "FFFFFF")

    doc.add_paragraph()

    # Section 1: Yêu cầu đề bài
    h1 = doc.add_heading("1. YÊU CẦU ĐỀ BÀI", level=1)
    h1.runs[0].font.name = "Times New Roman"
    h1.runs[0].font.color.rgb = RGBColor(0, 51, 153)

    p_req = doc.add_paragraph()
    p_req.paragraph_format.line_spacing = 1.2
    r_req = p_req.add_run(
        "Xây dựng chương trình Chat nhóm qua giao thức TCP Socket:\n"
        "• Mỗi Client nhập Tên và kết nối tới Server.\n"
        "• Chọn 1 phòng trong danh sách phòng do Server cung cấp (ví dụ: phòng Study, phòng Game, phòng TraiNghiem...) "
        "hoặc sinh viên có thể tự đề xuất tên phòng mới.\n"
        "• Cơ chế cô lập: Client chỉ nhận được tin nhắn từ những Client khác trong cùng phòng đó.\n"
        "• Nhập lệnh /quit để rời phòng hiện tại và quay lại danh sách chọn phòng.\n"
        "• Nhập lệnh /exit để ngắt kết nối an toàn tới Server.\n"
        "• Nộp mã nguồn nén .zip và Báo cáo chụp màn hình kết quả chạy (full màn hình có thông tin ngày tháng)."
    )
    r_req.font.name = "Times New Roman"
    r_req.font.size = Pt(11)

    # Section 2: Kiến trúc hệ thống
    h2 = doc.add_heading("2. THIẾT KẾ KIẾN TRÚC & NGUYÊN LÝ HOẠT ĐỘNG", level=1)
    h2.runs[0].font.name = "Times New Roman"
    h2.runs[0].font.color.rgb = RGBColor(0, 51, 153)

    p_arch = doc.add_paragraph()
    p_arch.paragraph_format.line_spacing = 1.2
    r_arch = p_arch.add_run(
        "Hệ thống được thiết kế theo chuẩn Java 17 LTS thuần (không phụ thuộc framework ngoài):\n"
        "• Mô hình Server: Master-Worker sử dụng Executors.newFixedThreadPool(50). Luồng chính lắng nghe tại cổng TCP 5006, "
        "tiếp nhận kết nối qua serverSocket.accept() và chuyển giao Socket cho ClientHandler xử lý độc lập.\n"
        "• Quản lý an toàn đa luồng (Thread-Safety): Danh sách phòng và danh sách người dùng online được lưu trữ trong ConcurrentHashMap "
        "để triệt tiêu lỗi ConcurrentModificationException khi nhiều Client tham gia/rời phòng cùng thời điểm.\n"
        "• Kiến trúc Client 2 luồng song song: Tách biệt Luồng đọc Socket nền (Reader Thread) và Luồng đọc bàn phím (Main Input Thread) "
        "giúp giao diện Console không bị nghẽn (non-blocking IO trên console).\n"
        "• Cơ chế mã hóa: Sử dụng StandardCharsets.UTF_8 và PrintWriter(..., true) bật autoFlush đảm bảo dữ liệu hiển thị đúng tiếng Việt "
        "và truyền đi ngay lập tức."
    )
    r_arch.font.name = "Times New Roman"
    r_arch.font.size = Pt(11)

    # Section 3: Bảng kiểm thử thực nghiệm
    h3 = doc.add_heading("3. KẾT QUẢ KIỂM THỬ THỰC NGHIỆM (5 TEST CASES)", level=1)
    h3.runs[0].font.name = "Times New Roman"
    h3.runs[0].font.color.rgb = RGBColor(0, 51, 153)

    tbl_test = doc.add_table(rows=6, cols=4)
    tbl_test.alignment = WD_TABLE_ALIGNMENT.CENTER
    headers = ["Kịch bản", "Hành động thực nghiệm", "Kết quả mong đợi", "Đánh giá"]
    for j, h in enumerate(headers):
        cell = tbl_test.rows[0].cells[j]
        cell.text = h
        cell.paragraphs[0].runs[0].bold = True
        cell.paragraphs[0].runs[0].font.name = "Times New Roman"
        set_cell_background(cell, "003399")
        cell.paragraphs[0].runs[0].font.color.rgb = RGBColor(255, 255, 255)

    test_rows = [
        ("Case 1: Khởi động Server", "Chạy ChatRoomServer tại port 5006", "Server mở cổng, sẵn sàng 3 phòng mặc định: Study, Game, TraiNghiem", "ĐẠT (PASS)"),
        ("Case 2: Chat cùng phòng", "Client An và Binh cùng vào phòng Study và nhắn tin", "Tin nhắn hiển thị tức thì cho cả 2 client trong phòng Study", "ĐẠT (PASS)"),
        ("Case 3: Cách ly phòng", "Client Cuong vào phòng Game và nhắn tin", "Chỉ thành viên phòng Game thấy, phòng Study hoàn toàn không bị làm phiền", "ĐẠT (PASS)"),
        ("Case 4: Lệnh /quit", "Client An gõ /quit rời phòng Study sang phòng Game", "Study nhận thông báo An rời phòng; An trở về Menu và vào phòng Game thành công", "ĐẠT (PASS)"),
        ("Case 5: Lệnh /exit", "Client Cuong gõ /exit để thoát", "Server giải phóng tài nguyên, thông báo Cuong rời phòng, ngắt socket an toàn", "ĐẠT (PASS)")
    ]

    for i, row_data in enumerate(test_rows, start=1):
        row = tbl_test.rows[i]
        for j, val in enumerate(row_data):
            c = row.cells[j]
            c.text = val
            c.paragraphs[0].runs[0].font.name = "Times New Roman"
            c.paragraphs[0].runs[0].font.size = Pt(10)
            if j == 3:
                c.paragraphs[0].runs[0].bold = True
                c.paragraphs[0].runs[0].font.color.rgb = RGBColor(0, 153, 51)
            if i % 2 == 1:
                set_cell_background(c, "F9F9F9")

    doc.add_paragraph()

    # Section 4: Minh chứng ảnh chụp
    h4 = doc.add_heading("4. MINH CHỨNG ẢNH CHỤP MÀN HÌNH (FULL MÀN HÌNH CÓ NGÀY GIỜ)", level=1)
    h4.runs[0].font.name = "Times New Roman"
    h4.runs[0].font.color.rgb = RGBColor(0, 51, 153)

    p_scr = doc.add_paragraph()
    r_scr = p_scr.add_run(
        "• Dưới đây là ảnh chụp màn hình chạy thực tế hệ thống đồng thời gồm 1 Server và 3 Client (An, Binh, Cuong):\n"
        "• Yêu cầu đã đáp ứng: Chụp full màn hình macOS có hiển thị rõ thanh ngày tháng, đồng hồ hệ thống và kết quả chạy trực tiếp từ Terminal."
    )
    r_scr.font.name = "Times New Roman"
    r_scr.font.size = Pt(11)

    # Check if there are screenshots in screenshots/
    screenshot_dir = os.path.join(os.path.dirname(output_path), "screenshots")
    if os.path.exists(screenshot_dir):
        images = [f for f in os.listdir(screenshot_dir) if f.lower().endswith(('.png', '.jpg', '.jpeg'))]
        if images:
            for img_name in sorted(images):
                img_path = os.path.join(screenshot_dir, img_name)
                doc.add_paragraph(f"Hình: {img_name}")
                doc.add_picture(img_path, width=Inches(6.5))
        else:
            p_placeholder = doc.add_paragraph()
            r_pl = p_placeholder.add_run("[VỊ TRÍ CHÈN ẢNH CHỤP MÀN HÌNH FULL DESKTOP CỦA SINH VIÊN]")
            r_pl.bold = True
            r_pl.font.color.rgb = RGBColor(204, 0, 0)
    
    doc.save(output_path)
    print(f"Báo cáo Word đã được lưu tại: {output_path}")

if __name__ == "__main__":
    out_file = "/Users/langpn/workspace/school/phat-trien-he-thong-tich-hop/pthtth_21bvl_25765251/kiem_tra_thuong_ky/25765251_PhamNgocLang_baocao.docx"
    create_report(out_file)
