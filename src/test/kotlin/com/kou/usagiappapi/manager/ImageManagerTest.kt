package com.kou.usagiappapi.manager

import com.cloudinary.Cloudinary
import com.cloudinary.Url
import com.kou.usagiappapi.IntegrationTestSupport
import com.kou.usagiappapi.manager.image.ImageManager
import com.kou.usagiappapi.manager.image.ImageMissingException
import com.kou.usagiappapi.property.CloudinaryProperties
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile

class ImageManagerTest(
    private val cloudinaryProperties: CloudinaryProperties,
) : IntegrationTestSupport(
        {

            lateinit var manager: ImageManager
            val cloudinary = mockk<Cloudinary>()
            val uploader = mockk<com.cloudinary.Uploader>()
            val urlBuilder = mockk<Url>()

            beforeEach {
                every { cloudinary.uploader() } returns uploader
                every { cloudinary.url() } returns urlBuilder
                every { urlBuilder.transformation(any()) } returns urlBuilder
                manager = ImageManager(cloudinary)
            }

            describe("이미지 업로드") {

                context("요청 파일이 비어있으면") {
                    it("ImageMissingException을 발생시킨다.") {
                        val emptyFile =
                            mockk<MultipartFile> {
                                every { isEmpty } returns true
                            }

                        shouldThrow<ImageMissingException> {
                            manager.uploadImage(emptyFile, cloudinaryProperties.folder.profile, 200, 200)
                        }
                    }
                }

                context("정상적인 이미지 파일이 주어지면") {
                    it("이미지를 업로드하고 url과 publicId를 반환한다.") {
                        val file =
                            MockMultipartFile(
                                "image",
                                "test.png",
                                "image/png",
                                "fake-image".toByteArray(),
                            )

                        val publicId = "couple-app/profile/abc123"
                        val transformedUrl = "https://res.cloudinary.com/demo/image/upload/w_200,h_200/$publicId"

                        every {
                            uploader.upload(any<ByteArray>(), any())
                        } returns mapOf("public_id" to publicId)

                        every {
                            urlBuilder.generate(publicId)
                        } returns transformedUrl

                        val result = manager.uploadImage(file, cloudinaryProperties.folder.profile, 200, 200)

                        result.url shouldBe transformedUrl
                        result.publicId shouldBe publicId
                    }
                }
            }
        },
    )
