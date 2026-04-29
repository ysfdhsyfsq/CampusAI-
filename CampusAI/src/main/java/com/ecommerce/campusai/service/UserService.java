package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.User;
import com.ecommerce.campusai.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    // 给 UserController 提供的 login 方法
    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user == null) return null;
        // 这里因为用了Spring Security，密码校验交给Security处理，这里简化
        return user;
    }
}