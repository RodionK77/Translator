package com.example.translator.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.translator.data.Room.entities.Entity
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationsDAO {

    @Query("SELECT * FROM translation_entity WHERE is_history = 1 ORDER BY id DESC")
    fun getAllEntities(): Flow<List<Entity>>

    @Query("SELECT * FROM translation_entity WHERE is_favorite = 1")
    fun getFavoritesEntities(): Flow<List<Entity>>

    @Query("UPDATE translation_entity SET is_favorite = 0 WHERE id = :id")
    suspend fun removeEntityFromFavoritesById(id: Int)

    @Query("UPDATE translation_entity SET is_history = 0 WHERE id = :id")
    suspend fun removeEntityFromHistoryById(id: Int)

    @Insert(entity = Entity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEntityToHistory(item: Entity)

    @Query("UPDATE translation_entity SET is_favorite = 1 WHERE id = :id")
    suspend fun saveEntityToFavoritesById(id: Int)

    @Query("DELETE FROM translation_entity WHERE id = :id")
    suspend fun deleteEntityById(id: Int)

    @Query("DELETE FROM translation_entity WHERE is_history = 0 AND is_favorite = 0")
    suspend fun deleteOrphans()

}