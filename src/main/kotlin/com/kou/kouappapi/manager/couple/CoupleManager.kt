package com.kou.kouappapi.manager.couple

import com.kou.kouappapi.entity.Couple
import com.kou.kouappapi.exception.UserNotFoundException
import com.kou.kouappapi.repository.CoupleRepository
import com.kou.kouappapi.repository.UserRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CoupleManager(
    private val redisTemplate: StringRedisTemplate,
    private val userRepository: UserRepository,
    private val coupleRepository: CoupleRepository,
) {
    companion object {
        private const val CODE_LENGTH = 6
        private const val CHARSET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        const val INVITE_USER_KEY = "invite:user:"
        const val INVITE_CODE_KEY = "invite:code:"
    }

    fun getCoupleInviteUrl(inviterUserId: Long) {
        val inviteUserValue = getRedisValueForInviteUser(inviterUserId)
        if (inviteUserValue != null) {
            throw CoupleAlreadyInviteRequestException()
        }

        if (coupleRepository.existsByInviterUserId(inviterUserId)) {
            throw CoupleAlreadyConnectionException()
        }

        val inviteCode = generateInviteCode()
        saveCoupleInviteCode(inviterUserId, inviteCode)
//        inviteCodeValue.inviterUserId

        // TODO 유니버셜 링크 생성해서 리턴
    }

    fun saveCoupleInviteCode(
        inviterUserId: Long,
        inviteCode: String,
    ) {
        val ttl = Duration.ofHours(24)

        setRedisValueForInviteUser(inviterUserId, inviteCode, ttl)
        setRedisValueForInviteCode(inviterUserId, inviteCode, ttl)
    }

    private fun setRedisValueForInviteUser(
        inviterUserId: Long,
        inviteCode: String,
        ttl: Duration,
    ) = redisTemplate
        .opsForValue()
        .set(
            "$INVITE_USER_KEY$inviterUserId",
            inviteCode,
            ttl,
        )

    private fun setRedisValueForInviteCode(
        inviterUserId: Long,
        inviteCode: String,
        ttl: Duration,
    ) = redisTemplate
        .opsForValue()
        .set(
            "$INVITE_CODE_KEY$inviteCode",
            inviterUserId.toString(),
            ttl,
        )

    fun getRedisValueForInviteUser(inviterUserId: Long) =
        redisTemplate.opsForValue().get("$INVITE_USER_KEY$inviterUserId")

    fun getRedisValueForInviteCode(inviteCode: String) = redisTemplate.opsForValue().get("$INVITE_CODE_KEY$inviteCode")

    fun generateInviteCode(): String =
        (1..CODE_LENGTH)
            .map { CHARSET.random() }
            .joinToString("")

    fun completeCoupleConnection(
        inviteeUserId: Long,
        inviteCode: String,
    ): Long {
        val inviteUserValue =
            getRedisValueForInviteCode(inviteCode)
                ?: throw CoupleNotFoundInviteRequestException()

        val inviterUser =
            userRepository.findByIdOrNull(inviteUserValue.toLong())
                ?: throw UserNotFoundException()

        if (
            coupleRepository.existsByInviterUserIdAndInviteeUserId(
                inviterUserId = inviterUser.id,
                inviteeUserId = inviteeUserId,
            )
        ) {
            throw CoupleAlreadyConnectionException()
        }

        val saveCouple =
            coupleRepository.save(
                Couple(
                    inviterUserId = inviterUser.id,
                    inviteeUserId = inviteeUserId,
                ),
            )

        return saveCouple.id
    }
}
