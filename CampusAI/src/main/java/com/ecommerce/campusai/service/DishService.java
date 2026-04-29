package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.Dish;
import com.ecommerce.campusai.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DishService {
    @Autowired
    private DishMapper dishMapper;

    // @Cacheable(value = "dishList")
    public List<Dish> findAll(Integer categoryId, String name){ 
        return dishMapper.findAll(categoryId, name); 
    }
    
    // @Cacheable(value = "merchantDishList", key = "#merchantName")
    public List<Dish> findByMerchant(String merchantName, Integer categoryId, String name) {
        return dishMapper.findByMerchant(merchantName, categoryId, name);
    }
    
    public Dish findById(Integer id){ 
        return dishMapper.findById(id); 
    }
    
    @CacheEvict(value = {"dishList", "merchantDishList"}, allEntries = true)
    public int addDish(Dish dish){ 
        if(dish.getStatus() == null) dish.setStatus(1); 
        return dishMapper.addDish(dish); 
    }
    
    @CacheEvict(value = {"dishList", "merchantDishList"}, allEntries = true)
    public int updateStatus(Integer id, Integer status){ 
        return dishMapper.updateStatus(id, status); 
    }
    
    @CacheEvict(value = {"dishList", "merchantDishList"}, allEntries = true)
    public int updateDish(Dish dish){ 
        return dishMapper.updateDish(dish); 
    }
    
    @CacheEvict(value = {"dishList", "merchantDishList"}, allEntries = true)
    public int deleteDish(Integer id){
        return dishMapper.deleteDish(id);
    }
}
