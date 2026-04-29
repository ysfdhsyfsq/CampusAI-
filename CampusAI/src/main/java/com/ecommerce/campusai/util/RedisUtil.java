package com.ecommerce.campusai.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    public RedisUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public Long getKeyspaceHits() {
        return redisTemplate.getConnectionFactory().getConnection().info().getProperty("keyspace_hits") != null ?
                Long.parseLong(redisTemplate.getConnectionFactory().getConnection().info().getProperty("keyspace_hits")) : 0;
    }

    public Long getKeyspaceMisses() {
        return redisTemplate.getConnectionFactory().getConnection().info().getProperty("keyspace_misses") != null ?
                Long.parseLong(redisTemplate.getConnectionFactory().getConnection().info().getProperty("keyspace_misses")) : 0;
    }
}