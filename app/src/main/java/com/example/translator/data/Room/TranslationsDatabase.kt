package com.example.translator.data.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.translator.data.Room.entities.TranslationFavoritesEntity
import com.example.translator.data.Room.entities.TranslationHistoryEntity

@Database(
    entities = [TranslationHistoryEntity::class, TranslationFavoritesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TranslationsDatabase : RoomDatabase() {

    abstract fun translationsDao(): TranslationsDAO


}