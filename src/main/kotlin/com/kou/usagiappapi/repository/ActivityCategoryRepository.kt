package com.kou.usagiappapi.repository

import com.kou.usagiappapi.entity.ActivityCategory
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityCategoryRepository : JpaRepository<ActivityCategory, Long> {
    fun findByUserIdIsNullOrUserIdOrderByName(userId: Long): List<ActivityCategory>
}
