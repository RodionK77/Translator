package com.example.translator.domain.useCases

import com.example.translator.domain.EntityRepository
import com.example.translator.domain.models.WordItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllEntitiesUseCase @Inject constructor(private val repository: EntityRepository) {

    operator fun invoke(): Flow<List<WordItem>> {
        return repository.getAllEntities()
    }
}