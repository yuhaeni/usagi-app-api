package com.kou.usagiappapi.activityCategory.exception

import com.kou.usagiappapi.exception.GlobalException
import org.springframework.http.HttpStatus

open class ActivityCategoryException(
    message: String,
    status: HttpStatus,
) : GlobalException(status = status, message = message)

class ActivityCategoryNotFoundException :
    ActivityCategoryException(status = HttpStatus.NOT_FOUND, message = "존재하지 않는 활동 카테고리 입니다.")
