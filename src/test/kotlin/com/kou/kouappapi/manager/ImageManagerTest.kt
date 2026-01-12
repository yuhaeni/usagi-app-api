package com.kou.kouappapi.manager

import com.cloudinary.Cloudinary
import com.kou.kouappapi.manager.image.ImageManager
import com.kou.kouappapi.manager.image.InvalidImageFileException
import com.kou.kouappapi.property.CloudinaryProperties
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile

class ImageManagerTest(
    private val cloudinaryProperties: CloudinaryProperties
) :
    DescribeSpec(
        {

            val cloudinary = mockk<Cloudinary>()
            val uploader = mockk<com.cloudinary.Uploader>()
            lateinit var manager: ImageManager

            beforeEach {
                every { cloudinary.uploader() } returns uploader
                manager = ImageManager(cloudinary)
            }

            describe("이미지 업로드") {

                context("요청 파일이 비어있으면") {
                    it("InvalidImageFileException을 발생시킨다.") {
                        val emptyFile =
                            mockk<MultipartFile> {
                                every { isEmpty } returns true
                            }

                        shouldThrow<InvalidImageFileException> {
                            manager.uploadImage(emptyFile, cloudinaryProperties.folder.profile)
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

                        every {
                            uploader.upload(any<ByteArray>(), any())
                        } returns
                            mapOf(
                                "secure_url" to "https://cloudinary.com/test.png",
                                "public_id" to "couple-app/profile/test",
                            )

                        val result = manager.uploadImage(file, cloudinaryProperties.folder.profile)

                        result.url shouldBe "https://cloudinary.com/test.png"
                        result.publicId shouldBe "couple-app/profile/test"
                    }
                }
            }
        },
    )
