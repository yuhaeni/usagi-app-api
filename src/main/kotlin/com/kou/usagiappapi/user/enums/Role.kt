package com.kou.usagiappapi.user.enums

enum class Role {
    USER,
    ADMIN,
    ;

    val value: String
        get() = "ROLE_$name"
}
