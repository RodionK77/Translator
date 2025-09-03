package com.example.translator.domain.useCases

import com.example.translator.domain.TranslationsRepository
import com.example.translator.domain.models.TranslationHistoryEntity
import javax.inject.Inject

class SaveTranslationToHistoryUseCase @Inject constructor(private val repository: TranslationsRepository) {

    suspend operator fun invoke(item: TranslationHistoryEntity)  {
        repository.saveTranslationToHistory(item)
    }
}