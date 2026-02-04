package com.example.translator.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.translator.R
import com.example.translator.ui.viewmodels.FavoritesViewModel

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel,
) {

    val uiState by favoritesViewModel.uiState.collectAsStateWithLifecycle()

    if(uiState.error != null){
        Toast.makeText(LocalContext.current,
            "${stringResource(R.string.error)}: " +
                    uiState.error!!.asString(), Toast.LENGTH_SHORT ).show()
        favoritesViewModel.clearError()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(uiState.favorites){ index, item ->
                ListItem(
                    item = item,
                    index = index,
                    fromHistory = false,
                    isFavorite = uiState.favorites.any { it.id == item.id },
                    onDeleteFromFavorites = {
                        favoritesViewModel.deleteTranslationFromFavorites(item.id)
                    }
                )
            }
        }
    }
}