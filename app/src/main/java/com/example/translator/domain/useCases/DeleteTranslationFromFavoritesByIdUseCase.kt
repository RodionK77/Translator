package com.example.translator.domain.useCases

import com.example.translator.domain.TranslationsRepository
import javax.inject.Inject

class DeleteTranslationFromFavoritesByIdUseCase @Inject constructor(private val repository: TranslationsRepository) {

    suspend operator fun invoke(id: Int)  {
        repository.deleteTranslationFromFavoritesById(id)
    }
}