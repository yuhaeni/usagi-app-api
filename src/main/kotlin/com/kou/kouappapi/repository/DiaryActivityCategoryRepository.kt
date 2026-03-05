package com.kou.kouappapi.repository

import com.kou.kouappapi.entity.DiaryActivityCategory
import org.springframework.data.jpa.repository.JpaRepository

interface DiaryActivityCategoryRepository : JpaRepository<DiaryActivityCategory, Long>
