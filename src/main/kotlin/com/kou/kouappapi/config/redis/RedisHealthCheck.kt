package com.kou.kouappapi.config.redis

import jakarta.annotation.PostConstruct
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisHealthCheck(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    @PostConstruct
    fun check() {
        redisTemplate.opsForValue().set("ping", "pong")
        println(redisTemplate.opsForValue().get("ping"))
    }
}
