package com.kou.usagiappapi.activityCategory.service

import com.kou.usagiappapi.activityCategory.repository.ActivityCategoryRepository
import com.kou.usagiappapi.activityCategory.service.dto.GetActivityCategoriesResponseDto
import org.springframework.stereotype.Service

@Service
class ActivityCategoryService(
    val activityCategoryRepository: ActivityCategoryRepository,
) {
    fun getActivityCategories(userId: Long): List<GetActivityCategoriesResponseDto> {
        val activityCategoryList = activityCategoryRepository.findByUserIdIsNullOrUserIdOrderByName(userId)
        val sortedActivityCategoryList = activityCategoryList.sortedBy { it.name }
        return sortedActivityCategoryList.map {
            GetActivityCategoriesResponseDto(
                activityCategoryId = it.id,
                name = it.name,
                userId = it.user?.id,
            )
        }
    }
}
