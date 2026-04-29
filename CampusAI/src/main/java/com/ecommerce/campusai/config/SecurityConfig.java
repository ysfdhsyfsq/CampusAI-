package com.ecommerce.campusai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 允许 AI 接口、短信、静态资源匿名访问
                        .requestMatchers("/ai/**", "/sms/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // 允许活动列表和详情访问
                        .requestMatchers("/activity/**").permitAll()
                        // 允许登录相关接口（包括验证码登录、用户名密码登录和获取当前用户）
                        .requestMatchers("/login", "/user/login", "/user/codeLogin", "/user/currentUser").permitAll()
                        // 允许静态 HTML 页面访问
                        .requestMatchers("/*.html").permitAll()
                        // 允许商家管理接口（需要认证）
                        .requestMatchers("/merchant/**").authenticated()
                        // 允许自习室相关接口（公开）
                        .requestMatchers("/seat/**", "/reserve/**", "/room/**").permitAll()
                        // 允许数据统计接口公开访问
                        .requestMatchers("/statistics/**").permitAll()
                        // 允许报表导出接口公开访问
                        .requestMatchers("/report/**").permitAll()
                        // 管理员专属接口
                        .requestMatchers("/cache/**").hasAuthority("ADMIN")
                        // 允许所有认证用户访问菜品查询和分类（GET请求）
                        .requestMatchers("/dish/list", "/dish/{id}", "/category/list").authenticated()
                        // 商家和管理员可以访问餐饮管理（增删改操作）
                        .requestMatchers("/dish/add", "/dish/update", "/dish/delete/**", "/dish/status/**", 
                                        "/category/add", "/category/update", "/category/delete/**",
                                        "/setmeal/**", "/setmealDish/**").hasAnyAuthority("ADMIN", "MERCHANT")
                        // 订单创建接口：所有认证用户都可以访问
                        .requestMatchers("/order/create").authenticated()
                        // 订单查询接口：所有认证用户都可以访问
                        .requestMatchers("/order/list", "/order/detail/**").authenticated()
                        // 其他请求需要认证
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/currentUser", true)
                        .permitAll()
                )
                .rememberMe(rem -> rem.key("security").tokenValiditySeconds(7*24*60*60))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/dashboard.html")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}