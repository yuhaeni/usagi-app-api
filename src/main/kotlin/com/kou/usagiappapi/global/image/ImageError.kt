package com.kou.usagiappapi.global.image

import org.springframework.http.HttpStatus

enum class ImageError(
    val status: HttpStatus,
    val message: String,
) {
    IMAGE_MISSING(HttpStatus.BAD_REQUEST, "이미지 파일이 존재하지 않습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
    IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제에 실패했습니다."),
}
