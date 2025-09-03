package com.example.translator.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_history")
data class TranslationHistoryEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    override val id: Int = 0,

    @ColumnInfo(name = "english_word")
    override val englishWord: String,

    @ColumnInfo(name = "russian_word")
    override val russianWord: String
) : TranslationEntity