package com.kou.usagiappapi.diary.entity

import com.kou.usagiappapi.entity.BaseEntity
import com.kou.usagiappapi.entity.DiaryActivityCategory
import com.kou.usagiappapi.enums.Emotion
import com.kou.usagiappapi.user.entity.User
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
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

    @OneToMany(mappedBy = "diary", orphanRemoval = true, cascade = [CascadeType.ALL])
    var diaryActivityCategories: MutableList<DiaryActivityCategory> = mutableListOf()
        protected set

    fun update(
        emotion: Emotion? = null,
        imageId: String? = null,
        content: String? = null,
        deleteImage: Boolean = false,
    ) {
        emotion?.let { this.emotion = it }

        content?.let {
            if (it.isNotBlank()) {
                this.content = it
            } else {
                this.content = null
            }
        }

        if (deleteImage) {
            this.imageId = null
        }
        imageId?.let { this.imageId = it }
    }
}
