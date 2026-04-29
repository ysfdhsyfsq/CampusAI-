package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.service.ZookeeperConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/config")
public class ZookeeperConfigController {
    
    @Autowired
    private ZookeeperConfigService configService;
    
    @PostMapping("/set")
    public Map<String, Object> setConfig(@RequestParam String key, @RequestParam String value) {
        Map<String, Object> result = new HashMap<>();
        try {
            configService.setConfig(key, value);
            result.put("code", 200);
            result.put("msg", "配置设置成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "配置设置失败: " + e.getMessage());
        }
        return result;
    }
    
    @GetMapping("/get")
    public Map<String, Object> getConfig(@RequestParam String key) {
        Map<String, Object> result = new HashMap<>();
        try {
            String value = configService.getConfig(key);
            result.put("code", 200);
            result.put("data", value);
            result.put("msg", "查询成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败: " + e.getMessage());
        }
        return result;
    }
    
    @DeleteMapping("/delete")
    public Map<String, Object> deleteConfig(@RequestParam String key) {
        Map<String, Object> result = new HashMap<>();
        try {
            configService.deleteConfig(key);
            result.put("code", 200);
            result.put("msg", "配置删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "配置删除失败: " + e.getMessage());
        }
        return result;
    }
}
