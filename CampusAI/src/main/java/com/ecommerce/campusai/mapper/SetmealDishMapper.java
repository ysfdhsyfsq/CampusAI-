package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.SetmealDish;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SetmealDishMapper {

    @Insert("INSERT INTO setmeal_dish(setmeal_id, dish_id, quantity) " +
            "VALUES(#{setmealId}, #{dishId}, #{quantity})")
    int add(SetmealDish setmealDish);

    @Select("SELECT sd.*, d.name as dish_name, d.image as dish_image " +
            "FROM setmeal_dish sd LEFT JOIN dish d ON sd.dish_id = d.id " +
            "WHERE sd.setmeal_id = #{setmealId}")
    List<SetmealDish> findBySetmealId(@Param("setmealId") Integer setmealId);

    @Delete("DELETE FROM setmeal_dish WHERE setmeal_id=#{setmealId} AND dish_id=#{dishId}")
    int delete(@Param("setmealId") Integer setmealId, @Param("dishId") Integer dishId);

    @Delete("DELETE FROM setmeal_dish WHERE setmeal_id=#{setmealId}")
    int deleteBySetmealId(@Param("setmealId") Integer setmealId);

    @Insert("<script>" +
            "INSERT INTO setmeal_dish(setmeal_id, dish_id, quantity) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.setmealId}, #{item.dishId}, #{item.quantity})" +
            "</foreach>" +
            "</script>")
    int batchAdd(@Param("list") List<SetmealDish> list);
}