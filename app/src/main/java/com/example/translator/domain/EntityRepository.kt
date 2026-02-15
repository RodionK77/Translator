package com.example.translator.domain

import com.example.translator.domain.models.WordItem
import kotlinx.coroutines.flow.Flow

interface EntityRepository {

    fun getAllEntities(): Flow<List<WordItem>>

    fun getFavoritesEntities(): Flow<List<WordItem>>

    suspend fun deleteEntityById(id: Int)

    suspend fun removeEntityFromFavoritesById(id: Int)

    suspend fun removeEntityFromHistoryById(id: Int)

    suspend fun saveEntityToHistory(item: WordItem)

    suspend fun saveEntityToFavoritesById(id: Int)

}