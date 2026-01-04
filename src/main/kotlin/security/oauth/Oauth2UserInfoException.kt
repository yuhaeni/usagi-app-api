package security.oauth

import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error

open class Oauth2UserInfoException(
    errorCode: String,
) : OAuth2AuthenticationException(OAuth2Error(errorCode))

class UnsupportedProviderException : Oauth2UserInfoException(OAuth2UserInfoErrorCodes.UNSUPPORTED_PROVIDER)

class MissingUserInfoException : Oauth2UserInfoException(OAuth2UserInfoErrorCodes.MISSING_USER_INFO)

class UnverifiedEmailException : Oauth2UserInfoException(OAuth2UserInfoErrorCodes.UNVERIFIED_EMAIL)

class EmailAlreadyExistsException : Oauth2UserInfoException(OAuth2UserInfoErrorCodes.EMAIL_ALREADY_EXISTS)
