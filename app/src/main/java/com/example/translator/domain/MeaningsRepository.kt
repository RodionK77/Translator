package com.example.translator.domain

import com.example.translator.domain.models.Response

interface MeaningsRepository {

    suspend fun getMeanings(search: String): List<Response>
}