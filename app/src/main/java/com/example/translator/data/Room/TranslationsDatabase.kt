package com.example.translator.data.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.translator.data.Room.entities.Entity

@Database(
    entities = [Entity::class],
    version = 1,
    exportSchema = false
)
abstract class TranslationsDatabase : RoomDatabase() {

    abstract fun translationsDao(): TranslationsDAO


}