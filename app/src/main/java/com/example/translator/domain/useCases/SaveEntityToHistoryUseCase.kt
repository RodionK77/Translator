package com.example.translator.domain.useCases

import com.example.translator.domain.EntityRepository
import com.example.translator.domain.models.WordItem
import javax.inject.Inject

class SaveEntityToHistoryUseCase @Inject constructor(private val repository: EntityRepository) {

    suspend operator fun invoke(item: WordItem) {
        repository.saveEntityToHistory(item)
    }
}