package com.kou.usagiappapi.diary.repository

import com.kou.usagiappapi.diary.entity.Diary
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface DiaryRepository : JpaRepository<Diary, Long> {
    fun findDiariesByUserIdAndDate(
        userId: Long,
        date: LocalDate,
    ): List<Diary>

    fun findByUserIdAndDateBetweenOrderByDateAsc(
        userId: Long,
        dateAfter: LocalDate,
        dateBefore: LocalDate,
    ): MutableList<Diary>
}
