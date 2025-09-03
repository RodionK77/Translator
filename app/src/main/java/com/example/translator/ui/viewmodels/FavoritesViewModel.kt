package com.example.translator.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translator.R
import com.example.translator.domain.models.TranslationFavoritesEntity
import com.example.translator.domain.useCases.DeleteTranslationFromFavoritesByIdUseCase
import com.example.translator.domain.useCases.GetAllFavoritesUseCase
import com.example.translator.ui.resources.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val favorites: List<TranslationFavoritesEntity> = emptyList()
)

@HiltViewModel
class FavoritesViewModel @Inject constructor (
    private val getAllFavoritesUseCase: GetAllFavoritesUseCase,
    private val deleteTranslationFromFavoritesByIdUseCase: DeleteTranslationFromFavoritesByIdUseCase,
    private val resourceProvider: ResourceProvider
) : ViewModel(){

    var uiState by mutableStateOf(FavoritesUiState())
        private set

    init {
        observeFavorites()
    }


    fun clearError() {
        uiState = uiState.copy(error = null)
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