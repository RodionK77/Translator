package com.example.translator.domain

import com.example.translator.domain.models.WordItem
import kotlinx.coroutines.flow.Flow

interface TranslationsRepository {


    suspend fun deleteTranslationFromHistoryById(id: Int)

    fun getAllHistory(): Flow<List<WordItem>>

    suspend fun saveTranslationToHistory(item: WordItem)

    suspend fun deleteTranslationFromFavoritesById(id: Int)

    fun getAllFavorites(): Flow<List<WordItem>>

    suspend fun saveTranslationToFavorites(item: WordItem)

}