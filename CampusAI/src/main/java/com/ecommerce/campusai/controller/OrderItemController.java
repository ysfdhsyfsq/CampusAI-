package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.OrderItem;
import com.ecommerce.campusai.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orderItem")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/add")
    public String add(@RequestBody OrderItem item){
        int i = orderItemService.add(item);
        return i>0 ? "添加成功" : "添加失败";
    }

    @GetMapping("/list/{orderId}")
    public List<OrderItem> list(@PathVariable Integer orderId){
        return orderItemService.findByOrderId(orderId);
    }
}