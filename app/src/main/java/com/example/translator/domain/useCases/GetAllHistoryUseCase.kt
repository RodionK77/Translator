package com.example.translator.domain.useCases

import com.example.translator.domain.TranslationsRepository
import com.example.translator.domain.models.TranslationHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllHistoryUseCase @Inject constructor(private val repository: TranslationsRepository) {

    operator fun invoke(): Flow<List<TranslationHistoryEntity>> {
        return repository.getAllHistory()
    }

}