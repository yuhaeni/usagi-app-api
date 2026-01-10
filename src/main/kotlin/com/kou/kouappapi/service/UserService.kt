package com.kou.kouappapi.service

import com.kou.kouappapi.auth.service.dto.CompleteProfileRequestDto
import com.kou.kouappapi.exception.UserNotFoundException
import com.kou.kouappapi.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun completeProfile(requestDto: CompleteProfileRequestDto): Long? {
        val user = userRepository.findByIdOrNull(requestDto.userId) ?: throw UserNotFoundException()
        user.update(requestDto)
        return user.id
    }
}
