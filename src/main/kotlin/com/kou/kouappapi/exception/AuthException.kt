package com.kou.kouappapi.exception

import com.kou.kouappapi.enums.AuthError

open class AuthException(
    authError: AuthError,
) : GlobalException(status = authError.status, message = authError.message)

class AuthProviderNotSupportedException : AuthException(AuthError.AUTH_PROVIDER_NOT_SUPPORTED)

class AuthEmailNotVerifiedException : AuthException(AuthError.AUTH_EMAIL_NOT_VERIFIED)

class AuthInvalidIdTokenException : AuthException(AuthError.AUTH_INVALID_ID_TOKEN)

class AuthGenerateTokenFailedException : AuthException(AuthError.AUTH_GENERATE_TOKEN_FAILED)

class AuthTokenExpiredException : AuthException(AuthError.AUTH_TOKEN_EXPIRED)

class AuthUnauthorizedTokenAccessException : AuthException(AuthError.AUTH_UNAUTHORIZED_TOKEN_ACCESS)

class AuthLoginRequiredException : AuthException(AuthError.AUTH_LOGIN_REQUIRED)
