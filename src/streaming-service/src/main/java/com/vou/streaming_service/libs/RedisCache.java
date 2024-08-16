/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 12/08/2024 - 14:28
 */
package com.vou.streaming_service.libs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RedisCache {
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisCache(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Set a key-value pair with optional TTL in seconds
    public void set(String key, String value, Long ttlInSeconds) {
        if (ttlInSeconds != null && ttlInSeconds > 0) {
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, Duration.ofSeconds(ttlInSeconds));
            log.debug("Set value for key: {} with TTL: {} seconds", key, ttlInSeconds);
        } else {
            redisTemplate.opsForValue().set(key, value);
            log.debug("Set value for key: {} without TTL", key);
        }
    }

    // Overloaded method to set a key-value pair without TTL
    public void set(String key, String value) {
        set(key, value, null);

    }

    // Get a value by key
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Get all key-value pairs
    public Map<String, String> getAll() {
        return getAll("*");
    }

    // Get all key-value pairs matching a pattern
    public Map<String, String> getAll(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        return keys.stream()
                .collect(Collectors.toMap(
                        key -> key,
                        this::get
                ));
    }

    // Delete a key
    public void delete(String key) {
        redisTemplate.delete(key);
        log.debug("Deleted key: {}", key);
    }

    // Set time to live for a key in seconds
    public void setTTL(String key, long ttlInSeconds) {
        redisTemplate.expire(key, Duration.ofDays(ttlInSeconds));
        log.debug("Set TTL for key: {} to {} seconds", key, ttlInSeconds);
    }

    // Check if a key exists
    public boolean hasKey(String key) {
        Boolean exists = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(exists);
    }

    // Get the TTL of a key in seconds
    public Long getTTL(String key) {
        return redisTemplate.getExpire(key);
    }
}