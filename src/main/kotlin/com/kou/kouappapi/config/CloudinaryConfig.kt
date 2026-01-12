package com.kou.kouappapi.config

import com.cloudinary.Cloudinary
import com.kou.kouappapi.property.CloudinaryProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CloudinaryConfig(
    private val cloudinaryProperties: CloudinaryProperties,
) {
    @Bean
    fun cloudinary(): Cloudinary {
        val config =
            mapOf(
                "cloud_name" to cloudinaryProperties.cloudName,
                "api_key" to cloudinaryProperties.apiKey,
                "api_secret" to cloudinaryProperties.apiSecret,
            )

        return Cloudinary(config)
    }
}
