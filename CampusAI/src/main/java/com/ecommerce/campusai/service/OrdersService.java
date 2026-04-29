package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.Orders;
import com.ecommerce.campusai.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    public int create(Orders orders){
        return ordersMapper.insert(orders);
    }

    public Orders findByOrderNo(String orderNo){
        return ordersMapper.findByOrderNo(orderNo);
    }

    public List<Orders> findAll(){
        return ordersMapper.findAll();
    }
}