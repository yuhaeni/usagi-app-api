package com.kou.usagiappapi.global.security.jwt

import com.kou.usagiappapi.global.redis.RedisManager
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisManager: RedisManager,
) : OncePerRequestFilter() {
    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val accessToken = resolveToken(request)
        if (
            accessToken != null &&
            jwtTokenProvider.validateToken(accessToken)
        ) {
            if (redisManager.isBlackList(accessToken)) {
                filterChain.doFilter(request, response)
                return
            }

            val authUser = jwtTokenProvider.getAuthUser(accessToken)
            val authorities = listOf(SimpleGrantedAuthority("ROLE_${authUser.role}"))
            val authentication =
                UsernamePasswordAuthenticationToken(
                    authUser,
                    null,
                    authorities,
                ).apply {
                    details = WebAuthenticationDetailsSource().buildDetails(request)
                }

            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? =
        request
            .getHeader(AUTHORIZATION_HEADER)
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.removePrefix(BEARER_PREFIX)
}
