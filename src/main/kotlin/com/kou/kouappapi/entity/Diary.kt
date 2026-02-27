package com.kou.kouappapi.entity

import com.kou.kouappapi.enums.Emotion
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDate

@Entity
class Diary(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    val date: LocalDate,
    var emotion: Emotion,
    var imageId: String? = null,
    var content: String? = null,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    fun update(
        emotion: Emotion? = null,
        imageId: String? = null,
        content: String? = null,
        deleteImage: Boolean = false,
    ) {
        if (deleteImage) {
            this.imageId = null
        }

        emotion?.let { this.emotion = it }
        imageId?.let { this.imageId = it }
        content?.let { this.content = it }
    }
}
