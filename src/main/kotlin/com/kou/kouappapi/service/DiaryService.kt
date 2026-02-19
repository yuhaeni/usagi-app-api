package com.kou.kouappapi.service

import com.kou.kouappapi.entity.Diary
import com.kou.kouappapi.exception.DiaryAlreadyExistsException
import com.kou.kouappapi.exception.UserNotFoundException
import com.kou.kouappapi.manager.image.ImageManager
import com.kou.kouappapi.manager.image.ImageUploadResult
import com.kou.kouappapi.property.CloudinaryProperties
import com.kou.kouappapi.repository.DiaryRepository
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.service.dto.CreateDiaryRequestDto
import com.kou.kouappapi.service.dto.CreateDiaryResponseDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DiaryService(
    val diaryRepository: DiaryRepository,
    val userRepository: UserRepository,
    val imageManager: ImageManager,
    val cloudinaryProperties: CloudinaryProperties,
) {
    fun createDiary(
        userId: Long,
        requestDto: CreateDiaryRequestDto,
    ): CreateDiaryResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()

        // TODO GlobalExceptionHandler 추가  -> @ExceptionHandler
        val savedDiaryList = diaryRepository.findDiariesByUserIdAndDate(userId, requestDto.date)
        if (savedDiaryList.isNotEmpty()) {
            throw DiaryAlreadyExistsException(requestDto.date)
        }

        var imageResult: ImageUploadResult? = null
        requestDto.imageFile?.let { file ->
            imageResult =
                imageManager.uploadImage(
                    file = file,
                    uploadFolder = cloudinaryProperties.folder.diary,
                    width = 500,
                    height = 300,
                )
        }

        val diary =
            diaryRepository.save(
                Diary(
                    user = user,
                    emotion = requestDto.emotion,
                    date = requestDto.date,
                    imageId = imageResult?.publicId,
                    content = requestDto.content,
                ),
            )

        return CreateDiaryResponseDto(
            diaryId = diary.id,
            date = diary.date,
            emotion = diary.emotion,
            imageUrl = diary.imageId?.let { imageId -> imageManager.getImageUrl(imageId, 500, 300) },
            content = diary.content,
        )
    }
}
