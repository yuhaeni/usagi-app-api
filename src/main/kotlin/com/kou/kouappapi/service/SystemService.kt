package com.kou.kouappapi.service

import com.kou.kouappapi.enums.Emotion
import com.kou.kouappapi.enums.EnumType
import com.kou.kouappapi.service.dto.GetEnumsResponseDto
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class SystemService {
    private val enumsRegistry =
        mapOf<String, Array<out EnumType>>(
            "EMOTION" to Emotion.entries.toTypedArray(),
        )

    fun getEnums(type: String): List<GetEnumsResponseDto> {
        val entries =
            enumsRegistry[type.uppercase()]
                // TODO 커스텀 예외처리 필요
                ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown enum type: $type")

        return entries.map {
            GetEnumsResponseDto(name = (it as Enum<*>).name, description = it.description)
        }
    }
}
