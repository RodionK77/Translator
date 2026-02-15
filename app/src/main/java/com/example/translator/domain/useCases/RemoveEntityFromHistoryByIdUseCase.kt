package com.example.translator.domain.useCases

import com.example.translator.domain.EntityRepository
import javax.inject.Inject

class RemoveEntityFromHistoryByIdUseCase @Inject constructor(private val repository: EntityRepository) {

    suspend operator fun invoke(id: Int) {
        repository.removeEntityFromHistoryById(id)
    }
}