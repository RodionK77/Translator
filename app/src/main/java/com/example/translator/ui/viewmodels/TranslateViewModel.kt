package com.example.translator.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator.R
import com.example.translator.domain.models.Meanings
import com.example.translator.domain.models.TranslationFavoritesEntity
import com.example.translator.domain.models.TranslationHistoryEntity
import com.example.translator.domain.useCases.DeleteTranslationFromFavoritesByIdUseCase
import com.example.translator.domain.useCases.DeleteTranslationFromHistoryByIdUseCase
import com.example.translator.domain.useCases.GetAllFavoritesUseCase
import com.example.translator.domain.useCases.GetAllHistoryUseCase
import com.example.translator.domain.useCases.GetMeaningsUseCase
import com.example.translator.domain.useCases.SaveTranslationToFavoritesUseCase
import com.example.translator.domain.useCases.SaveTranslationToHistoryUseCase
import com.example.translator.ui.resources.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TranslateUiState(
    val meanings: List<Meanings> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val translate: TranslationHistoryEntity? = null,
    val history: List<TranslationHistoryEntity> = emptyList(),
    val favorites: List<TranslationFavoritesEntity> = emptyList()
)

@HiltViewModel
class TranslateViewModel @Inject constructor (
    private val saveTranslationToHistoryUseCase: SaveTranslationToHistoryUseCase,
    private val saveTranslationToFavoritesUseCase: SaveTranslationToFavoritesUseCase,
    private val getAllHistoryUseCase: GetAllHistoryUseCase,
    private val getMeaningsUseCase: GetMeaningsUseCase,
    private val deleteTranslationFromHistoryByIdUseCase: DeleteTranslationFromHistoryByIdUseCase,
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase,
    private val deleteTranslationFromFavoritesByIdUseCase: DeleteTranslationFromFavoritesByIdUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel(){

    var uiState by mutableStateOf(TranslateUiState())
        private set

    init {
        observeHistory()
        observeFavorites()
    }


    fun clearError() {
        uiState = uiState.copy(error = null)
    }
    fun clearTranslate() {
        uiState = uiState.copy(translate = null)
    }

    private fun observeHistory() {
        viewModelScope.launch {
            getAllHistoryUseCase()
                .catch { e ->
                    uiState = uiState.copy(error = e.message)
                    Log.d("R", "История не загрузилась ${e.message}")
                }
                .collect { history ->
                    uiState = uiState.copy(history = history)
                }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getAllFavoritesUseCase()
                .catch { e ->
                    uiState = uiState.copy(error = e.message)
                    Log.d("R", "Избранное не загрузилось ${e.message}")
                }
                .collect { favorites ->
                    uiState = uiState.copy(favorites = favorites)
                }
        }
    }

    fun getMeanings(search: String){
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            kotlin.runCatching { getMeaningsUseCase(search) }
                .onSuccess {
                    if(it.isNotEmpty()){
                        uiState = uiState.copy(translate = TranslationHistoryEntity(
                            englishWord = search,
                            russianWord = it[0].meanings[0].translation?.text?:"")
                        )
                        Log.d("R", "Перевод: ${it[0].meanings[0].translation?.text}")
                        saveTranslationToHistory()
                    } else {
                        uiState = uiState.copy(
                            isLoading = false,
                            error = resourceProvider.getString(R.string.translate_not_found)
                        )
                        Log.d("R", "Перевод найти не удалось")
                    }
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "${resourceProvider.getString
                            (R.string.translate_not_received)}\n${it.message}"
                    )
                    Log.d("R", "Перевод получить не удалось: ${it.message}")
                }
        }
    }

    private fun saveTranslationToHistory(){
        viewModelScope.launch {
            kotlin.runCatching { uiState.translate?.let { saveTranslationToHistoryUseCase(it) } }
                .onSuccess {
                    uiState = uiState.copy(isLoading = false)
                    Log.d("R", "Перевод сохранен в историю")
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "${resourceProvider.getString
                            (R.string.not_save_to_history)}\n${it.message}")
                    Log.d("R", "Не удалось сохранить в историю\n${it.message}")
                }
        }
    }

    fun deleteTranslationFromHistory(id: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            kotlin.runCatching { deleteTranslationFromHistoryByIdUseCase(id) }
                .onSuccess {
                    uiState = uiState.copy(isLoading = false)
                    Log.d("R", "Запись с id=$id удалена из истории")
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "${resourceProvider.getString
                            (R.string.not_delete_from_history)}n${it.message}")
                    Log.d("R", "Не удалось удалить из истории: ${it.message}")
                }
        }
    }

    fun saveTranslationToFavorites(translate: TranslationFavoritesEntity){
        viewModelScope.launch {
            kotlin.runCatching { saveTranslationToFavoritesUseCase(translate) }
                .onSuccess {
                    uiState = uiState.copy(isLoading = false)
                    Log.d("R", "Перевод сохранен в избранное")
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "${resourceProvider.getString
                            (R.string.not_save_to_favorites)}\n${it.message}")
                    Log.d("R", "Не удалось сохранить в избранное\n${it.message}")
                }
        }
    }

    fun deleteTranslationFromFavorites(id: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            kotlin.runCatching { deleteTranslationFromFavoritesByIdUseCase(id) }
                .onSuccess {
                    uiState = uiState.copy(isLoading = false)
                    Log.d("R", "Запись с id=$id удалена из избранного")
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = "${resourceProvider.getString
                            (R.string.not_delete_from_favorites)}\n${it.message}")
                    Log.d("R", "Не удалось удалить из избранного\n${it.message}")
                }
        }
    }

}