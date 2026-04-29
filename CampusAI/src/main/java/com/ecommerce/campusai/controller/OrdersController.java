package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.Orders;
import com.ecommerce.campusai.service.MerchantPermissionService;
import com.ecommerce.campusai.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    
    @Autowired
    private MerchantPermissionService permissionService;

    @PostMapping("/create")
    public String create(@RequestBody Orders orders){
        if (orders.getMerchantName() == null || orders.getMerchantName().isEmpty()) {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            
            if (role.contains("MERCHANT")) {
                orders.setMerchantName(currentUser);
            } else {
                orders.setMerchantName("校园食堂");
            }
        }
        int i = ordersService.create(orders);
        return i>0 ? "订单创建成功" : "订单创建失败";
    }

    @GetMapping("/list")
    public List<Orders> list(){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        
        List<Orders> allOrders = ordersService.findAll();
        
        if (role.contains("MERCHANT")) {
            return allOrders.stream()
                    .filter(order -> permissionService.isOwner(order.getMerchantName()))
                    .collect(Collectors.toList());
        }
        
        return allOrders;
    }

    @GetMapping("/detail/{orderNo}")
    public Orders detail(@PathVariable String orderNo){
        Orders order = ordersService.findByOrderNo(orderNo);
        
        if (order != null) {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
            
            if (role.contains("MERCHANT") && !permissionService.isOwner(order.getMerchantName())) {
                return null;
            }
        }
        
        return order;
    }
}