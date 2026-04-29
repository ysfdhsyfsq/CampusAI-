package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {

    @Insert("INSERT INTO orders(order_no,total_amount,status,merchant_name) VALUES(#{orderNo},#{totalAmount},#{status},#{merchantName})")
    int insert(Orders orders);

    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Orders findByOrderNo(String orderNo);

    @Select("SELECT * FROM orders ORDER BY id DESC")
    List<Orders> findAll();

    @Select("SELECT COALESCE(merchant_name, '未知商家') as name, COUNT(*) as value FROM orders GROUP BY merchant_name ORDER BY value DESC")
    List<Map<String, Object>> getOrderDistribution();

    @Select("SELECT COUNT(*) as total, COALESCE(SUM(total_amount), 0) as totalAmount FROM orders")
    Map<String, Object> getOrderStats();

    @Select("SELECT " +
            "  merchant_name as merchantName, " +
            "  COUNT(*) as orderCount, " +
            "  COALESCE(SUM(total_amount), 0) as totalRevenue, " +
            "  COALESCE(SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 0) as refundRate, " +
            "  COALESCE(AVG(total_amount), 0) as avgOrderAmount " +
            "FROM orders " +
            "WHERE DATE_FORMAT(create_time, '%Y-%m') = #{month} " +
            "GROUP BY merchant_name " +
            "ORDER BY totalRevenue DESC")
    List<Map<String, Object>> getMonthlyReport(@Param("month") String month);
}