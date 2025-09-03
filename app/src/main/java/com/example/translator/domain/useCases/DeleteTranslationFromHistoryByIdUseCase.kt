package com.example.translator.domain.useCases

import com.example.translator.domain.TranslationsRepository
import javax.inject.Inject

class DeleteTranslationFromHistoryByIdUseCase @Inject constructor(private val repository: TranslationsRepository) {

    suspend operator fun invoke(id: Int)  {
        repository.deleteTranslationFromHistoryById(id)
    }
}