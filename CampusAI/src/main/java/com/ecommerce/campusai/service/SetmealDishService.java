package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.SetmealDish;
import com.ecommerce.campusai.mapper.SetmealDishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SetmealDishService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    public int add(SetmealDish setmealDish) {
        return setmealDishMapper.add(setmealDish);
    }

    public int batchAdd(List<SetmealDish> list) {
        if (list != null && !list.isEmpty()) {
            return setmealDishMapper.batchAdd(list);
        }
        return 0;
    }

    public List<SetmealDish> findBySetmealId(Integer setmealId) {
        return setmealDishMapper.findBySetmealId(setmealId);
    }

    public int delete(Integer setmealId, Integer dishId) {
        return setmealDishMapper.delete(setmealId, dishId);
    }

    public int deleteBySetmealId(Integer setmealId) {
        return setmealDishMapper.deleteBySetmealId(setmealId);
    }
}