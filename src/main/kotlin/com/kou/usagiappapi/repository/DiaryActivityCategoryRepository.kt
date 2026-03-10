package com.kou.usagiappapi.repository

import com.kou.usagiappapi.entity.DiaryActivityCategory
import org.springframework.data.jpa.repository.JpaRepository

interface DiaryActivityCategoryRepository : JpaRepository<DiaryActivityCategory, Long>
