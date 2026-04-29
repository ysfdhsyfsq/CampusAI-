package com.ecommerce.campusai.service;


import com.ecommerce.campusai.entity.Dish;
import com.ecommerce.campusai.entity.OrderItem;
import com.ecommerce.campusai.mapper.DishMapper;
import com.ecommerce.campusai.mapper.OrderItemMapper;
import com.ecommerce.campusai.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DishRecommendationService {

    private final OrderItemMapper orderItemMapper;
    private final OrdersMapper ordersMapper;
    private final DishMapper dishMapper;
    private final AiChatService aiChatService;

    public DishRecommendationService(OrderItemMapper orderItemMapper, 
                                     OrdersMapper ordersMapper,
                                     DishMapper dishMapper,
                                     AiChatService aiChatService) {
        this.orderItemMapper = orderItemMapper;
        this.ordersMapper = ordersMapper;
        this.dishMapper = dishMapper;
        this.aiChatService = aiChatService;
    }

    /**
     * 基于用户历史订单的智能推荐
     */
    public Map<String, Object> recommendDishes(Integer userId, String tastePreference, Double budget) {
        List<OrderItem> historyItems = getUserOrderHistory(userId);
        
        if (historyItems.isEmpty()) {
            return coldStartRecommendation(tastePreference, budget);
        }

        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> recommendedDishes = collaborativeFiltering(historyItems, tastePreference, budget);
        
        String aiRecommendation = generateAiRecommendation(historyItems, tastePreference, budget);
        
        result.put("recommendedDishes", recommendedDishes);
        result.put("aiSuggestion", aiRecommendation);
        result.put("basedOnHistory", historyItems.size() + "条历史订单");
        
        return result;
    }

    /**
     * 协同过滤算法实现
     */
    private List<Map<String, Object>> collaborativeFiltering(List<OrderItem> historyItems, 
                                                              String tastePreference, 
                                                              Double budget) {
        Map<Integer, Integer> dishFrequency = new HashMap<>();
        for (OrderItem item : historyItems) {
            dishFrequency.merge(item.getDishId(), item.getQuantity(), Integer::sum);
        }

        List<Integer> frequentDishIds = dishFrequency.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Set<Integer> categoryIds = new HashSet<>();
        for (Integer dishId : frequentDishIds) {
            Dish dish = dishMapper.selectById(dishId);
            if (dish != null) {
                categoryIds.add(dish.getCategoryId());
            }
        }

        List<Dish> candidateDishes = new ArrayList<>();
        for (Integer categoryId : categoryIds) {
            List<Dish> dishes = dishMapper.selectByCategoryId(categoryId);
            candidateDishes.addAll(dishes);
        }

        candidateDishes.removeIf(dish -> 
            dish.getStatus() == 0 || 
            frequentDishIds.contains(dish.getId()) ||
            (budget != null && dish.getPrice().doubleValue() > budget)
        );

        candidateDishes.sort((d1, d2) -> {
            double score1 = calculateDishScore(d1, tastePreference);
            double score2 = calculateDishScore(d2, tastePreference);
            return Double.compare(score2, score1);
        });

        return candidateDishes.stream()
                .limit(5)
                .map(dish -> {
                    Map<String, Object> dishInfo = new HashMap<>();
                    dishInfo.put("id", dish.getId());
                    dishInfo.put("name", dish.getName());
                    dishInfo.put("price", dish.getPrice());
                    dishInfo.put("description", dish.getDescription());
                    dishInfo.put("image", dish.getImage());
                    dishInfo.put("matchScore", calculateDishScore(dish, tastePreference));
                    return dishInfo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 计算菜品匹配分数
     */
    private double calculateDishScore(Dish dish, String tastePreference) {
        double score = Math.random() * 30;
        
        if (tastePreference != null && dish.getDescription() != null) {
            String desc = dish.getDescription().toLowerCase();
            if (tastePreference.contains("辣") && desc.contains("辣")) {
                score += 20;
            }
            if (tastePreference.contains("甜") && desc.contains("甜")) {
                score += 20;
            }
            if (tastePreference.contains("清淡") && (desc.contains("清") || desc.contains("淡"))) {
                score += 20;
            }
        }
        
        score += 50 / (dish.getPrice().doubleValue() + 1);
        
        return score;
    }

    /**
     * 冷启动推荐（新用户）
     */
    private Map<String, Object> coldStartRecommendation(String tastePreference, Double budget) {
        List<Dish> allDishes = dishMapper.selectAll();
        
        if (tastePreference != null) {
            allDishes = allDishes.stream()
                    .filter(dish -> dish.getStatus() == 1)
                    .filter(dish -> budget == null || dish.getPrice().doubleValue() <= budget)
                    .sorted((d1, d2) -> Double.compare(
                            calculateDishScore(d2, tastePreference),
                            calculateDishScore(d1, tastePreference)))
                    .limit(5)
                    .collect(Collectors.toList());
        } else {
            allDishes = allDishes.stream()
                    .filter(dish -> dish.getStatus() == 1)
                    .filter(dish -> budget == null || dish.getPrice().doubleValue() <= budget)
                    .limit(5)
                    .collect(Collectors.toList());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("recommendedDishes", allDishes.stream()
                .map(dish -> {
                    Map<String, Object> dishInfo = new HashMap<>();
                    dishInfo.put("id", dish.getId());
                    dishInfo.put("name", dish.getName());
                    dishInfo.put("price", dish.getPrice());
                    dishInfo.put("description", dish.getDescription());
                    dishInfo.put("image", dish.getImage());
                    return dishInfo;
                })
                .collect(Collectors.toList()));
        result.put("aiSuggestion", "欢迎使用CampusAI！根据您的偏好为您推荐热门菜品。尝试下单后，我们会为您提供更精准的个性化推荐哦~");
        result.put("isNewUser", true);
        
        return result;
    }

    /**
     * 获取用户历史订单
     */
    private List<OrderItem> getUserOrderHistory(Integer userId) {
        List<OrderItem> allItems = new ArrayList<>();
        
        return allItems;
    }

    /**
     * 生成AI推荐建议
     */
    private String generateAiRecommendation(List<OrderItem> historyItems, String tastePreference, Double budget) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("作为CampusAI智慧校园食堂的智能营养师，请根据以下信息给出菜品推荐建议：\n");
        prompt.append("用户口味偏好：").append(tastePreference != null ? tastePreference : "未设置").append("\n");
        prompt.append("预算范围：").append(budget != null ? budget + "元" : "不限").append("\n");
        prompt.append("历史点餐次数：").append(historyItems.size()).append("次\n");
        
        if (!historyItems.isEmpty()) {
            prompt.append("常点菜品类型：");
            Map<String, Long> dishTypeCount = historyItems.stream()
                    .collect(Collectors.groupingBy(OrderItem::getDishName, Collectors.counting()));
            dishTypeCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(3)
                    .forEach(entry -> prompt.append(entry.getKey()).append("(").append(entry.getValue()).append("次), "));
            prompt.append("\n");
        }
        
        prompt.append("请给出温馨的推荐建议，包含：\n");
        prompt.append("1. 推荐尝试的新口味\n");
        prompt.append("2. 营养搭配建议\n");
        prompt.append("3. 鼓励性话语\n");
        prompt.append("控制在100字以内，语气亲切友好。");
        
        return aiChatService.chat(prompt.toString());
    }

    /**
     * 学习用户口味偏好
     */
    public void learnUserPreference(Integer userId) {
        List<OrderItem> historyItems = getUserOrderHistory(userId);
        
        if (historyItems.isEmpty()) {
            return;
        }

        Map<String, Integer> keywordCount = new HashMap<>();
        for (OrderItem item : historyItems) {
            Dish dish = dishMapper.selectById(item.getDishId());
            if (dish != null && dish.getDescription() != null) {
                String desc = dish.getDescription().toLowerCase();
                if (desc.contains("辣")) {
                    keywordCount.merge("辣", item.getQuantity(), Integer::sum);
                }
                if (desc.contains("甜")) {
                    keywordCount.merge("甜", item.getQuantity(), Integer::sum);
                }
                if (desc.contains("酸")) {
                    keywordCount.merge("酸", item.getQuantity(), Integer::sum);
                }
                if (desc.contains("清淡") || desc.contains("清蒸")) {
                    keywordCount.merge("清淡", item.getQuantity(), Integer::sum);
                }
            }
        }

        String topPreference = keywordCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("均衡");

        System.out.println("用户" + userId + "的口味偏好：" + topPreference);
    }
}
