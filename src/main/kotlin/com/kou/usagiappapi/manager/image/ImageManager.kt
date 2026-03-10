package com.kou.usagiappapi.manager.image

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
        width: Int,
        height: Int,
    ): ImageUploadResult {
        if (file.isEmpty) {
            throw ImageMissingException()
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
            url = getImageUrl(uploadResult["public_id"] as String, width, height),
            publicId = uploadResult["public_id"] as String,
        )
    }

    fun getImageUrl(
        publicId: String,
        width: Int,
        height: Int,
    ): String? =
        cloudinary
            .url()
            .transformation(
                Transformation<Transformation<*>>()
                    .width(width)
                    .height(height)
                    .quality("auto")
                    .fetchFormat("auto"),
            ).generate(publicId)

    fun deleteImage(publicId: String) {
        val result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap())
        if (result["result"] != "ok") {
            throw ImageDeleteFailedException()
        }
    }
}
