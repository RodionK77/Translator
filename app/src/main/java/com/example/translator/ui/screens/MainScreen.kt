package com.example.translator.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.translator.R
import com.example.translator.ui.viewmodels.FavoritesViewModel
import com.example.translator.ui.viewmodels.TranslateViewModel

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun MainScreen(
    startDestination: String = ScreenConstants.TRANSLATE_SCREEN,
    translateViewModel: TranslateViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
               ) {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
                BottomBar(
                    navController = navController,
                    currentDestination = currentDestination,
                )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column {
                NavHost(navController = navController, startDestination = startDestination) {
                    composable(ScreenConstants.TRANSLATE_SCREEN) {
                        TranslateScreen(translateViewModel)
                    }
                    composable(ScreenConstants.FAVORITES_SCREEN) {
                        FavoritesScreen(favoritesViewModel)
                    }
                }
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val route: List<String>,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
private fun BottomBar(navController: NavController, currentDestination: String?){

    val navItems = listOf(
        BottomNavigationItem(
            title = stringResource(R.string.translate_screen),
            route = listOf(ScreenConstants.TRANSLATE_SCREEN),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavigationItem(
            title = stringResource(R.string.favorites),
            route = listOf(ScreenConstants.FAVORITES_SCREEN),
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder
        )
    )

    val selectedItemIndex = navItems.indexOfFirst { it.route.contains(currentDestination)}

    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    if (selectedItemIndex != index) {
                        navController.navigate(item.route.first()) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if(index==selectedItemIndex){
                            item.selectedIcon
                        }else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text=item.title) }
            )
        }
    }

}