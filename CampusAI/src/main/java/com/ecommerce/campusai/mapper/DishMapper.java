package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.Dish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface DishMapper {
    List<Dish> findAll(@Param("categoryId") Integer categoryId, @Param("name") String name);
    Dish findById(@Param("id") Integer id);
    int addDish(Dish dish);
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    int updateDish(Dish dish);
    int deleteDish(@Param("id") Integer id);
    
    List<Dish> findByMerchant(@Param("merchantName") String merchantName, 
                              @Param("categoryId") Integer categoryId, 
                              @Param("name") String name);

    @Select("SELECT * FROM dish WHERE id = #{id}")
    Dish selectById(Integer id);

    @Select("SELECT * FROM dish WHERE category_id = #{categoryId} AND status = 1")
    List<Dish> selectByCategoryId(Integer categoryId);

    @Select("SELECT * FROM dish WHERE status = 1 ORDER BY create_time DESC")
    List<Dish> selectAll();

    @Select("SELECT DISTINCT image FROM dish WHERE image IS NOT NULL AND image != ''")
    List<String> getAllImagePaths();
}