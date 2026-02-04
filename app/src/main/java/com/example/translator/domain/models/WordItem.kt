package com.example.translator.domain.models

data class WordItem(
    val id: Int = 0,
    val text: String,
    val translation: String,
    val isFavorite: Boolean = false
)