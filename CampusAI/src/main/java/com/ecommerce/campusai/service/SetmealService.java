package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.Setmeal;
import com.ecommerce.campusai.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Cacheable(value = "setmealList")
    public List<Setmeal> findAll() {
        return setmealMapper.findAll();
    }

    public Setmeal findById(Integer id) {
        return setmealMapper.findById(id);
    }

    @CacheEvict(value = "setmealList", allEntries = true)
    public int add(Setmeal setmeal) {
        if (setmeal.getStatus() == null) {
            setmeal.setStatus(0);
        }
        return setmealMapper.add(setmeal);
    }

    @CacheEvict(value = "setmealList", allEntries = true)
    public int update(Setmeal setmeal) {
        return setmealMapper.update(setmeal);
    }

    @CacheEvict(value = "setmealList", allEntries = true)
    public int updateStatus(Integer id, Integer status) {
        return setmealMapper.updateStatus(id, status);
    }

    @CacheEvict(value = "setmealList", allEntries = true)
    public int delete(Integer id) {
        return setmealMapper.delete(id);
    }
}