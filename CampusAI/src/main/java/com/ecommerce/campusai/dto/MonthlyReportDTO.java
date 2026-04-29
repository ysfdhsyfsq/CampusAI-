package com.ecommerce.campusai.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlyReportDTO {
    private String merchantName;
    private Integer orderCount;
    private BigDecimal totalRevenue;
    private BigDecimal refundRate;
    private BigDecimal avgOrderAmount;
}
