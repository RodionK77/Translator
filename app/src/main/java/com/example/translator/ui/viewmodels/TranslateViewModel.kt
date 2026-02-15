package com.example.translator.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator.R
import com.example.translator.domain.models.WordItem
import com.example.translator.domain.useCases.GetMeaningsUseCase
import com.example.translator.domain.useCases.GetAllEntitiesUseCase
import com.example.translator.domain.useCases.RemoveEntityFromFavoritesByIdUseCase
import com.example.translator.domain.useCases.RemoveEntityFromHistoryByIdUseCase
import com.example.translator.domain.useCases.SaveEntityToFavoritesByIdUseCase
import com.example.translator.domain.useCases.SaveEntityToHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TranslateUiState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val translate: WordItem? = null,
    val history: List<WordItem> = emptyList(),
    val favorites: List<WordItem> = emptyList()
)

@HiltViewModel
class TranslateViewModel @Inject constructor (
    private val getMeaningsUseCase: GetMeaningsUseCase,
    private val saveEntityToHistoryUseCase: SaveEntityToHistoryUseCase,
    private val saveEntityToFavoritesByIdUseCase: SaveEntityToFavoritesByIdUseCase,
    private val getAllEntitiesUseCase: GetAllEntitiesUseCase,
    private val removeEntityFromHistoryByIdUseCase: RemoveEntityFromHistoryByIdUseCase,
    private val removeEntityFromFavoritesByIdUseCase: RemoveEntityFromFavoritesByIdUseCase
) : ViewModel(){


    private val _uiState = MutableStateFlow(TranslateUiState())
    val uiState: StateFlow<TranslateUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }


    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    fun clearTranslate() {
        _uiState.update { it.copy(translate = null) }
    }

    private fun observeData() {
        /*viewModelScope.launch {
            val historyFlow = getAllHistoryUseCase()
            val favoritesFlow = getAllFavoritesUseCase()

            combine(historyFlow, favoritesFlow) { history, favorites ->

                val favoriteWordsSet = favorites.map { it.id }.toSet()

                val historyWithFavorites = history.map { historyItem ->
                    historyItem.copy(
                        isFavorite = favoriteWordsSet.contains(historyItem.id)
                    )
                }

                Pair(historyWithFavorites, favorites)
            }
                .catch { e ->
                    _uiState.update { it.copy(error = UiText.DynamicString(e.message ?: "")) }
                    Log.d("R", "История и избранное не загрузилась ${e.message}")
                }
                .collect { (updatedHistory, favorites) ->
                    _uiState.update {
                        it.copy(
                            history = updatedHistory,
                            favorites = favorites
                        )
                    }
                }
        }*/

        viewModelScope.launch {
            getAllEntitiesUseCase()
                .catch { e ->
                    _uiState.update {
                        it.copy(error = UiText.DynamicString(e.message ?: ""))
                    }
                    Log.d("R", "История не загрузилась: ${e.message}")
                }
                .collect { history ->
                    _uiState.update {
                        it.copy(
                            history = history,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun getMeanings(search: String){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            kotlin.runCatching { getMeaningsUseCase(search) }
                .onSuccess { meanings ->
                    if(meanings.isNotEmpty()){
                        val meaning = meanings.first()

                        _uiState.update { it.copy(isLoading = false, translate = meaning) }

                        Log.d("R", "Перевод: ${meaning.translation}")

                        saveTranslationToHistory(meaning)
                    } else {
                        _uiState.update { it.copy(isLoading = false, error = UiText.StringResource(R.string.translate_not_found)) }
                        Log.d("R", "Перевод найти не удалось")
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = UiText.StringResource(
                            R.string.translate_not_received,
                            e.message ?: "Unknown error"
                        ))
                    }

                    Log.d("R", "Перевод получить не удалось: ${e.message}")
                }
        }
    }

    private suspend fun saveTranslationToHistory(item: WordItem){
        kotlin.runCatching { saveEntityToHistoryUseCase(item) }
            .onSuccess {
                Log.d("R", "Перевод сохранен в историю")
            }
            .onFailure { e ->
                _uiState.update {
                    it.copy(error = UiText.StringResource(
                        R.string.translate_not_received,
                        e.message ?: "Unknown error"
                    ))
                }
                Log.d("R", "Не удалось сохранить в историю\n${e.message}")
            }
    }

    fun deleteTranslationFromHistory(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            kotlin.runCatching { removeEntityFromHistoryByIdUseCase(id) }
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    Log.d("R", "Запись с id=$id удалена из истории")
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = UiText.StringResource(
                                R.string.not_delete_from_history,
                                e.message ?: ""
                            )
                        )
                    }
                    Log.d("R", "Не удалось удалить из истории: ${e.message}")
                }
        }
    }

    fun saveTranslationToFavorites(item: WordItem){
        viewModelScope.launch {
            kotlin.runCatching { saveEntityToFavoritesByIdUseCase(item.id) }
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    Log.d("R", "Перевод сохранен в избранное")
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, error = UiText.StringResource(
                                R.string.not_save_to_favorites,
                                e.message ?: ""
                            )
                        )
                    }
                    Log.d("R", "Не удалось сохранить в избранное\n${e.message}")
                }
        }
    }

    fun deleteTranslationFromFavorites(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            kotlin.runCatching { removeEntityFromFavoritesByIdUseCase(id) }
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    Log.d("R", "Запись с id=$id удалена из избранного")
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false, error = UiText.StringResource(
                                R.string.not_delete_from_favorites,
                                e.message ?: ""
                            )
                        )
                    }
                    Log.d("R", "Не удалось удалить из избранного\n${e.message}")
                }
        }
    }

}