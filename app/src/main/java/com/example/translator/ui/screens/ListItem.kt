package com.example.translator.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.translator.R
import com.example.translator.domain.models.WordItem

@Composable
fun ListItem(
    item: WordItem,
    index: Int,
    fromHistory: Boolean,
    onDeleteFromHistory: (() -> Unit)? = null,
    onAddToFavorites: (() -> Unit)? = null,
    onDeleteFromFavorites: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (showMenu) 1.06f else 1f,
        label = "scaleAnim"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { showMenu = true }
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (showMenu) 8.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if(!fromHistory){
                MaterialTheme.colorScheme.primary
            } else {
                if (index == 0) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${item.text} ➞ ${item.translation}",
                modifier = Modifier.weight(1f),
                fontWeight =
                if(item.isFavorite && !fromHistory) FontWeight.Bold else FontWeight.Normal
            )

            if (item.isFavorite && fromHistory) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.favorites),
                )
            }
        }


        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            if (fromHistory) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.delete_from_history)) },
                    onClick = {
                        if (onDeleteFromHistory != null) {
                            onDeleteFromHistory()
                        }
                        showMenu = false
                    }
                )
            }
            DropdownMenuItem(
                text = {
                    if(item.isFavorite) Text(stringResource(R.string.delete_from_favorites))
                    else Text(stringResource(R.string.add_to_favorites))
                },
                onClick = {
                    if(item.isFavorite) onDeleteFromFavorites() else if (onAddToFavorites != null) {
                        onAddToFavorites()
                    }
                    showMenu = false
                }
            )
        }
    }
}