package com.kou.kouappapi.enums

enum class Role {
    USER,
    ADMIN,
    ;

    val value: String
        get() = "ROLE_$name"
}
