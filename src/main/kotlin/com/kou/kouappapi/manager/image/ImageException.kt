package com.kou.kouappapi.manager.image

open class ImageException(
    val error: ImageError,
) : RuntimeException()

class InvalidImageFileException : ImageException(ImageError.IMAGE_INVALID_FORMAT)

class ImageNotFoundException : ImageException(ImageError.IMAGE_NOT_FOUND)

class ImageUploadFailedException : ImageException(ImageError.IMAGE_UPLOAD_FAILED)
