package com.kou.kouappapi.service

import com.kou.kouappapi.auth.service.dto.CompleteProfileRequestDto
import com.kou.kouappapi.exception.UserAlreadyProfileCompleteException
import com.kou.kouappapi.exception.UserNotFoundException
import com.kou.kouappapi.manager.image.ImageManager
import com.kou.kouappapi.property.CloudinaryProperties
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.service.dto.CompleteProfileResponseDto
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
    @Transactional
    fun completeProfile(requestDto: CompleteProfileRequestDto): CompleteProfileResponseDto {
        val user = userRepository.findByIdOrNull(requestDto.userId) ?: throw UserNotFoundException()
        if (user.profileCompleted) {
            throw UserAlreadyProfileCompleteException()
        }

        val resultUploadImage =
            requestDto.profileFile?.let {
                imageManager.uploadImage(it, cloudinaryProperties.folder.profile)
            }

        val encodedPassword = requestDto.password?.let { passwordEncoder.encode(it) }

        user.completeProfile(
            name = requestDto.name,
            encodedPassword = encodedPassword,
            profileImageId = resultUploadImage?.let { resultUploadImage.publicId },
        )

        return CompleteProfileResponseDto(
            userId = user.id,
            email = user.email,
            userName = user.name,
            profileImageUrl = resultUploadImage?.let { resultUploadImage.url },
        )
    }
}
