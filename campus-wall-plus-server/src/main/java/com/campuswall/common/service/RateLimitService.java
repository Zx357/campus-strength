package com.campuswall.common.service;

import com.campuswall.common.exception.BusinessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimitService {
    private final StringRedisTemplate redisTemplate;
    private final ConcurrentHashMap<String, AtomicInteger> fallback = new ConcurrentHashMap<>();

    public RateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void check(String key, int max, Duration window, String message) {
        try {
            Long count = redisTemplate.opsForValue().increment(key);
            if (count != null && count == 1) {
                redisTemplate.expire(key, window);
            }
            if (count != null && count > max) {
                throw new BusinessException(message);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ignored) {
            int count = fallback.computeIfAbsent(key, k -> new AtomicInteger()).incrementAndGet();
            if (count > max) throw new BusinessException(message);
        }
    }
}
