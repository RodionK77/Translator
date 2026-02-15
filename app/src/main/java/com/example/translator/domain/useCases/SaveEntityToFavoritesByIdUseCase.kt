package com.example.translator.domain.useCases

import com.example.translator.domain.EntityRepository
import javax.inject.Inject

class SaveEntityToFavoritesByIdUseCase @Inject constructor(private val repository: EntityRepository) {

    suspend operator fun invoke(id: Int) {
        repository.saveEntityToFavoritesById(id)
    }
}