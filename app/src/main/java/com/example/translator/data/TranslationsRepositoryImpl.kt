package com.example.translator.data

import com.example.translator.data.Room.TranslationsDatabase
import com.example.translator.domain.TranslationsRepository
import com.example.translator.domain.models.WordItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationsRepositoryImpl @Inject constructor(database: TranslationsDatabase) : TranslationsRepository {

    private val dao = database.translationsDao()

    override suspend fun deleteTranslationFromHistoryById(id: Int){
        dao.deleteTranslationFromHistoryById(id)
    }

    override fun getAllHistory(): Flow<List<WordItem>> {
        val history = dao.getAllHistory()
            .map { entities ->
                entities.map { it.toDomain() }
            }
        return history
    }

    override suspend fun saveTranslationToHistory(item: WordItem) {
        dao.saveTranslationToHistory(item.toHistoryEntity())
    }


    override suspend fun deleteTranslationFromFavoritesById(id: Int) {
        dao.deleteTranslationFromFavoritesById(id)
    }

    override fun getAllFavorites(): Flow<List<WordItem>> {
        val favorites = dao.getAllFavorites()
            .map { entities ->
                entities.map { it.toDomain() }
            }
        return favorites
    }

    override suspend fun saveTranslationToFavorites(item: WordItem) {
        dao.saveTranslationToFavorites(item.toFavoritesEntity())
    }

}