package com.kou.usagiappapi.service

import com.kou.usagiappapi.auth.exception.AuthLoginRequiredException
import com.kou.usagiappapi.auth.exception.AuthUnauthorizedTokenAccessException
import com.kou.usagiappapi.controller.dto.UpdateUserProfileResponse
import com.kou.usagiappapi.entity.User
import com.kou.usagiappapi.enums.UserStatus
import com.kou.usagiappapi.exception.UserNotFoundException
import com.kou.usagiappapi.manager.RedisManager
import com.kou.usagiappapi.manager.image.ImageManager
import com.kou.usagiappapi.manager.image.ImageUploadResult
import com.kou.usagiappapi.property.CloudinaryProperties
import com.kou.usagiappapi.repository.RefreshTokenRepository
import com.kou.usagiappapi.repository.UserRepository
import com.kou.usagiappapi.security.jwt.JwtTokenProvider
import com.kou.usagiappapi.service.dto.GetUserProfileResponseDto
import com.kou.usagiappapi.service.dto.UpdateUserProfileRequestDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
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

        val profileImage =
            handleProfileImage(
                profileImageFile = requestDto.profileImageFile,
                deleteProfileImage = requestDto.deleteProfileImage,
                user = user,
            )

        user.update(
            name = requestDto.name,
            encodedPassword = encodedPassword,
            profileImageId = profileImage?.publicId,
            deleteProfileImage = requestDto.deleteProfileImage ?: false,
        )

        return UpdateUserProfileResponse(
            userId = user.id,
            profileImageUrl = user.profileImageId?.let { imageId -> imageManager.getImageUrl(imageId, 200, 200) },
        )
    }

    private fun handleProfileImage(
        profileImageFile: MultipartFile?,
        deleteProfileImage: Boolean?,
        user: User,
    ): ImageUploadResult? {
        if (
            deleteProfileImage == true ||
            profileImageFile != null
        ) {
            user.profileImageId?.let { profileImageId ->
                imageManager.deleteImage(profileImageId)
            }
        }

        return profileImageFile?.let {
            imageManager.uploadImage(it, cloudinaryProperties.folder.profile, 200, 200)
        }
    }

    @Transactional
    fun withdrawUser(
        userId: Long,
        request: HttpServletRequest,
    ) {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        user.update(status = UserStatus.WITHDRAWN, deletedAt = LocalDateTime.now())
        // TODO 유저 관련 데이터 즉시 삭제로 변경

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
