package security.oauth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import security.jwt.JwtTokenProvider

@Component
class OAuth2AuthenticationSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
) : SimpleUrlAuthenticationSuccessHandler() {
    // TODO 신규 회원과 기존 회원을 구분해서 다른 페이지로 리디렉션
//    @Value("\${app.oauth2.redirect-uri.main}")
    private lateinit var mainRedirectUri: String

//    @Value("\${app.oauth2.redirect-uri.signup}")
    private lateinit var signupRedirectUri: String

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val principal = authentication.principal as OAuth2UserPrincipal
        val user = principal.user

        val accessToken = jwtTokenProvider.generateAccessToken(user.id, user.email, user.role)
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.id, user.email, user.role)

        // TODO 신규 회원이면 회원가입 페이지로, 기존 회원이면 메인 페이지로
        val baseUri = if (user.isRegistrationCompleted) signupRedirectUri else mainRedirectUri

        val targetUrl =
            UriComponentsBuilder
                .fromUriString(baseUri)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build()
                .toUriString()

        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}
