package com.example.translator.data

import com.example.translator.data.Room.entities.Entity
import com.example.translator.data.dto.Response
import com.example.translator.domain.models.WordItem

fun Response.toDomain(): WordItem? {
    val firstMeaning = this.meanings.firstOrNull() ?: return null
    val translationText = firstMeaning.translation?.text ?: return null

    return WordItem(
        id = this.id ?: 0,
        text = this.text ?: "",
        translation = translationText,
    )
}


fun Entity.toDomain(): WordItem {
    return WordItem(
        id = this.id,
        text = this.englishWord,
        translation = this.russianWord,
        isFavorite = this.isFavorite,
        isHistory = this.isHistory
    )
}

fun WordItem.toEntity(
    forceHistory: Boolean? = null,
    forceFavorite: Boolean? = null
): Entity {
    return Entity(
        englishWord = this.text,
        russianWord = this.translation,
        isFavorite = forceFavorite ?: this.isFavorite,
        isHistory = forceHistory ?: this.isHistory
    )
}