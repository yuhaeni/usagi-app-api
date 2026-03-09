package com.kou.kouappapi.service

import com.kou.kouappapi.enums.Emotion
import com.kou.kouappapi.enums.EnumType
import com.kou.kouappapi.service.dto.GetEnumsResponseDto
import org.springframework.stereotype.Service

@Service
class SystemService {
    private val enumsRegistry =
        mapOf<String, List<EnumType>>(
            "EMOTION" to Emotion.entries,
        )

    fun getEnums(type: String): List<GetEnumsResponseDto> {
        val entries =
            enumsRegistry[type.uppercase()]
                // TODO 커스텀 예외처리 필요
                ?: throw IllegalArgumentException("지원하지 않는 Enum 타입입니다: $type")

        return entries.map {
            GetEnumsResponseDto(name = it.name, description = it.description)
        }
    }
}
