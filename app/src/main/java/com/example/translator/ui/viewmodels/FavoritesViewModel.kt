package com.example.translator.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator.R
import com.example.translator.domain.models.WordItem
import com.example.translator.domain.useCases.DeleteTranslationFromFavoritesByIdUseCase
import com.example.translator.domain.useCases.GetAllFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val favorites: List<WordItem> = emptyList()
)

@HiltViewModel
class FavoritesViewModel @Inject constructor (
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase,
    private val deleteTranslationFromFavoritesByIdUseCase: DeleteTranslationFromFavoritesByIdUseCase,
) : ViewModel(){


    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }


    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }


    private fun observeFavorites() {
        viewModelScope.launch {
            getAllFavoritesUseCase()
                .catch { e ->
                    _uiState.update { it.copy(error = UiText.DynamicString(e.message ?: "")) }
                    Log.d("R", "Избранное не загрузилось ${e.message}")
                }
                .collect { favorites ->
                    _uiState.update { it.copy(favorites = favorites) }
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
                    _uiState.update { it.copy(isLoading = false, error = UiText.StringResource(
                        R.string.not_delete_from_favorites,
                        e.message ?: ""
                    )) }

                    Log.d("R", "Не удалось удалить из избранного\n${e.message}")
                }
        }
    }

}