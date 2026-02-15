package com.kou.kouappapi.service

import com.kou.kouappapi.controller.dto.ModifyUserProfileResponse
import com.kou.kouappapi.exception.UserNotFoundException
import com.kou.kouappapi.manager.image.ImageManager
import com.kou.kouappapi.property.CloudinaryProperties
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.service.dto.GetUserProfileResponseDto
import com.kou.kouappapi.service.dto.ModifyUserProfileRequestDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val imageManager: ImageManager,
    private val cloudinaryProperties: CloudinaryProperties,
) {
    fun getUserProfile(userId: Long): GetUserProfileResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()

        var profileImageUrl: String? = null
        user.profileImageId?.let {
            profileImageUrl = imageManager.getProfileImageUrl(user.profileImageId!!)
        }

        return GetUserProfileResponseDto(
            userId = user.id,
            name = user.name,
            email = user.email,
            profileImageUrl = profileImageUrl,
        )
    }

    @Transactional
    fun modifyUserProfile(
        id: Long,
        requestDto: ModifyUserProfileRequestDto,
    ): ModifyUserProfileResponse {
        val user = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()

        val encodedPassword =
            requestDto.password?.let { password ->
                passwordEncoder.encode(password)
            }

        val resultUploadImage =
            requestDto.profileImageFile?.let { file ->
                imageManager.uploadImage(file, cloudinaryProperties.folder.profile)
            }

        user.updateUser(
            name = requestDto.name,
            encodedPassword = encodedPassword,
            profileImageId = resultUploadImage?.publicId,
        )

        return ModifyUserProfileResponse(
            userId = user.id,
            profileImageUrl = resultUploadImage?.url,
        )
    }
}
