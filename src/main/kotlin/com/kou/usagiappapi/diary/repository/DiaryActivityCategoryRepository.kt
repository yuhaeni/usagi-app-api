package com.kou.usagiappapi.diary.repository

import com.kou.usagiappapi.diary.entity.Diary
import com.kou.usagiappapi.diary.entity.DiaryActivityCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface DiaryActivityCategoryRepository : JpaRepository<DiaryActivityCategory, Long> {
    @Modifying
    @Query("delete from DiaryActivityCategory dac where dac.diary = :diary")
    fun deleteAllByDiary(diary: Diary): Int
}
