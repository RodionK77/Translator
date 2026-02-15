package com.example.translator.data

import com.example.translator.data.Room.TranslationsDatabase
import com.example.translator.domain.EntityRepository
import com.example.translator.domain.models.WordItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntityRepositoryImpl @Inject constructor(database: TranslationsDatabase): EntityRepository {

    private val dao = database.translationsDao()

    override fun getAllEntities(): Flow<List<WordItem>> {
        return dao.getAllEntities().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFavoritesEntities(): Flow<List<WordItem>> {
        return dao.getFavoritesEntities().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteEntityById(id: Int) {
        dao.deleteEntityById(id)
    }

    override suspend fun removeEntityFromFavoritesById(id: Int) {
        dao.removeEntityFromFavoritesById(id)
        dao.deleteOrphans()
    }

    override suspend fun removeEntityFromHistoryById(id: Int) {
        dao.removeEntityFromHistoryById(id)
        dao.deleteOrphans()
    }

    override suspend fun saveEntityToHistory(item: WordItem) {
        dao.saveEntityToHistory(item.toEntity())
    }

    override suspend fun saveEntityToFavoritesById(id: Int) {
        dao.saveEntityToFavoritesById(id)
    }


}