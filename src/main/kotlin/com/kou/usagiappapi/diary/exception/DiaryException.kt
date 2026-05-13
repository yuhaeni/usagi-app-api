package com.kou.usagiappapi.diary.exception

import com.kou.usagiappapi.exception.GlobalException
import org.springframework.http.HttpStatus
import java.time.LocalDate

open class DiaryException(
    message: String,
    status: HttpStatus,
) : GlobalException(status = status, message = message)

// TODO enum으로 처리

class DiaryNotFoundException : DiaryException(status = HttpStatus.NOT_FOUND, message = "존재하지 않는 일기입니다.")

class DiaryAlreadyExistsException(
    date: LocalDate,
) : DiaryException(status = HttpStatus.CONFLICT, message = "${date}에 이미 일기가 존재합니다.")

class NotDiaryOwnerException : DiaryException(status = HttpStatus.FORBIDDEN, message = "접근 권한이 없습니다.")
