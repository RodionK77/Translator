package com.example.translator.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.translator.domain.models.TranslationFavoritesEntity
import com.example.translator.domain.models.TranslationHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationsDAO {


    @Query("DELETE FROM translation_history WHERE id = :id")
    suspend fun deleteTranslationFromHistoryById(id: Int)

    @Query("SELECT * FROM translation_history ORDER BY id DESC")
    fun getAllHistory(): Flow<List<TranslationHistoryEntity>>

    @Insert(entity = TranslationHistoryEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTranslationToHistory(item: TranslationHistoryEntity)


    @Query("DELETE FROM translation_favorites WHERE id = :id")
    suspend fun deleteTranslationFromFavoritesById(id: Int)

    @Query("SELECT * FROM translation_favorites")
    fun getAllFavorites(): Flow<List<TranslationFavoritesEntity>>

    @Insert(entity = TranslationFavoritesEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTranslationToFavorites(item: TranslationFavoritesEntity)

}