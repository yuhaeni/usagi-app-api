package com.kou.kouappapi.service

import com.kou.kouappapi.auth.exception.AuthLoginRequiredException
import com.kou.kouappapi.auth.exception.AuthUnauthorizedTokenAccessException
import com.kou.kouappapi.controller.dto.UpdateUserProfileResponse
import com.kou.kouappapi.enums.UserStatus
import com.kou.kouappapi.exception.UserNotFoundException
import com.kou.kouappapi.manager.RedisManager
import com.kou.kouappapi.manager.image.ImageManager
import com.kou.kouappapi.manager.image.ImageUploadResult
import com.kou.kouappapi.property.CloudinaryProperties
import com.kou.kouappapi.repository.RefreshTokenRepository
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.security.jwt.JwtTokenProvider
import com.kou.kouappapi.service.dto.GetUserProfileResponseDto
import com.kou.kouappapi.service.dto.UpdateUserProfileRequestDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val imageManager: ImageManager,
    private val cloudinaryProperties: CloudinaryProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisManager: RedisManager,
) {
    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    fun getUserProfile(userId: Long): GetUserProfileResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()

        var profileImageUrl: String? = null
        user.profileImageId?.let { profileImageId ->
            profileImageUrl = imageManager.getImageUrl(profileImageId, 200, 200)
        }

        return GetUserProfileResponseDto(
            userId = user.id,
            name = user.name,
            email = user.email,
            profileImageUrl = profileImageUrl,
        )
    }

    @Transactional
    fun updateUserProfile(
        id: Long,
        requestDto: UpdateUserProfileRequestDto,
    ): UpdateUserProfileResponse {
        val user = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()

        val encodedPassword =
            requestDto.password?.let { password ->
                passwordEncoder.encode(password)
            }

        if (
            requestDto.deleteProfileImage ||
            requestDto.profileImageFile != null
        ) {
            user.profileImageId?.let { profileImageId ->
                imageManager.deleteImage(profileImageId)
            }
        }

        var resultUploadImage: ImageUploadResult? = null
        requestDto.profileImageFile?.let { file ->
            resultUploadImage = imageManager.uploadImage(file, cloudinaryProperties.folder.profile, 200, 200)
        }

        user.update(
            name = requestDto.name,
            encodedPassword = encodedPassword,
            profileImageId = resultUploadImage?.publicId,
            deleteProfileImage = requestDto.deleteProfileImage,
        )

        return UpdateUserProfileResponse(
            userId = user.id,
            profileImageUrl = user.profileImageId?.let { imageId -> imageManager.getImageUrl(imageId, 200, 200) },
        )
    }

    @Transactional
    fun withdrawUser(
        userId: Long,
        request: HttpServletRequest,
    ) {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        // TODO 추후에 배치 돌면서 물리적 삭제 -> 관련 데이터 처리(일기, 이미지)
        user.update(status = UserStatus.WITHDRAWN, deletedAt = LocalDateTime.now())

        user.profileImageId?.let { profileImageId ->
            imageManager.deleteImage(profileImageId)
            user.delete(profileImageId = profileImageId)
        }

        // TODO Filter / Interceptor 레벨에서 처리
        val authHeader =
            request.getHeader(HttpHeaders.AUTHORIZATION)
                ?: throw AuthLoginRequiredException()
        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw AuthUnauthorizedTokenAccessException()
        }

        val accessToken = authHeader.replace(BEARER_PREFIX, "").trim()
        val remainingTime = jwtTokenProvider.getRemainingTime(accessToken)
        if (remainingTime > 0) {
            redisManager.setBlackList(accessToken, remainingTime)
        }

        // TODO deleteAllById 성능 체크 필요
        //  https://programmer-chocho.tistory.com/102
        refreshTokenRepository.deleteAllByUserId(user.id)
    }
}
