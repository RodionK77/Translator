package com.example.translator.data

import com.example.translator.domain.MeaningsRepository
import com.example.translator.domain.models.WordItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeaningsRepositoryImpl@Inject constructor(private val meaningsAPI: MeaningsAPI) : MeaningsRepository {

    override suspend fun getMeanings(search: String): List<WordItem>{
        val responseList = meaningsAPI.getMeanings(search = search)
        return responseList.mapNotNull { it.toDomain() }
    }
}