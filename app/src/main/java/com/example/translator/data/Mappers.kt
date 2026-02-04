package com.example.translator.data

import com.example.translator.data.Room.entities.TranslationFavoritesEntity
import com.example.translator.data.Room.entities.TranslationHistoryEntity
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

fun TranslationHistoryEntity.toDomain(): WordItem {
    return WordItem(
        id = this.id,
        text = this.englishWord,
        translation = this.russianWord,
        isFavorite = false
    )
}

fun WordItem.toHistoryEntity(): TranslationHistoryEntity {
    return TranslationHistoryEntity(
        id = 0,
        englishWord = this.text,
        russianWord = this.translation,
    )
}

fun TranslationFavoritesEntity.toDomain(): WordItem {
    return WordItem(
        id = this.id,
        text = this.englishWord,
        translation = this.russianWord,
        isFavorite = true
    )
}

fun WordItem.toFavoritesEntity(): TranslationFavoritesEntity {
    return TranslationFavoritesEntity(
        id = this.id,
        englishWord = this.text,
        russianWord = this.translation
    )
}