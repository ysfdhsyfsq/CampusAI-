package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.DishCategory;
import com.ecommerce.campusai.service.DishCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class DishCategoryController {

    @Autowired
    private DishCategoryService dishCategoryService;

    @GetMapping("/list")
    public Map<String, Object> list(){
        Map<String, Object> result = new HashMap<>();
        List<DishCategory> categories = dishCategoryService.findAll();
        result.put("code", 200);
        result.put("data", categories);
        result.put("msg", "查询成功");
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id){
        Map<String, Object> result = new HashMap<>();
        DishCategory category = dishCategoryService.findById(id);
        if (category != null) {
            result.put("code", 200);
            result.put("data", category);
            result.put("msg", "查询成功");
        } else {
            result.put("code", 404);
            result.put("msg", "分类不存在");
        }
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody DishCategory category){
        Map<String, Object> result = new HashMap<>();
        int i = dishCategoryService.add(category);
        if (i > 0) {
            result.put("code", 200);
            result.put("msg", "添加成功");
        } else {
            result.put("code", 500);
            result.put("msg", "添加失败");
        }
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody DishCategory category){
        Map<String, Object> result = new HashMap<>();
        int i = dishCategoryService.update(category);
        if (i > 0) {
            result.put("code", 200);
            result.put("msg", "更新成功");
        } else {
            result.put("code", 500);
            result.put("msg", "更新失败");
        }
        return result;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id){
        Map<String, Object> result = new HashMap<>();
        int i = dishCategoryService.delete(id);
        if (i > 0) {
            result.put("code", 200);
            result.put("msg", "删除成功");
        } else {
            result.put("code", 500);
            result.put("msg", "删除失败");
        }
        return result;
    }
}