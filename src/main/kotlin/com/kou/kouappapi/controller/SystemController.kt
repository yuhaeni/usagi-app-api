package com.kou.kouappapi.controller

import com.kou.kouappapi.controller.dto.GetEnumsResponse
import com.kou.kouappapi.service.SystemService
import com.kou.kouappapi.service.dto.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "⚙️시스템")
@RestController
@RequestMapping("/api/v1/system")
class SystemController(
    val service: SystemService,
) {
    @Operation(summary = "상수 조회")
    @GetMapping("/enum")
    fun getEnums(
        @Schema(
            description = "조회할 상수 타입",
            allowableValues = ["EMOTION"],
            example = "EMOTION",
        )
        @RequestParam("type") type: String,
    ): List<GetEnumsResponse> = service.getEnums(type).toResponse()
}
