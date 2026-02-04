package com.example.translator.domain

import com.example.translator.domain.models.WordItem

interface MeaningsRepository {

    suspend fun getMeanings(search: String): List<WordItem>
}