package com.example.translator.data.Room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_entity")
data class Entity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "english_word")
    val englishWord: String,

    @ColumnInfo(name = "russian_word")
    val russianWord: String,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "is_history")
    val isHistory: Boolean = true

)