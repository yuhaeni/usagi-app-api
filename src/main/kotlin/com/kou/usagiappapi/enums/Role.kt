package com.kou.usagiappapi.enums

enum class Role {
    USER,
    ADMIN,
    ;

    val value: String
        get() = "ROLE_$name"
}
