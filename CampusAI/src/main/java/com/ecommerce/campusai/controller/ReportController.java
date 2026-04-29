package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.mapper.ActivityMapper;
import com.ecommerce.campusai.mapper.OrdersMapper;
import com.ecommerce.campusai.util.ExcelExportUtil;
import com.ecommerce.campusai.util.PdfExportUtil;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final OrdersMapper ordersMapper;
    private final ActivityMapper activityMapper;
    private final ExcelExportUtil excelExportUtil;
    private final PdfExportUtil pdfExportUtil;

    public ReportController(OrdersMapper ordersMapper, ActivityMapper activityMapper,
                           ExcelExportUtil excelExportUtil, PdfExportUtil pdfExportUtil) {
        this.ordersMapper = ordersMapper;
        this.activityMapper = activityMapper;
        this.excelExportUtil = excelExportUtil;
        this.pdfExportUtil = pdfExportUtil;
    }

    @GetMapping("/excel/monthly")
    public void exportMonthlyReport(HttpServletResponse response, 
                                    @RequestParam(defaultValue = "2026-04") String month) throws Exception {
        List<Map<String, Object>> data = ordersMapper.getMonthlyReport(month);
        excelExportUtil.exportMonthlyReport(response, data, month);
    }

    @GetMapping("/pdf/activity")
    public void exportActivityReport(HttpServletResponse response) throws Exception {
        var data = activityMapper.getActivityReportDTO();
        pdfExportUtil.exportActivityReport(response, data);
    }

    @GetMapping("/preview/monthly")
    public List<Map<String, Object>> previewMonthlyReport(@RequestParam(defaultValue = "2026-04") String month) {
        return ordersMapper.getMonthlyReport(month);
    }

    @GetMapping("/preview/activity")
    public List<Map<String, Object>> previewActivityReport() {
        return activityMapper.getActivityReport();
    }
}
