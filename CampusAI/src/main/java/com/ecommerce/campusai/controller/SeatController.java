package com.ecommerce.campusai.controller;
import com.ecommerce.campusai.entity.StudyRoomSeat;
import com.ecommerce.campusai.service.StudyRoomSeatService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/seat")
public class SeatController {
    private final StudyRoomSeatService service;

    public SeatController(StudyRoomSeatService service) {
        this.service = service;
    }

    @GetMapping("/template")
    public void template(HttpServletResponse response) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("座位");
        Row r = sheet.createRow(0);
        r.createCell(0).setCellValue("roomId");
        r.createCell(1).setCellValue("seatCode");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode("座位模板.xlsx", "UTF-8"));
        wb.write(response.getOutputStream());
        wb.close();
    }

    @PostMapping("/import")
    public String importSeat(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "请选择文件";
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            return "请上传Excel文件（.xlsx或.xls）";
        }

        try {
            int count = service.importSeats(file);
            return "导入成功，共导入" + count + "条座位数据";
        } catch (Exception e) {
            e.printStackTrace();
            return "导入失败：" + e.getMessage();
        }
    }

    @GetMapping("/list")
    public List<StudyRoomSeat> list() {
        return service.list();
    }

    @GetMapping("/list/{roomId}")
    public List<StudyRoomSeat> listByRoom(@PathVariable Integer roomId) {
        return service.listSeatsByRoom(roomId);
    }
}