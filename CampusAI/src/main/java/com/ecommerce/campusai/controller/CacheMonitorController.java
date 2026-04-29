package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.service.CacheOptimizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/cache")
public class CacheMonitorController {
    
    @Autowired
    private CacheOptimizationService cacheService;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @GetMapping("/stats")
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        
        Properties info = redisTemplate.getConnectionFactory().getConnection().info();
        
        stats.put("used_memory", info.getProperty("used_memory_human"));
        stats.put("used_memory_peak", info.getProperty("used_memory_peak_human"));
        stats.put("total_connections_received", info.getProperty("total_connections_received"));
        stats.put("connected_clients", info.getProperty("connected_clients"));
        stats.put("keyspace_hits", info.getProperty("keyspace_hits"));
        stats.put("keyspace_misses", info.getProperty("keyspace_misses"));
        
        long hits = Long.parseLong(info.getProperty("keyspace_hits", "0"));
        long misses = Long.parseLong(info.getProperty("keyspace_misses", "0"));
        long total = hits + misses;
        double hitRate = total > 0 ? (double) hits / total * 100 : 0;
        
        stats.put("hit_rate", String.format("%.2f%%", hitRate));
        
        return stats;
    }
    
    @GetMapping("/clear/hot-dishes")
    public Map<String, Object> clearHotDishes() {
        Map<String, Object> result = new HashMap<>();
        cacheService.clearHotDishesCache();
        result.put("code", 200);
        result.put("msg", "热门菜品缓存已清除");
        return result;
    }
    
    @GetMapping("/clear/hot-setmeals")
    public Map<String, Object> clearHotSetmeals() {
        Map<String, Object> result = new HashMap<>();
        cacheService.clearHotSetmealsCache();
        result.put("code", 200);
        result.put("msg", "热门套餐缓存已清除");
        return result;
    }
    
    @GetMapping("/clear/home-recommend")
    public Map<String, Object> clearHomeRecommend() {
        Map<String, Object> result = new HashMap<>();
        cacheService.clearHomeRecommendCache();
        result.put("code", 200);
        result.put("msg", "首页推荐缓存已清除");
        return result;
    }
}
