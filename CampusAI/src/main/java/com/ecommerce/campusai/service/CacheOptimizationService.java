package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.Dish;
import com.ecommerce.campusai.entity.Setmeal;
import com.ecommerce.campusai.mapper.DishMapper;
import com.ecommerce.campusai.mapper.SetmealMapper;
import com.ecommerce.campusai.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CacheOptimizationService {
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Autowired
    private DishMapper dishMapper;
    
    @Autowired
    private SetmealMapper setmealMapper;
    
    private static final String HOT_DISHES_KEY = "cache:hot:dishes";
    private static final String HOT_SETMEALS_KEY = "cache:hot:setmeals";
    private static final String HOME_RECOMMEND_KEY = "cache:home:recommend";
    private static final long CACHE_EXPIRE_HOURS = 24;
    
    public List<Dish> getHotDishes() {
        String cached = redisUtil.get(HOT_DISHES_KEY);
        if (cached != null) {
            System.out.println("从缓存获取热门菜品");
            return parseDishList(cached);
        }
        
        List<Dish> hotDishes = dishMapper.selectAll();
        redisUtil.set(HOT_DISHES_KEY, serializeDishList(hotDishes), CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        return hotDishes;
    }
    
    public List<Setmeal> getHotSetmeals() {
        String cached = redisUtil.get(HOT_SETMEALS_KEY);
        if (cached != null) {
            System.out.println("从缓存获取热门套餐");
            return parseSetmealList(cached);
        }
        
        List<Setmeal> hotSetmeals = setmealMapper.findAll();
        redisUtil.set(HOT_SETMEALS_KEY, serializeSetmealList(hotSetmeals), CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        return hotSetmeals;
    }
    
    public Object getHomeRecommend() {
        String cached = redisUtil.get(HOME_RECOMMEND_KEY);
        if (cached != null) {
            System.out.println("从缓存获取首页推荐");
            return cached;
        }
        
        Object recommend = buildHomeRecommend();
        redisUtil.set(HOME_RECOMMEND_KEY, recommend.toString(), CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        return recommend;
    }
    
    public void clearHotDishesCache() {
        redisUtil.delete(HOT_DISHES_KEY);
    }
    
    public void clearHotSetmealsCache() {
        redisUtil.delete(HOT_SETMEALS_KEY);
    }
    
    public void clearHomeRecommendCache() {
        redisUtil.delete(HOME_RECOMMEND_KEY);
    }
    
    public long getCacheHitRate() {
        Long keyspaceHits = redisUtil.getKeyspaceHits();
        Long keyspaceMisses = redisUtil.getKeyspaceMisses();
        if (keyspaceHits == null || keyspaceMisses == null) {
            return 0;
        }
        long total = keyspaceHits + keyspaceMisses;
        return total > 0 ? (keyspaceHits * 100 / total) : 0;
    }
    
    private Object buildHomeRecommend() {
        return "首页推荐数据";
    }
    
    private String serializeDishList(List<Dish> dishes) {
        return dishes.toString();
    }
    
    private List<Dish> parseDishList(String data) {
        return null;
    }
    
    private String serializeSetmealList(List<Setmeal> setmeals) {
        return setmeals.toString();
    }
    
    private List<Setmeal> parseSetmealList(String data) {
        return null;
    }
}
