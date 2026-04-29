package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.DishCategory;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DishCategoryMapper {

    @Select("SELECT * FROM dish_category ORDER BY sort")
    List<DishCategory> findAll();

    @Select("SELECT * FROM dish_category WHERE id = #{id}")
    DishCategory findById(@Param("id") Integer id);

    @Insert("INSERT INTO dish_category(name,sort) VALUES(#{name},#{sort})")
    int add(DishCategory category);

    @Update("UPDATE dish_category SET name = #{name}, sort = #{sort} WHERE id = #{id}")
    int update(DishCategory category);

    @Delete("DELETE FROM dish_category WHERE id = #{id}")
    int delete(@Param("id") Integer id);
}