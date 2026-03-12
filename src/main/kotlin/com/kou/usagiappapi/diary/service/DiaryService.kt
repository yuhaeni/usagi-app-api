package com.kou.usagiappapi.diary.service

import com.kou.usagiappapi.activityCategory.entity.toResponseDto
import com.kou.usagiappapi.activityCategory.exception.ActivityCategoryNotFoundException
import com.kou.usagiappapi.activityCategory.repository.ActivityCategoryRepository
import com.kou.usagiappapi.activityCategory.service.dto.ActivityCategoryResponseDto
import com.kou.usagiappapi.diary.entity.Diary
import com.kou.usagiappapi.diary.entity.DiaryActivityCategory
import com.kou.usagiappapi.diary.exception.DiaryAlreadyExistsException
import com.kou.usagiappapi.diary.exception.DiaryNotFoundException
import com.kou.usagiappapi.diary.exception.NotDiaryOwnerException
import com.kou.usagiappapi.diary.repository.DiaryActivityCategoryRepository
import com.kou.usagiappapi.diary.repository.DiaryRepository
import com.kou.usagiappapi.diary.service.dto.CreateDiaryRequestDto
import com.kou.usagiappapi.diary.service.dto.CreateDiaryResponseDto
import com.kou.usagiappapi.diary.service.dto.GetDiariesResponseDto
import com.kou.usagiappapi.diary.service.dto.GetDiaryResponseDto
import com.kou.usagiappapi.diary.service.dto.UpdateDiaryRequestDto
import com.kou.usagiappapi.diary.service.dto.UpdateDiaryResponseDto
import com.kou.usagiappapi.global.image.CloudinaryProperties
import com.kou.usagiappapi.global.image.ImageManager
import com.kou.usagiappapi.global.image.ImageUploadResult
import com.kou.usagiappapi.shared.tool.DateTool
import com.kou.usagiappapi.user.exception.UserNotFoundException
import com.kou.usagiappapi.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class DiaryService(
    private val diaryRepository: DiaryRepository,
    private val userRepository: UserRepository,
    private val activityCategoryRepository: ActivityCategoryRepository,
    private val diaryActivityCategoryRepository: DiaryActivityCategoryRepository,
    private val imageManager: ImageManager,
    private val cloudinaryProperties: CloudinaryProperties,
) {
    companion object {
        private const val DIARY_IMAGE_WIDTH = 500
        private const val DIARY_IMAGE_HEIGHT = 300
    }

    fun getDiaries(
        userId: Long,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
    ): List<GetDiariesResponseDto> {
        val start = startDate ?: DateTool.getFirstDayOfCurrentMonth()
        val end = endDate ?: DateTool.getLastDayOfCurrentMonth()
        val diaries = diaryRepository.findByUserIdAndDateBetweenOrderByDateAsc(userId, start, end)
        return diaries.map {
            GetDiariesResponseDto(diaryId = it.id, date = it.date, emotion = it.emotion)
        }
    }

    fun getDiary(
        userId: Long,
        diaryId: Long,
    ): GetDiaryResponseDto {
        val diary = getValidatedDiary(userId, diaryId)
        val imageUrl =
            diary.imageId?.let {
                imageManager.getImageUrl(publicId = it, width = DIARY_IMAGE_WIDTH, height = DIARY_IMAGE_HEIGHT)
            }

        val diaryActivityCategories =
            diary.diaryActivityCategories.map {
                ActivityCategoryResponseDto(
                    id = it.activityCategory.id,
                    name = it.activityCategory.name,
                )
            }

        return GetDiaryResponseDto(
            diaryId = diary.id,
            date = diary.date,
            emotion = diary.emotion,
            imageUrl = imageUrl,
            content = diary.content,
            diaryActivityCategories = diaryActivityCategories,
        )
    }

    @Transactional
    fun createDiary(
        userId: Long,
        requestDto: CreateDiaryRequestDto,
        imageFile: MultipartFile?,
    ): CreateDiaryResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        val savedDiaries = diaryRepository.findDiariesByUserIdAndDate(userId, requestDto.date)
        if (savedDiaries.isNotEmpty()) {
            throw DiaryAlreadyExistsException(requestDto.date)
        }

        val image = handleImage(imageFile = imageFile)

        val diary =
            diaryRepository.save(
                Diary(
                    user = user,
                    emotion = requestDto.emotion,
                    date = requestDto.date,
                    imageId = image?.publicId,
                    content = requestDto.content,
                ),
            )

        handleActivityCategory(activityCategoryIds = requestDto.activityCategoryIds, diary = diary)

        return CreateDiaryResponseDto(
            diaryId = diary.id,
            date = diary.date,
            emotion = diary.emotion,
            imageUrl =
                diary.imageId?.let {
                    imageManager.getImageUrl(
                        publicId = it,
                        width = DIARY_IMAGE_WIDTH,
                        height = DIARY_IMAGE_HEIGHT,
                    )
                },
            content = diary.content,
            diaryActivityCategories =
                diary.diaryActivityCategories.map { it.activityCategory }.toResponseDto(),
        )
    }

    @Transactional
    fun updateDiary(
        userId: Long,
        diaryId: Long,
        requestDto: UpdateDiaryRequestDto,
        imageFile: MultipartFile? = null,
    ): UpdateDiaryResponseDto {
        val diary = getValidatedDiary(userId, diaryId)
        val image =
            handleImage(imageFile = imageFile, deleteImage = requestDto.deleteImage ?: false, diary = diary)
        handleActivityCategory(activityCategoryIds = requestDto.activityCategoryIds, diary = diary)

        diary.update(
            emotion = requestDto.emotion,
            content = requestDto.content,
            deleteImage = requestDto.deleteImage ?: false,
            imageId = image?.publicId,
        )

        return UpdateDiaryResponseDto(
            diaryId = diary.id,
            date = diary.date,
            emotion = diary.emotion,
            imageUrl =
                diary.imageId?.let {
                    imageManager.getImageUrl(
                        publicId = it,
                        width = DIARY_IMAGE_WIDTH,
                        height = DIARY_IMAGE_HEIGHT,
                    )
                },
            content = diary.content,
            diaryActivityCategories =
                diary.diaryActivityCategories.map { it.activityCategory }.toResponseDto(),
        )
    }

    private fun handleImage(
        imageFile: MultipartFile?,
        deleteImage: Boolean = false,
        diary: Diary? = null,
    ): ImageUploadResult? {
        if (
            deleteImage ||
            imageFile != null
        ) {
            diary?.imageId?.let {
                imageManager.deleteImage(it)
            }
        }

        return imageFile?.let {
            imageManager.uploadImage(
                file = it,
                uploadFolder = cloudinaryProperties.folder.diary,
                width = DIARY_IMAGE_WIDTH,
                height = DIARY_IMAGE_HEIGHT,
            )
        }
    }

    private fun handleActivityCategory(
        activityCategoryIds: List<Long>?,
        diary: Diary,
    ) {
        activityCategoryIds ?: return

        val activityCategories = activityCategoryRepository.findAllById(activityCategoryIds)
        if (activityCategoryIds.size != activityCategories.size) {
            throw ActivityCategoryNotFoundException()
        }

        diary.diaryActivityCategories.clear()
        diaryActivityCategoryRepository.flush()

        val diaryActivityCategories =
            diaryActivityCategoryRepository.saveAll<DiaryActivityCategory>(
                activityCategories.map {
                    DiaryActivityCategory(activityCategory = it, diary = diary)
                },
            )

        diary.diaryActivityCategories.addAll(diaryActivityCategories)
    }

    @Transactional
    fun deleteDiary(
        userId: Long,
        diaryId: Long,
    ) {
        val diary = getValidatedDiary(userId, diaryId)
        diary.imageId?.let { imageId ->
            imageManager.deleteImage(imageId)
        }

        diaryRepository.delete(diary)
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
