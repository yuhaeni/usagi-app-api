package com.kou.usagiappapi.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"

        return OpenAPI()
            .info(
                Info()
                    .title("Usagi App API")
                    .version("1.0")
                    .description(
                        """
                        ## 인증 방법
                        1. `/api/auth/google/login` 으로 로그인하여 JWT 토큰 발급
                        2. 응답의 `accessToken` 복사
                        3. 우측 상단 🔓 **Authorize** 버튼 클릭
                        4. `{토큰}` 형식으로 입력 (Bearer 제외)
                        5. **Authorize** 클릭
                        6. 이제 인증이 필요한 API 호출 가능! 🚀
                        """.trimIndent(),
                    ),
            ).components(
                Components()
                    .addSecuritySchemes(
                        securitySchemeName,
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .`in`(SecurityScheme.In.HEADER)
                            .name("Authorization")
                            .description("JWT 토큰을 입력하세요 (Bearer 제외)"),
                    ),
            ).addSecurityItem(
                SecurityRequirement().addList(securitySchemeName),
            )
    }
}
