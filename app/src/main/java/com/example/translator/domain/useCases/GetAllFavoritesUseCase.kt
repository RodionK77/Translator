package com.example.translator.domain.useCases

import com.example.translator.domain.TranslationsRepository
import com.example.translator.domain.models.TranslationFavoritesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFavoritesUseCase @Inject constructor(private val repository: TranslationsRepository) {

    operator fun invoke() : Flow<List<TranslationFavoritesEntity>> {
        return repository.getAllFavorites()
    }
}