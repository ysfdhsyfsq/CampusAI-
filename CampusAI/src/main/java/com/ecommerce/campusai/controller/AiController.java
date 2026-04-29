package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.service.AiChatService;
import com.ecommerce.campusai.service.DishRecommendationService;
import com.ecommerce.campusai.service.NutritionAnalysisService;
import com.ecommerce.campusai.service.ScheduleConflictService;
import com.ecommerce.campusai.service.ActivitySummaryService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiChatService aiChatService;
    private final DishRecommendationService dishRecommendationService;
    private final NutritionAnalysisService nutritionAnalysisService;
    private final ScheduleConflictService scheduleConflictService;
    private final ActivitySummaryService activitySummaryService;

    public AiController(AiChatService aiChatService,
                        DishRecommendationService dishRecommendationService,
                        NutritionAnalysisService nutritionAnalysisService,
                        ScheduleConflictService scheduleConflictService,
                        ActivitySummaryService activitySummaryService) {
        this.aiChatService = aiChatService;
        this.dishRecommendationService = dishRecommendationService;
        this.nutritionAnalysisService = nutritionAnalysisService;
        this.scheduleConflictService = scheduleConflictService;
        this.activitySummaryService = activitySummaryService;
    }

    @RequestMapping(value = "/chat", method = {RequestMethod.GET, RequestMethod.POST})
    public String handleChat(@RequestParam String question) {
        return aiChatService.chat(question);
    }

    @PostMapping("/recommend/dish")
    public Map<String, Object> recommendDish(@RequestBody Map<String, Object> preferences) {
        Integer userId = (Integer) preferences.get("userId");
        String taste = (String) preferences.get("taste");
        Double budget = preferences.get("budget") != null ? 
                Double.parseDouble(preferences.get("budget").toString()) : null;
        
        return dishRecommendationService.recommendDishes(userId, taste, budget);
    }

    @PostMapping("/analyze/nutrition")
    public Map<String, Object> analyzeNutrition(@RequestBody Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        String startDateStr = (String) params.get("startDate");
        
        LocalDate startDate = startDateStr != null ? 
                LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")) :
                LocalDate.now().minusDays(6);
        
        return nutritionAnalysisService.analyzeWeeklyNutrition(userId, startDate);
    }

    @PostMapping("/check/schedule-conflict")
    public Map<String, Object> checkScheduleConflict(@RequestBody Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        Integer seatId = (Integer) params.get("seatId");
        String startTimeStr = (String) params.get("startTime");
        String endTimeStr = (String) params.get("endTime");
        
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        return scheduleConflictService.checkScheduleConflict(userId, seatId, startTime, endTime);
    }

    @GetMapping("/recommend/slots")
    public Map<String, Object> recommendAvailableSlots(@RequestParam Integer userId,
                                                        @RequestParam String dayOfWeek) {
        java.time.DayOfWeek day = java.time.DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        java.util.List<Map<String, Object>> slots = 
                scheduleConflictService.recommendAvailableSlots(userId, day);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("dayOfWeek", dayOfWeek);
        result.put("availableSlots", slots);
        result.put("count", slots.size());
        
        return result;
    }

    @RequestMapping(value = "/summarize/activity", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> summarizeActivity(@RequestParam Integer activityId) {
        return activitySummaryService.generateActivitySummary(activityId);
    }

    @PostMapping("/summarize/activity/batch")
    public Map<String, Object> batchSummarizeActivities(@RequestBody java.util.List<Integer> activityIds) {
        return activitySummaryService.batchGenerateSummaries(activityIds);
    }

    @PostMapping("/learn/preference")
    public Map<String, Object> learnUserPreference(@RequestParam Integer userId) {
        dishRecommendationService.learnUserPreference(userId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("message", "口味偏好学习完成");
        result.put("userId", userId);
        return result;
    }
}
