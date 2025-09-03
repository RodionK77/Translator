package com.example.translator.domain

import com.example.translator.domain.models.TranslationFavoritesEntity
import com.example.translator.domain.models.TranslationHistoryEntity
import kotlinx.coroutines.flow.Flow

interface TranslationsRepository {


    suspend fun deleteTranslationFromHistoryById(id: Int)

    fun getAllHistory(): Flow<List<TranslationHistoryEntity>>

    suspend fun saveTranslationToHistory(item: TranslationHistoryEntity)

    suspend fun deleteTranslationFromFavoritesById(id: Int)

    fun getAllFavorites(): Flow<List<TranslationFavoritesEntity>>

    suspend fun saveTranslationToFavorites(item: TranslationFavoritesEntity)

}