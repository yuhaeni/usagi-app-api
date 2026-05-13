package com.kou.usagiappapi.diary.repository

import com.kou.usagiappapi.diary.entity.DiaryActivityCategory
import org.springframework.data.jpa.repository.JpaRepository

interface DiaryActivityCategoryRepository : JpaRepository<DiaryActivityCategory, Long>
