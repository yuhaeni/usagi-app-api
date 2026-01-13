package com.kou.kouappapi.manager

import com.cloudinary.Cloudinary
import com.kou.kouappapi.manager.image.ImageManager
import com.kou.kouappapi.manager.image.ImageNotFoundException
import com.kou.kouappapi.property.CloudinaryProperties
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Disabled
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@Disabled("Cloudinary 실제 업로드 테스트 - 수동 실행용")
class ImageManagerIntegrationTest(
    private val imageManager: ImageManager,
    private val cloudinaryProperties: CloudinaryProperties,
) : DescribeSpec({

    describe("Cloudinary 실제 업로드 테스트") {
        it("이미지를 실제로 업로드하고 URL과 publicId를 반환한다") {
            val inputStream =
                javaClass.classLoader
                    .getResourceAsStream("images/꽃을든_치이카와.jpeg")
                    ?: throw ImageNotFoundException()

            val file = MockMultipartFile(
                "image",
                "integration-test.jpeg",
                "image/jpeg",
                inputStream
            )

            val result = imageManager.uploadImage(file, cloudinaryProperties.folder.profile)

            result.url shouldStartWith "https://res.cloudinary.com"
            result.publicId shouldContain "kou-app/test/individual/profile"
        }
    }
})
