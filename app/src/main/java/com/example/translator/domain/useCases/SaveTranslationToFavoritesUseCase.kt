package com.example.translator.domain.useCases

import com.example.translator.domain.TranslationsRepository
import com.example.translator.domain.models.WordItem
import javax.inject.Inject

class SaveTranslationToFavoritesUseCase @Inject constructor(private val repository: TranslationsRepository) {

    suspend operator fun invoke(item: WordItem)  {
        repository.saveTranslationToFavorites(item)
    }
}