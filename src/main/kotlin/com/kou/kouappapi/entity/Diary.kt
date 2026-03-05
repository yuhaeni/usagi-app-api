package com.kou.kouappapi.entity

import com.kou.kouappapi.enums.Emotion
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

    @OneToMany(mappedBy = "diary")
    var diaryActivityCategory: MutableList<DiaryActivityCategory> = mutableListOf()
        protected set

    fun update(
        emotion: Emotion? = null,
        imageId: String? = null,
        content: String? = null,
        deleteImage: Boolean = false,
        diaryActivityCategoryList: List<DiaryActivityCategory> = emptyList(),
    ) {
        emotion?.let { this.emotion = it }

        content?.let {
            if (it.isNotBlank()) {
                this.content = it
            } else {
                this.content = null
            }
        }

        if (deleteImage == true) {
            this.imageId = null
        }
        imageId?.let { this.imageId = it }

        if (diaryActivityCategoryList.isNotEmpty()) {
            this.diaryActivityCategory.clear()
            this.diaryActivityCategory.addAll(diaryActivityCategoryList)
        }
    }
}
