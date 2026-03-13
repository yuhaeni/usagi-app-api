package com.kou.usagiappapi.global.image

import com.kou.usagiappapi.exception.GlobalException

open class ImageException(
    error: ImageError,
) : GlobalException(status = error.status, message = error.message)

class ImageMissingException : ImageException(ImageError.IMAGE_MISSING)

class ImageNotFoundException : ImageException(ImageError.IMAGE_NOT_FOUND)

class ImageUploadFailedException : ImageException(ImageError.IMAGE_UPLOAD_FAILED)

class ImageDeleteFailedException : ImageException(ImageError.IMAGE_DELETE_FAILED)
