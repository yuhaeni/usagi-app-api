package com.kou.kouappapi.service.dto

import com.kou.kouappapi.controller.dto.GetEnumsResponse

data class GetEnumsResponseDto(
    val name: String,
    val description: String,
)

fun List<GetEnumsResponseDto>.toResponse(): List<GetEnumsResponse> =
    map {
        GetEnumsResponse(
            name = it.name,
            description = it.description,
        )
    }
