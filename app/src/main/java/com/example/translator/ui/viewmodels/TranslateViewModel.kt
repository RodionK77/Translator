package com.example.translator.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator.R
import com.example.translator.domain.models.WordItem
import com.example.translator.domain.useCases.DeleteTranslationFromFavoritesByIdUseCase
import com.example.translator.domain.useCases.DeleteTranslationFromHistoryByIdUseCase
import com.example.translator.domain.useCases.GetAllFavoritesUseCase
import com.example.translator.domain.useCases.GetAllHistoryUseCase
import com.example.translator.domain.useCases.GetMeaningsUseCase
import com.example.translator.domain.useCases.SaveTranslationToFavoritesUseCase
import com.example.translator.domain.useCases.SaveTranslationToHistoryUseCase
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
    private val saveTranslationToHistoryUseCase: SaveTranslationToHistoryUseCase,
    private val saveTranslationToFavoritesUseCase: SaveTranslationToFavoritesUseCase,
    private val getAllHistoryUseCase: GetAllHistoryUseCase,
    private val getMeaningsUseCase: GetMeaningsUseCase,
    private val deleteTranslationFromHistoryByIdUseCase: DeleteTranslationFromHistoryByIdUseCase,
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase,
    private val deleteTranslationFromFavoritesByIdUseCase: DeleteTranslationFromFavoritesByIdUseCase,
) : ViewModel(){


    private val _uiState = MutableStateFlow(TranslateUiState())
    val uiState: StateFlow<TranslateUiState> = _uiState.asStateFlow()

    init {
        observeHistory()
        observeFavorites()
    }


    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    fun clearTranslate() {
        _uiState.update { it.copy(translate = null) }
    }

    private fun observeHistory() {
        viewModelScope.launch {
            getAllHistoryUseCase()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            error = UiText.DynamicString(e.message ?: "")
                        )
                    }
                    Log.d("R", "История не загрузилась ${e.message}")
                }
                .collect { history ->
                    _uiState.update { it.copy(history = history) }
                }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getAllFavoritesUseCase()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            error = UiText.DynamicString(e.message ?: "")
                        )
                    }
                    Log.d("R", "Избранное не загрузилось ${e.message}")
                }
                .collect { favorites ->
                    _uiState.update { it.copy(favorites = favorites) }
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
        kotlin.runCatching { saveTranslationToHistoryUseCase(item) }
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
            kotlin.runCatching { deleteTranslationFromHistoryByIdUseCase(id) }
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
            kotlin.runCatching { saveTranslationToFavoritesUseCase(item) }
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
            kotlin.runCatching { deleteTranslationFromFavoritesByIdUseCase(id) }
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