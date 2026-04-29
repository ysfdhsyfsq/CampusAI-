package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.User;
import com.ecommerce.campusai.mapper.UserMapper;
import com.ecommerce.campusai.util.RedisUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {

    private final RedisUtil redisUtil;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public LoginController(RedisUtil redisUtil, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.redisUtil = redisUtil;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/currentUser")
    public Map<String, Object> getCurrentUser(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        String username = (String) session.getAttribute("username");
        
        if (username != null) {
            User user = userMapper.findByUsername(username);
            
            result.put("code", 200);
            result.put("id", user != null ? user.getId() : null);
            result.put("username", username);
            result.put("role", user != null ? user.getRole() : "USER");
            result.put("phone", user != null ? user.getPhone() : "");
            result.put("msg", "获取成功");
        } else {
            result.put("code", 401);
            result.put("msg", "未登录");
        }
        
        return result;
    }

    @PostMapping("/codeLogin")
    public Map<String, Object> codeLogin(@RequestParam String phone,
                                          @RequestParam String code,
                                          HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        String redisCode = redisUtil.get("sms:code:" + phone);
        if (redisCode == null) {
            result.put("code", 400);
            result.put("msg", "验证码已过期");
            return result;
        }
        
        if (!redisCode.equals(code)) {
            result.put("code", 400);
            result.put("msg", "验证码错误");
            return result;
        }

        User user = userMapper.findByUsername(phone);
        if (user == null) {
            user = new User();
            user.setUsername(phone);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole("USER");
            user.setPhone(phone);
            user.setStatus(1);
            userMapper.insert(user);
        }

        session.setAttribute("username", user.getUsername());

        redisUtil.delete("sms:code:" + phone);

        result.put("code", 200);
        result.put("msg", "登录成功！当前用户：" + phone);
        result.put("username", phone);
        return result;
    }
}