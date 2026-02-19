package com.kou.kouappapi.enums

enum class Emotion(
    override val description: String,
) : EnumType {
    GOOD("좋음"),
    NEUTRAL("보통"),
    HAPPY("행복"),
    TIRED("기운없음"),
    HOPELESS("자포자기"),
    BAD("기분안좋음"),
    PAINFUL("아픔"),
    ANGRY("화남"),
}
