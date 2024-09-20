package com.gmurari.pokemon.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gmurari.pokemon.presentation.home.HomeScreen
import com.gmurari.pokemon.presentation.home.HomeViewModel
import com.gmurari.pokemon.presentation.welcome.WelcomeScreen
import com.gmurari.pokemon.presentation.welcome.WelcomeUiEvent
import com.gmurari.pokemon.presentation.welcome.WelcomeViewModel
import com.gmurari.pokemon.presentation.welcome.event.WelcomeVmEvent
import com.gmurari.pokemon.util.ObserveAsEvents

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destination.Welcome) {

        composable<Destination.Welcome> {
            val viewModel = hiltViewModel<WelcomeViewModel>()
            val state = viewModel.state.collectAsStateWithLifecycle().value

            ObserveAsEvents(flow = viewModel.event) {
                when (it) {
                    WelcomeVmEvent.DownloadCompleted -> navController.navigate(Destination.Home) {
                        popUpTo(0)
                    }
                }
            }

            WelcomeScreen(state = state) {
                viewModel.onEvent(WelcomeUiEvent.OnRetryDownload)
            }
        }
        composable<Destination.Home> {
            val viewModel = hiltViewModel<HomeViewModel>()
            val state = viewModel.state.collectAsStateWithLifecycle().value

            HomeScreen(
                state = state,
                onEvent = viewModel::onEvent
            )
        }
    }
}
