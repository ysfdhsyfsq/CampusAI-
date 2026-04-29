package com.ecommerce.campusai.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class StatisticsVO {
    private List<Map<String, Object>> userTrend;
    private List<Map<String, Object>> orderDistribution;
    private List<Map<String, Object>> roomUsage;
    private List<Map<String, Object>> activityRanking;
    private Map<String, Object> realTimeStats;
}
