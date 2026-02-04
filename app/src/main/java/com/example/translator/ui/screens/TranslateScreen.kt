package com.example.translator.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.translator.R
import com.example.translator.ui.viewmodels.TranslateViewModel

@Composable
fun TranslateScreen(
    translateViewModel: TranslateViewModel,
) {

    val uiState by translateViewModel.uiState.collectAsStateWithLifecycle()

    if(uiState.error != null){
        Toast.makeText(LocalContext.current,
            "${stringResource(R.string.error)}: " +
                    uiState.error!!.asString(), Toast.LENGTH_SHORT ).show()
        translateViewModel.clearError()
    }

    var word by rememberSaveable { mutableStateOf("") }

    if(word.isEmpty()){
        translateViewModel.clearTranslate()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                value = word,
                onValueChange = {word = it.trim()},
                textStyle = TextStyle(fontSize = 16.sp),
                placeholder = {Text(stringResource(R.string.word_for_translate))},
                maxLines = 1
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,

                text = uiState.translate?.translation
                    ?: stringResource(R.string.translation)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { if(word.isNotEmpty()) translateViewModel.getMeanings(word) },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.translate),
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.history),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(uiState.history){ index, item ->
                ListItem(
                    item = item,
                    index = index,
                    fromHistory = true,
                    isFavorite = uiState.favorites.any { it.id == item.id },
                    onDeleteFromHistory = {
                        translateViewModel.deleteTranslationFromHistory(item.id)
                    },
                    onAddToFavorites = { translateViewModel.saveTranslationToFavorites(
                        item
                    ) },
                    onDeleteFromFavorites = {
                        translateViewModel.deleteTranslationFromFavorites(item.id)
                    }
                )
            }
        }
    }
}