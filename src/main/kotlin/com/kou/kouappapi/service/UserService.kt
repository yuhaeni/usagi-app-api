package com.kou.kouappapi.service

import com.kou.kouappapi.auth.service.dto.CompleteProfileRequestDto
import com.kou.kouappapi.exception.UserAlreadyProfileCompleteException
import com.kou.kouappapi.exception.UserNotFoundException
import com.kou.kouappapi.repository.UserRepository
import org.postgresql.util.PasswordUtil.encodePassword
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun completeProfile(requestDto: CompleteProfileRequestDto): Long {
        val user = userRepository.findByIdOrNull(requestDto.userId) ?: throw UserNotFoundException()
        if (user.profileCompleted) {
            throw UserAlreadyProfileCompleteException()
        }

        // TODO 프로필 이미지 업데이트

        val encodePassword = requestDto.password?.let { passwordEncoder.encode(it) }
        user.update(requestDto, encodePassword)
        return user.id
    }
}
