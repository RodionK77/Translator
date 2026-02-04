package com.example.translator.domain.useCases

import com.example.translator.domain.TranslationsRepository
import com.example.translator.domain.models.WordItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFavoritesUseCase @Inject constructor(private val repository: TranslationsRepository) {

    operator fun invoke() : Flow<List<WordItem>> {
        return repository.getAllFavorites()
    }
}