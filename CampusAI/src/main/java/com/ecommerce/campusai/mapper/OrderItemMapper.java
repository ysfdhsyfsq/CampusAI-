package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.OrderItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OrderItemMapper {

    // 添加订单明细
    @Insert("INSERT INTO order_item(order_id,dish_id,dish_name,price,quantity) " +
            "VALUES(#{orderId},#{dishId},#{dishName},#{price},#{quantity})")
    int insert(OrderItem item);

    // 根据订单ID查明细
    @Select("SELECT * FROM order_item WHERE order_id = #{orderId}")
    List<OrderItem> findByOrderId(Integer orderId);
}