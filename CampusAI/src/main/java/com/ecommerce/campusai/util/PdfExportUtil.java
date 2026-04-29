package com.ecommerce.campusai.util;

import com.ecommerce.campusai.dto.ActivityReportDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExportUtil {

    private static final Logger log = LoggerFactory.getLogger(PdfExportUtil.class);

    public void exportActivityReport(HttpServletResponse response, List<ActivityReportDTO> data) throws Exception {
        log.info("========== 开始导出PDF报表 ==========");
        log.info("数据条数：{}", data.size());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "活动报名统计报表");
        parameters.put("exportTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        parameters.put("totalCount", data.size());
        parameters.put(net.sf.jasperreports.engine.JRParameter.REPORT_LOCALE, java.util.Locale.CHINA);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
        
        InputStream jrxmlStream = getClass().getClassLoader().getResourceAsStream("jasper/activity-report.jrxml");
        
        if (jrxmlStream == null) {
            throw new RuntimeException("找不到报表模板文件：jasper/activity-report.jrxml");
        }

        log.info("步骤1：编译报表模板...");
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
        
        log.info("开始填充数据...");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        
        int pageCount = jasperPrint.getPages().size();
        log.info("数据填充完成，生成页面数量：{}", pageCount);

        log.info("导出PDF...");
        response.setContentType("application/pdf");
        String fileName = URLEncoder.encode("活动报名统计报表.pdf", StandardCharsets.UTF_8)
            .replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);
        response.setCharacterEncoding("UTF-8");

        try (OutputStream out = response.getOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            log.info("PDF导出成功");
            log.info("========== PDF导出完成 ==========");
        }
    }
}
