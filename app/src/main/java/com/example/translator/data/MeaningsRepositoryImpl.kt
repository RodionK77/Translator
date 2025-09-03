package com.example.translator.data

import com.example.translator.domain.MeaningsRepository
import com.example.translator.domain.models.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeaningsRepositoryImpl@Inject constructor(private val meaningsAPI: MeaningsAPI) : MeaningsRepository {

    override suspend fun getMeanings(search: String): List<Response>{
        return meaningsAPI.getMeanings(search = search)
    }
}