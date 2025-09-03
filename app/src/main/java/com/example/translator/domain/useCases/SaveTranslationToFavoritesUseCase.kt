package com.example.translator.domain.useCases

import com.example.translator.domain.TranslationsRepository
import com.example.translator.domain.models.TranslationFavoritesEntity
import javax.inject.Inject

class SaveTranslationToFavoritesUseCase @Inject constructor(private val repository: TranslationsRepository) {

    suspend operator fun invoke(item: TranslationFavoritesEntity)  {
        repository.saveTranslationToFavorites(item)
    }
}