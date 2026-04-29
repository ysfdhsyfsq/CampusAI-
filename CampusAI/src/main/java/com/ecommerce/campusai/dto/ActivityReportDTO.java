package com.ecommerce.campusai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityReportDTO {
    private String activityTitle;
    private Integer maxNum;
    private Integer enrollNum;
    private BigDecimal enrollRate;
    private String endTime;
    private String status;
}
