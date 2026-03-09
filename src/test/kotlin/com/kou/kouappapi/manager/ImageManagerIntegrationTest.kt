package com.kou.kouappapi.manager

import com.kou.kouappapi.IntegrationTestSupport
import com.kou.kouappapi.manager.image.ImageManager
import com.kou.kouappapi.manager.image.ImageNotFoundException
import com.kou.kouappapi.property.CloudinaryProperties
import io.kotest.core.annotation.Tags
import io.kotest.matchers.string.shouldContain
import org.springframework.mock.web.MockMultipartFile

@Tags("local-only")
class ImageManagerIntegrationTest(
    private val imageManager: ImageManager,
    private val cloudinaryProperties: CloudinaryProperties,
) : IntegrationTestSupport({

        describe("Cloudinary 실제 업로드 테스트") {
            it("이미지를 실제로 업로드하고 URL과 publicId를 반환한다") {
                val inputStream =
                    javaClass.classLoader
                        .getResourceAsStream("images/꽃을든_치이카와.jpeg")
                        ?: throw ImageNotFoundException()

                val file =
                    MockMultipartFile(
                        "image",
                        "integration-test.jpeg",
                        "image/jpeg",
                        inputStream,
                    )
                val result = imageManager.uploadImage(file, cloudinaryProperties.folder.profile, 200, 200)

                result.url shouldContain "res.cloudinary.com"
                result.publicId shouldContain "usagi-app/test/individual/profile"
            }
        }
    })
