package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.User;
import com.ecommerce.campusai.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username,
                                     @RequestParam String password,
                                     HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = userService.login(username, password);
        if (user != null) {
            // 保存 username 到 Session
            request.getSession().setAttribute("username", user.getUsername());
            
            // 手动建立 Spring Security 认证上下文
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
            
            UsernamePasswordAuthenticationToken token = 
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);
            
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(token);
            SecurityContextHolder.setContext(context);
            
            request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
            
            map.put("code", 200);
            map.put("msg", "登录成功");
            map.put("user", user);
        } else {
            map.put("code", 500);
            map.put("msg", "账号或密码错误");
        }
        return map;
    }

    @GetMapping("/list")
    public Object list() {
        return userService.findAll();
    }
}