package com.example.translator.data

import com.example.translator.data.Room.TranslationsDatabase
import com.example.translator.domain.TranslationsRepository
import com.example.translator.domain.models.TranslationFavoritesEntity
import com.example.translator.domain.models.TranslationHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationsRepositoryImpl @Inject constructor(database: TranslationsDatabase) : TranslationsRepository {

    private val dao = database.translationsDao()

    override suspend fun deleteTranslationFromHistoryById(id: Int){
        dao.deleteTranslationFromHistoryById(id)
    }

    override fun getAllHistory(): Flow<List<TranslationHistoryEntity>> {
        return dao.getAllHistory()
    }

    override suspend fun saveTranslationToHistory(item: TranslationHistoryEntity) {
        dao.saveTranslationToHistory(item)
    }


    override suspend fun deleteTranslationFromFavoritesById(id: Int) {
        dao.deleteTranslationFromFavoritesById(id)
    }

    override fun getAllFavorites(): Flow<List<TranslationFavoritesEntity>> {
        return dao.getAllFavorites()
    }

    override suspend fun saveTranslationToFavorites(item: TranslationFavoritesEntity) {
        dao.saveTranslationToFavorites(item)
    }

}