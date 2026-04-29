package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select * from user where id=#{id}")
    User findById(Integer id);

    @Select("select * from user where username=#{username}")
    User findByUsername(String username);

    @Select("select * from user")
    List<User> findAll();

    @Insert("INSERT INTO user(username, password, role, phone, status) VALUES(#{username}, #{password}, #{role}, #{phone}, #{status})")
    int insert(User user);

    @Select("SELECT DATE(create_time) as date, COUNT(*) as count FROM user WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) GROUP BY DATE(create_time) ORDER BY date")
    List<Map<String, Object>> getUserRegistrationTrend();

    @Select("SELECT COUNT(*) as total FROM user")
    int getTotalUserCount();

    @Select("SELECT DISTINCT avatar FROM user WHERE avatar IS NOT NULL AND avatar != ''")
    List<String> getAllAvatarPaths();
}