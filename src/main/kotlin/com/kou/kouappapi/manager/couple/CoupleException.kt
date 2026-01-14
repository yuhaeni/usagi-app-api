package com.kou.kouappapi.manager.couple

open class CoupleException(
    val error: CoupleError,
) : RuntimeException()

class CoupleAlreadyInviteRequestException : CoupleException(CoupleError.COUPLE_ALREADY_INVITE_REQUEST)

class CoupleNotFoundInviteRequestException : CoupleException(CoupleError.COUPLE_NOT_FOUND_INVITE_REQUEST)

class CoupleAlreadyConnectionException : CoupleException(CoupleError.COUPLE_ALREADY_CONNECTION)
