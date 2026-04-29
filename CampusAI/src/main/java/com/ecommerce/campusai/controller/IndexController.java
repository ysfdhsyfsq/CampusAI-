package com.ecommerce.campusai.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexController {

    // ====================== 修复 404 关键！======================
    // 处理根路径 /，访问 http://localhost:8080 直接进入这里
    @GetMapping("/")
    public Map<String,Object> index() {
        return currentUser();
    }

    // 获取当前登录用户名
    @GetMapping("/currentUser")
    public Map<String,Object> currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        Map<String,Object> map = new HashMap<>();
        map.put("username",name);
        map.put("role",role);
        return map;
    }

    // 退出
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
}