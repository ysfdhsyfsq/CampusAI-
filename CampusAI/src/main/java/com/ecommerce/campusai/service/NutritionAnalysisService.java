package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.Dish;
import com.ecommerce.campusai.entity.OrderItem;
import com.ecommerce.campusai.mapper.DishMapper;
import com.ecommerce.campusai.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NutritionAnalysisService {

    private final OrderItemMapper orderItemMapper;
    private final DishMapper dishMapper;
    private final AiChatService aiChatService;

    public NutritionAnalysisService(OrderItemMapper orderItemMapper,
                                    DishMapper dishMapper,
                                    AiChatService aiChatService) {
        this.orderItemMapper = orderItemMapper;
        this.dishMapper = dishMapper;
        this.aiChatService = aiChatService;
    }

    /**
     * 分析用户一周饮食营养
     */
    public Map<String, Object> analyzeWeeklyNutrition(Integer userId, LocalDate startDate) {
        List<OrderItem> weeklyOrders = getWeeklyOrders(userId, startDate);
        
        if (weeklyOrders.isEmpty()) {
            return createEmptyAnalysis();
        }

        Map<String, Object> analysis = new HashMap<>();
        
        Map<String, Object> nutritionStats = calculateNutritionStats(weeklyOrders);
        
        List<Map<String, String>> dailyBreakdown = analyzeDailyBreakdown(weeklyOrders, startDate);
        
        List<String> healthSuggestions = generateHealthSuggestions(nutritionStats, weeklyOrders);
        
        String aiReport = generateAiNutritionReport(nutritionStats, dailyBreakdown, healthSuggestions);
        
        analysis.put("userId", userId);
        analysis.put("weekStartDate", startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        analysis.put("weekEndDate", startDate.plusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        analysis.put("nutritionStats", nutritionStats);
        analysis.put("dailyBreakdown", dailyBreakdown);
        analysis.put("healthSuggestions", healthSuggestions);
        analysis.put("aiReport", aiReport);
        analysis.put("totalMeals", weeklyOrders.size());
        
        return analysis;
    }

    /**
     * 计算营养统计数据
     */
    private Map<String, Object> calculateNutritionStats(List<OrderItem> orders) {
        Map<String, Object> stats = new HashMap<>();
        
        int totalCalories = 0;
        int mealCount = orders.size();
        Map<String, Integer> categoryCount = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (OrderItem item : orders) {
            Dish dish = dishMapper.selectById(item.getDishId());
            if (dish != null) {
                int estimatedCalories = estimateCalories(dish);
                totalCalories += estimatedCalories * item.getQuantity();
                
                String category = categorizeDish(dish);
                categoryCount.merge(category, item.getQuantity(), Integer::sum);
                
                totalAmount = totalAmount.add(dish.getPrice().multiply(new BigDecimal(item.getQuantity())));
            }
        }
        
        int avgDailyCalories = totalCalories / 7;
        String balanceLevel = evaluateBalance(categoryCount);
        
        stats.put("totalCalories", totalCalories);
        stats.put("avgDailyCalories", avgDailyCalories);
        stats.put("mealCount", mealCount);
        stats.put("totalSpending", totalAmount);
        stats.put("categoryDistribution", categoryCount);
        stats.put("balanceLevel", balanceLevel);
        stats.put("calorieStatus", avgDailyCalories > 2500 ? "偏高" : avgDailyCalories < 1500 ? "偏低" : "适中");
        
        return stats;
    }

    /**
     * 估算菜品热量（简化版）
     */
    private int estimateCalories(Dish dish) {
        String name = dish.getName().toLowerCase();
        String desc = dish.getDescription() != null ? dish.getDescription().toLowerCase() : "";
        
        if (name.contains("米饭") || name.contains("面条")) {
            return 300;
        } else if (name.contains("鸡") || name.contains("肉")) {
            return 400;
        } else if (name.contains("蔬菜") || name.contains("青菜")) {
            return 150;
        } else if (name.contains("汤")) {
            return 200;
        } else if (desc.contains("油炸") || desc.contains("红烧")) {
            return 500;
        } else if (desc.contains("清蒸") || desc.contains("凉拌")) {
            return 250;
        }
        
        return 350;
    }

    /**
     * 菜品分类
     */
    private String categorizeDish(Dish dish) {
        String name = dish.getName().toLowerCase();
        String desc = dish.getDescription() != null ? dish.getDescription().toLowerCase() : "";
        
        if (name.contains("米饭") || name.contains("面条") || name.contains("馒头")) {
            return "主食";
        } else if (name.contains("鸡") || name.contains("鸭") || name.contains("鱼") || name.contains("肉")) {
            return "蛋白质";
        } else if (name.contains("蔬菜") || name.contains("青菜") || name.contains("豆腐")) {
            return "蔬菜";
        } else if (name.contains("汤")) {
            return "汤类";
        } else if (desc.contains("水果")) {
            return "水果";
        }
        
        return "其他";
    }

    /**
     * 评估营养均衡度
     */
    private String evaluateBalance(Map<String, Integer> categoryCount) {
        boolean hasMain = categoryCount.containsKey("主食");
        boolean hasProtein = categoryCount.containsKey("蛋白质");
        boolean hasVegetable = categoryCount.containsKey("蔬菜");
        
        if (hasMain && hasProtein && hasVegetable) {
            return "优秀";
        } else if ((hasMain && hasProtein) || (hasMain && hasVegetable)) {
            return "良好";
        } else if (hasMain) {
            return "一般";
        } else {
            return "需改进";
        }
    }

    /**
     * 分析每日饮食 breakdown
     */
    private List<Map<String, String>> analyzeDailyBreakdown(List<OrderItem> orders, LocalDate startDate) {
        Map<LocalDate, List<OrderItem>> dailyOrders = new HashMap<>();
        
        for (OrderItem order : orders) {
            dailyOrders.computeIfAbsent(startDate, k -> new ArrayList<>()).add(order);
        }
        
        List<Map<String, String>> breakdown = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            List<OrderItem> dayOrders = dailyOrders.getOrDefault(date, new ArrayList<>());
            
            Map<String, String> dayInfo = new HashMap<>();
            dayInfo.put("date", date.format(DateTimeFormatter.ofPattern("MM-dd")));
            dayInfo.put("meals", String.valueOf(dayOrders.size()));
            
            if (dayOrders.isEmpty()) {
                dayInfo.put("status", "无记录");
                dayInfo.put("summary", "今日暂无用餐记录");
            } else {
                dayInfo.put("status", "有记录");
                Set<String> categories = dayOrders.stream()
                        .map(item -> dishMapper.selectById(item.getDishId()))
                        .filter(Objects::nonNull)
                        .map(this::categorizeDish)
                        .collect(Collectors.toSet());
                dayInfo.put("summary", "摄入类别：" + String.join("、", categories));
            }
            
            breakdown.add(dayInfo);
        }
        
        return breakdown;
    }

    /**
     * 生成健康建议
     */
    private List<String> generateHealthSuggestions(Map<String, Object> stats, List<OrderItem> orders) {
        List<String> suggestions = new ArrayList<>();
        
        int avgCalories = (int) stats.get("avgDailyCalories");
        if (avgCalories > 2500) {
            suggestions.add("⚠️ 日均热量摄入偏高，建议适当减少高热量食物，增加运动量");
        } else if (avgCalories < 1500) {
            suggestions.add("⚠️ 日均热量摄入偏低，建议增加营养丰富的食物摄入");
        }
        
        Map<String, Integer> categoryDist = (Map<String, Integer>) stats.get("categoryDistribution");
        if (!categoryDist.containsKey("蔬菜") || categoryDist.getOrDefault("蔬菜", 0) < 5) {
            suggestions.add("🥗 蔬菜摄入不足，建议每天至少食用一份蔬菜");
        }
        if (!categoryDist.containsKey("蛋白质") || categoryDist.getOrDefault("蛋白质", 0) < 5) {
            suggestions.add("🍗 蛋白质摄入不足，建议增加鱼类、鸡肉等优质蛋白");
        }
        
        int mealCount = (int) stats.get("mealCount");
        if (mealCount < 14) {
            suggestions.add("🍽️ 本周用餐次数较少，建议规律三餐，避免暴饮暴食");
        }
        
        String balance = (String) stats.get("balanceLevel");
        if ("需改进".equals(balance) || "一般".equals(balance)) {
            suggestions.add("📊 营养搭配需要改进，建议主食、蛋白质、蔬菜均衡搭配");
        }
        
        if (suggestions.isEmpty()) {
            suggestions.add("✅ 本周饮食状况良好，继续保持健康的饮食习惯！");
        }
        
        return suggestions;
    }

    /**
     * 生成AI营养报告
     */
    private String generateAiNutritionReport(Map<String, Object> stats, 
                                             List<Map<String, String>> dailyBreakdown,
                                             List<String> suggestions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("作为CampusAI智慧校园的营养健康顾问，请根据以下数据生成一份简洁的营养分析报告：\n\n");
        prompt.append("【基本数据】\n");
        prompt.append("- 总热量：").append(stats.get("totalCalories")).append("千卡\n");
        prompt.append("- 日均热量：").append(stats.get("avgDailyCalories")).append("千卡\n");
        prompt.append("- 用餐次数：").append(stats.get("mealCount")).append("次\n");
        prompt.append("- 营养均衡度：").append(stats.get("balanceLevel")).append("\n");
        prompt.append("- 热量状态：").append(stats.get("calorieStatus")).append("\n\n");
        
        prompt.append("【类别分布】\n");
        Map<String, Integer> categoryDist = (Map<String, Integer>) stats.get("categoryDistribution");
        categoryDist.forEach((k, v) -> prompt.append("- ").append(k).append("：").append(v).append("次\n"));
        prompt.append("\n");
        
        prompt.append("【健康建议】\n");
        suggestions.forEach(s -> prompt.append("- ").append(s).append("\n"));
        
        prompt.append("\n请用温暖专业的语气，生成150字以内的总结报告，包含：\n");
        prompt.append("1. 整体评价\n");
        prompt.append("2. 亮点表扬\n");
        prompt.append("3. 改进方向\n");
        prompt.append("4. 鼓励话语");
        
        return aiChatService.chat(prompt.toString());
    }

    /**
     * 获取一周订单
     */
    private List<OrderItem> getWeeklyOrders(Integer userId, LocalDate startDate) {
        return new ArrayList<>();
    }

    /**
     * 创建空的分析结果
     */
    private Map<String, Object> createEmptyAnalysis() {
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("message", "本周暂无用餐记录，快去食堂品尝美食吧！");
        analysis.put("nutritionStats", new HashMap<>());
        analysis.put("dailyBreakdown", new ArrayList<>());
        analysis.put("healthSuggestions", Arrays.asList("建议规律用餐，保持健康饮食习惯"));
        analysis.put("aiReport", "本周还没有用餐记录哦~ 记得按时吃饭，保持身体健康！");
        return analysis;
    }
}
