package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.Setmeal;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface SetmealMapper {

    @Select("SELECT * FROM setmeal ORDER BY id DESC")
    List<Setmeal> findAll();

    @Select("SELECT * FROM setmeal WHERE id = #{id}")
    Setmeal findById(@Param("id") Integer id);

    @Insert("INSERT INTO setmeal(name, price, status, description, image) " +
            "VALUES(#{name}, #{price}, #{status}, #{description}, #{image})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int add(Setmeal setmeal);

    @Update("UPDATE setmeal SET name=#{name}, price=#{price}, description=#{description}, " +
            "image=#{image}, update_time=NOW() WHERE id=#{id}")
    int update(Setmeal setmeal);

    @Update("UPDATE setmeal SET status=#{status}, update_time=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Delete("DELETE FROM setmeal WHERE id=#{id}")
    int delete(@Param("id") Integer id);
}