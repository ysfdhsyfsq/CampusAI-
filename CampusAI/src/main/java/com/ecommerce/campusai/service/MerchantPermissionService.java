package com.ecommerce.campusai.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MerchantPermissionService {

    // 获取当前登录商家名
    public String getCurrentMerchant() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // 校验：只能操作自己的数据
    public boolean isOwner(String dataMerchantName) {
        return getCurrentMerchant().equals(dataMerchantName);
    }
}