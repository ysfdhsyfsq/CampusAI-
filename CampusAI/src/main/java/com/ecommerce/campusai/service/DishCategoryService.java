package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.DishCategory;
import com.ecommerce.campusai.mapper.DishCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DishCategoryService {

    @Autowired
    private DishCategoryMapper dishCategoryMapper;

    public List<DishCategory> findAll(){
        return dishCategoryMapper.findAll();
    }

    public DishCategory findById(Integer id){
        return dishCategoryMapper.findById(id);
    }

    public int add(DishCategory category){
        return dishCategoryMapper.add(category);
    }

    public int update(DishCategory category){
        return dishCategoryMapper.update(category);
    }

    public int delete(Integer id){
        return dishCategoryMapper.delete(id);
    }
}