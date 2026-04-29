package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.Dish;
import com.ecommerce.campusai.service.DishService;
import com.ecommerce.campusai.service.MerchantPermissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    
    @Autowired
    private MerchantPermissionService permissionService;
    
    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) Integer categoryId,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        Map<String, Object> result = new HashMap<>();
        
        System.out.println("====== 菜品列表查询 ======");
        System.out.println("接收到的 pageNum: " + pageNum);
        System.out.println("接收到的 pageSize: " + pageSize);
        System.out.println("categoryId: " + categoryId);
        System.out.println("name: " + name);
        
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        
        try {
            if (role.contains("MERCHANT")) {
                PageHelper.startPage(pageNum, pageSize);
                List<Dish> list = dishService.findByMerchant(currentUser, categoryId, name);
                PageInfo<Dish> pageInfo = new PageInfo<>(list);
                
                System.out.println("商家角色，查询结果总数: " + pageInfo.getTotal());
                System.out.println("实际返回页码: " + pageInfo.getPageNum());
                System.out.println("返回数据条数: " + pageInfo.getList().size());
                
                result.put("code", 200);
                result.put("data", pageInfo.getList());
                result.put("total", pageInfo.getTotal());
                result.put("pageNum", pageInfo.getPageNum());
                result.put("pageSize", pageInfo.getPageSize());
                result.put("pages", pageInfo.getPages());
                result.put("msg", "查询成功");
            } else {
                PageHelper.startPage(pageNum, pageSize);
                List<Dish> list = dishService.findAll(categoryId, name);
                PageInfo<Dish> pageInfo = new PageInfo<>(list);
                
                System.out.println("普通用户角色，查询结果总数: " + pageInfo.getTotal());
                System.out.println("实际返回页码: " + pageInfo.getPageNum());
                System.out.println("返回数据条数: " + pageInfo.getList().size());
                
                result.put("code", 200);
                result.put("data", pageInfo.getList());
                result.put("total", pageInfo.getTotal());
                result.put("pageNum", pageInfo.getPageNum());
                result.put("pageSize", pageInfo.getPageSize());
                result.put("pages", pageInfo.getPages());
                result.put("msg", "查询成功");
            }
        } finally {
            PageHelper.clearPage();
        }
        
        System.out.println("========================");
        
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id){
        Map<String, Object> result = new HashMap<>();
        Dish dish = dishService.findById(id);
        if (dish != null) {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            
            if (role.contains("MERCHANT") && !permissionService.isOwner(dish.getMerchantName())) {
                result.put("code", 403);
                result.put("msg", "无权查看其他商家的菜品");
                return result;
            }
            
            result.put("code", 200);
            result.put("data", dish);
            result.put("msg", "查询成功");
        } else {
            result.put("code", 404);
            result.put("msg", "菜品不存在");
        }
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Dish dish){
        Map<String, Object> result = new HashMap<>();
        
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        dish.setMerchantName(currentUser);
        
        int rows = dishService.addDish(dish);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "添加成功");
        } else {
            result.put("code", 500);
            result.put("msg", "添加失败");
        }
        return result;
    }

    @PostMapping("/status/{id}/{status}")
    public Map<String, Object> status(@PathVariable Integer id, @PathVariable Integer status){
        Map<String, Object> result = new HashMap<>();
        
        Dish dish = dishService.findById(id);
        if (dish != null) {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            
            if (role.contains("MERCHANT") && !permissionService.isOwner(dish.getMerchantName())) {
                result.put("code", 403);
                result.put("msg", "无权操作其他商家的菜品");
                return result;
            }
        }
        
        int rows = dishService.updateStatus(id, status);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "状态更新成功");
        } else {
            result.put("code", 500);
            result.put("msg", "状态更新失败");
        }
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody Dish dish){
        Map<String, Object> result = new HashMap<>();
        
        Dish existingDish = dishService.findById(dish.getId());
        if (existingDish != null) {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            
            if (role.contains("MERCHANT") && !permissionService.isOwner(existingDish.getMerchantName())) {
                result.put("code", 403);
                result.put("msg", "无权修改其他商家的菜品");
                return result;
            }
        }
        
        int rows = dishService.updateDish(dish);
        if (rows > 0) {
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
        
        Dish dish = dishService.findById(id);
        if (dish == null) {
            result.put("code", 404);
            result.put("msg", "菜品不存在");
            return result;
        }
        
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        
        if (role.contains("MERCHANT") && !permissionService.isOwner(dish.getMerchantName())) {
            result.put("code", 403);
            result.put("msg", "无权删除其他商家的菜品");
            return result;
        }
        
        int rows = dishService.updateStatus(id, 0);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "菜品已下架（逻辑删除）");
        } else {
            result.put("code", 500);
            result.put("msg", "操作失败");
        }
        return result;
    }
}