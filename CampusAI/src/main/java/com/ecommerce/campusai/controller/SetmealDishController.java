package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.SetmealDish;
import com.ecommerce.campusai.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/setmealDish")
public class SetmealDishController {

    @Autowired
    private SetmealDishService setmealDishService;

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody SetmealDish setmealDish) {
        Map<String, Object> result = new HashMap<>();
        int rows = setmealDishService.add(setmealDish);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "绑定成功");
        } else {
            result.put("code", 500);
            result.put("msg", "绑定失败");
        }
        return result;
    }

    @PostMapping("/batchAdd")
    public Map<String, Object> batchAdd(@RequestBody List<SetmealDish> list) {
        Map<String, Object> result = new HashMap<>();
        int rows = setmealDishService.batchAdd(list);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "批量绑定成功");
        } else {
            result.put("code", 500);
            result.put("msg", "批量绑定失败");
        }
        return result;
    }

    @GetMapping("/list/{setmealId}")
    public Map<String, Object> list(@PathVariable Integer setmealId) {
        Map<String, Object> result = new HashMap<>();
        List<SetmealDish> list = setmealDishService.findBySetmealId(setmealId);
        result.put("code", 200);
        result.put("data", list);
        result.put("msg", "查询成功");
        return result;
    }

    @DeleteMapping("/delete/{setmealId}/{dishId}")
    public Map<String, Object> delete(@PathVariable Integer setmealId, @PathVariable Integer dishId) {
        Map<String, Object> result = new HashMap<>();
        int rows = setmealDishService.delete(setmealId, dishId);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "解绑成功");
        } else {
            result.put("code", 500);
            result.put("msg", "解绑失败");
        }
        return result;
    }

    @DeleteMapping("/deleteBySetmeal/{setmealId}")
    public Map<String, Object> deleteBySetmealId(@PathVariable Integer setmealId) {
        Map<String, Object> result = new HashMap<>();
        int rows = setmealDishService.deleteBySetmealId(setmealId);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "清空套餐菜品成功");
        } else {
            result.put("code", 500);
            result.put("msg", "操作失败");
        }
        return result;
    }
}