package com.ecommerce.campusai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin/test")
    public String adminTest() {
        return "✅ 管理员页面：只有 admin 能访问";
    }

    @GetMapping("/merchant/test")
    public String merchantTest() {
        return "✅ 商家页面：只有 merchant 能访问";
    }

    @GetMapping("/user/test")
    public String userTest() {
        return "✅ 用户页面：只有 user 能访问";
    }
}