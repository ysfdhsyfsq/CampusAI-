package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.Setmeal;
import com.ecommerce.campusai.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<Setmeal> list = setmealService.findAll();
        result.put("code", 200);
        result.put("data", list);
        result.put("msg", "查询成功");
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        Setmeal setmeal = setmealService.findById(id);
        if (setmeal != null) {
            result.put("code", 200);
            result.put("data", setmeal);
            result.put("msg", "查询成功");
        } else {
            result.put("code", 404);
            result.put("msg", "套餐不存在");
        }
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Setmeal setmeal) {
        Map<String, Object> result = new HashMap<>();
        int rows = setmealService.add(setmeal);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "添加成功");
            result.put("data", setmeal.getId());
        } else {
            result.put("code", 500);
            result.put("msg", "添加失败");
        }
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody Setmeal setmeal) {
        Map<String, Object> result = new HashMap<>();
        int rows = setmealService.update(setmeal);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "更新成功");
        } else {
            result.put("code", 500);
            result.put("msg", "更新失败");
        }
        return result;
    }

    @PostMapping("/status/{id}/{status}")
    public Map<String, Object> updateStatus(@PathVariable Integer id, @PathVariable Integer status) {
        Map<String, Object> result = new HashMap<>();
        int rows = setmealService.updateStatus(id, status);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", status == 1 ? "上架成功" : "下架成功");
        } else {
            result.put("code", 500);
            result.put("msg", "操作失败");
        }
        return result;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        int rows = setmealService.delete(id);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "删除成功");
        } else {
            result.put("code", 500);
            result.put("msg", "删除失败");
        }
        return result;
    }
}