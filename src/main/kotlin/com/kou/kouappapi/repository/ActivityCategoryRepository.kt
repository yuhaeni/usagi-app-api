package com.kou.kouappapi.repository

import com.kou.kouappapi.entity.ActivityCategory
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityCategoryRepository : JpaRepository<ActivityCategory, Long> {
    fun findByUserIdIsNullOrUserIdOrderByName(userId: Long): List<ActivityCategory>
}
