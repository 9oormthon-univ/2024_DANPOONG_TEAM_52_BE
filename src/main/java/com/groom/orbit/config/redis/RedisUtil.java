package com.groom.orbit.config.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class RedisUtil {
  private final StringRedisTemplate stringRedisTemplate;
}
