package com.kou.kouappapi.service

import com.kou.kouappapi.entity.Diary
import com.kou.kouappapi.exception.DiaryAlreadyExistsException
import com.kou.kouappapi.exception.DiaryNotFoundException
import com.kou.kouappapi.exception.NotDiaryOwnerException
import com.kou.kouappapi.exception.UserNotFoundException
import com.kou.kouappapi.manager.image.ImageManager
import com.kou.kouappapi.manager.image.ImageUploadResult
import com.kou.kouappapi.property.CloudinaryProperties
import com.kou.kouappapi.repository.DiaryRepository
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.service.dto.CreateDiaryRequestDto
import com.kou.kouappapi.service.dto.CreateDiaryResponseDto
import com.kou.kouappapi.service.dto.GetDiaryResponseDto
import com.kou.kouappapi.service.dto.UpdateDiaryRequestDto
import com.kou.kouappapi.service.dto.UpdateDiaryResponseDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DiaryService(
    val diaryRepository: DiaryRepository,
    val userRepository: UserRepository,
    val imageManager: ImageManager,
    val cloudinaryProperties: CloudinaryProperties,
) {
    @Transactional
    fun createDiary(
        userId: Long,
        requestDto: CreateDiaryRequestDto,
    ): CreateDiaryResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()

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

    fun getDiary(
        userId: Long,
        diaryId: Long,
    ): GetDiaryResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        val diary = getValidatedDiary(user.id, diaryId)
        var imageUrl = null
        diary.imageId?.let { imageId ->
            imageUrl = imageManager.getImageUrl(imageId, 500, 300) as Nothing?
        }

        return GetDiaryResponseDto(
            diaryId = diary.id,
            date = diary.date,
            emotion = diary.emotion,
            imageUrl = imageUrl,
            content = diary.content,
        )
    }

    @Transactional
    fun updateDiary(
        userId: Long,
        diaryId: Long,
        request: UpdateDiaryRequestDto,
    ): UpdateDiaryResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        val diary = getValidatedDiary(user.id, diaryId)

        var resultUploadImage: ImageUploadResult? = null
        request.imageFile?.let { file ->
            resultUploadImage =
                imageManager.uploadImage(
                    file = file,
                    uploadFolder = cloudinaryProperties.folder.diary,
                    width = 500,
                    height = 300,
                )

            diary.imageId?.let { imageId ->
                imageManager.deleteImage(imageId)
            }
        }

        diary.update(emotion = request.emotion, imageId = resultUploadImage?.publicId, content = request.content)

        return UpdateDiaryResponseDto(
            diaryId = diary.id,
            date = diary.date,
            emotion = request.emotion,
            imageUrl = diary.imageId?.let { imageId -> imageManager.getImageUrl(imageId, 500, 300) },
            content = diary.content,
        )
    }

    private fun getValidatedDiary(
        userId: Long,
        diaryId: Long,
    ): Diary {
        val diary = diaryRepository.findByIdOrNull(diaryId) ?: throw DiaryNotFoundException()
        if (diary.user.id != userId) {
            throw NotDiaryOwnerException()
        }

        return diary
    }
}
