package com.kou.kouappapi.entity

import com.kou.kouappapi.enums.CoupleStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Couple(
    @Enumerated(EnumType.STRING)
    var status: CoupleStatus = CoupleStatus.CONNECTED,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
