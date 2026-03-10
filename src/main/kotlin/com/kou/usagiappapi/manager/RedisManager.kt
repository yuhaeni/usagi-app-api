package com.kou.usagiappapi.manager

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisManager(
    private val stringRedisTemplate: StringRedisTemplate,
) {
    fun setBlackList(
        accessToken: String,
        remainingTime: Long,
    ) {
        stringRedisTemplate.opsForValue().set(
            "blacklist:$accessToken",
            "logout",
            remainingTime,
            TimeUnit.MILLISECONDS,
        )
    }

    fun isBlackList(accessToken: String): Boolean = stringRedisTemplate.hasKey("blacklist:$accessToken")
}
