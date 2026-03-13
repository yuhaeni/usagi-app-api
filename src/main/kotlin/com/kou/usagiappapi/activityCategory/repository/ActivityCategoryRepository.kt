package com.kou.usagiappapi.activityCategory.repository

import com.kou.usagiappapi.activityCategory.entity.ActivityCategory
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityCategoryRepository : JpaRepository<ActivityCategory, Long> {
    fun findByUserIdIsNullOrUserIdOrderByName(userId: Long): List<ActivityCategory>
}
