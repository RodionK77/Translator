package com.example.translator.domain.useCases

import com.example.translator.domain.EntityRepository
import javax.inject.Inject

class RemoveEntityFromFavoritesByIdUseCase @Inject constructor(private val repository: EntityRepository) {

    suspend operator fun invoke(id: Int) {
        repository.removeEntityFromFavoritesById(id)
    }
}