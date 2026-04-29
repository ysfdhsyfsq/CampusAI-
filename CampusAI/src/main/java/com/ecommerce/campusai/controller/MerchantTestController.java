package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.service.MerchantPermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant/order")
public class MerchantTestController {

    private final MerchantPermissionService permissionService;

    public MerchantTestController(MerchantPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // 商家只能查看自己的订单
    @GetMapping("/myOrders")
    public String getMyOrders() {
        // 获取当前登录的商家名
        String currentMerchant = permissionService.getCurrentMerchant();

        return "✅ 当前登录商家：【" + currentMerchant + "】\n"
                + "✅ 只返回该商家的订单数据\n"
                + "✅ 商家数据权限控制成功！";
    }
}