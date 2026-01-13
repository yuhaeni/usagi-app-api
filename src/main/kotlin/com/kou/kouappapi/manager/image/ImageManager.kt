package com.kou.kouappapi.manager.image

import com.cloudinary.Cloudinary
import com.cloudinary.Transformation
import com.cloudinary.utils.ObjectUtils
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ImageManager(
    private val cloudinary: Cloudinary,
) {
    fun uploadImage(
        file: MultipartFile,
        uploadFolder: String,
    ): ImageUploadResult {
        if (file.isEmpty) {
            throw InvalidImageFileException()
        }

        val params =
            ObjectUtils.asMap(
                "folder",
                uploadFolder,
                "unique_filename",
                true,
            )

        val uploadResult =
            cloudinary.uploader().upload(file.bytes, params)
                ?: throw ImageUploadFailedException()
        return ImageUploadResult(
            url = getProfileImageUrl(uploadResult["public_id"] as String),
            publicId = uploadResult["public_id"] as String,
        )
    }

    fun getProfileImageUrl(publicId: String): String =
        cloudinary
            .url()
            .transformation(
                Transformation<Transformation<*>>()
                    .width(200)
                    .height(200)
                    .crop("fill")
                    .gravity("face")
                    .radius("max")
                    .fetchFormat("png"),
            ).generate(publicId)
}
