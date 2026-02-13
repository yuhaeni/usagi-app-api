package com.kou.kouappapi.service

import com.kou.kouappapi.exception.UserNotFoundException
import com.kou.kouappapi.manager.image.ImageManager
import com.kou.kouappapi.property.CloudinaryProperties
import com.kou.kouappapi.repository.CoupleRepository
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.service.dto.GetUserProfileResponseDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val coupleRepository: CoupleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val imageManager: ImageManager,
    private val cloudinaryProperties: CloudinaryProperties,
) {
    fun getUserProfile(userId: Long): GetUserProfileResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        val couple = coupleRepository.findByIdOrNull(userId)

        // TODO 프로필 이미지 주소 추가
        var profileImageUrl: String? = null
        user.profileImageId?.let {
            profileImageUrl = imageManager.getProfileImageUrl(user.profileImageId!!)
        }

        return GetUserProfileResponseDto(
            userId = user.id,
            name = user.name,
            email = user.email,
            profileImageUrl = profileImageUrl,
            coupleId = couple?.id,
            coupleStatus = couple?.status,
        )
    }
}
