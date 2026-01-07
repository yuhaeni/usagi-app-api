package com.kou.kouappapi.enums

enum class LoginNextStep(
    val description: String?,
) {
    PROFILE_SETUP("최초 로그인 후 필수 프로필 정보 입력"),
    COUPLE_INVITE("커플 초대 코드 입력 필요"), //
    WAITING_PARTNER("상대방 가입 대기 상태"),
    COUPLE_SETUP("커플 기본 설정 단계"),
    ACCOUNT_WITHDRAWN("탈퇴한 회원"),
}
