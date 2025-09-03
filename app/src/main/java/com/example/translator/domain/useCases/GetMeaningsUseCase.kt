package com.example.translator.domain.useCases

import com.example.translator.domain.MeaningsRepository
import com.example.translator.domain.models.Response
import javax.inject.Inject

class GetMeaningsUseCase @Inject constructor(private val repository: MeaningsRepository) {

    suspend operator fun invoke(search: String): List<Response> {
        return repository.getMeanings(search)
    }
}