package com.kou.kouappapi.exception

import java.time.LocalDate

open class DiaryException(
    message: String,
) : RuntimeException(message)

class DiaryNotFoundException : DiaryException("일기를 찾을 수 없습니다.")

class DiaryAlreadyExistsException(
    date: LocalDate,
) : DiaryException("${date}에 이미 일기가 존재합니다.")
