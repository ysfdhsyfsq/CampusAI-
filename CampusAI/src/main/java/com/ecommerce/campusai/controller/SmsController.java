package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.util.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sms")
public class SmsController {

    private final RedisUtil redisUtil;
    private final Random random = new Random();

    public SmsController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @GetMapping("/sendCode")
    public Map<String, Object> sendCode(@RequestParam String phone) {
        Map<String, Object> result = new HashMap<>();

        if (redisUtil.hasKey("sms:limit:" + phone)) {
            result.put("code", 400);
            result.put("msg", "请稍后再发送验证码（60秒内只能发送一次）");
            return result;
        }

        String code = String.format("%04d", random.nextInt(10000));

        // 存入 Redis，有效期 5 分钟
        redisUtil.set("sms:code:" + phone, code, 5, TimeUnit.MINUTES);
        // 设置发送限制，60 秒内不可重复发送
        redisUtil.set("sms:limit:" + phone, "1", 60, TimeUnit.SECONDS);

        result.put("code", 200);
        result.put("msg", "验证码发送成功：" + code + "（5分钟内有效）");
        
        // 在控制台打印，方便测试
        System.out.println("【测试模式】手机号：" + phone + "，验证码：" + code);

        return result;
    }
}