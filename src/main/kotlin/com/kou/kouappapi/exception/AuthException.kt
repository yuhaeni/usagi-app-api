package com.kou.kouappapi.exception

import com.kou.kouappapi.enums.AuthError

open class AuthException(
    val error: AuthError,
) : RuntimeException()

class AuthProviderNotSupportedException : AuthException(AuthError.AUTH_PROVIDER_NOT_SUPPORTED)

class AuthProfileInfoMissingException : AuthException(AuthError.AUTH_REQUIRED_PROFILE_INFO_MISSING)

class AuthEmailNotVerifiedException : AuthException(AuthError.AUTH_EMAIL_NOT_VERIFIED)

class AuthEmailAlreadyRegisteredException : AuthException(AuthError.AUTH_EMAIL_ALREADY_REGISTERED)

class AuthInvalidIdTokenException : AuthException(AuthError.AUTH_INVALID_ID_TOKEN)

class AuthGenerateTokenFailedException : AuthException(AuthError.AUTH_GENERATE_TOKEN_FAILED)

class AuthTokenExpiredException : AuthException(AuthError.AUTH_TOKEN_EXPIRED)

class AuthUnauthorizedTokenAccessException : AuthException(AuthError.AUTH_UNAUTHORIZED_TOKEN_ACCESS)
