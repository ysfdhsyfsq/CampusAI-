package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.OrderItem;
import com.ecommerce.campusai.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    public int add(OrderItem item){
        return orderItemMapper.insert(item);
    }

    public List<OrderItem> findByOrderId(Integer orderId){
        return orderItemMapper.findByOrderId(orderId);
    }
}