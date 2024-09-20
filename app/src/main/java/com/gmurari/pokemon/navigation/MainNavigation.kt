package com.gmurari.pokemon.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.gmurari.pokemon.presentation.home.HomeScreen
import com.gmurari.pokemon.presentation.home.HomeViewModel
import com.gmurari.pokemon.presentation.home_with_pager.HomeWIthPagerViewModel
import com.gmurari.pokemon.presentation.home_with_pager.HomeWithPagerScreen
import com.gmurari.pokemon.presentation.welcome.WelcomeScreen
import com.gmurari.pokemon.presentation.welcome.WelcomeUiEvent
import com.gmurari.pokemon.presentation.welcome.WelcomeViewModel
import com.gmurari.pokemon.presentation.welcome.event.WelcomeVmEvent
import com.gmurari.pokemon.ui.theme.dimens
import com.gmurari.pokemon.util.ObserveAsEvents

@Composable
fun MainNavigation(
    paddingValues: PaddingValues
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destination.Welcome) {

        composable<Destination.Welcome> {
            val viewModel = hiltViewModel<WelcomeViewModel>()
            val state = viewModel.state.collectAsStateWithLifecycle().value

            ObserveAsEvents(flow = viewModel.event) {
                when (it) {
                    WelcomeVmEvent.DownloadCompleted -> navController.navigate(Destination.HomeWithPager) {
                        popUpTo(0)
                    }
                }
            }

            WelcomeScreen(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.large),
                state = state) {
                viewModel.onEvent(WelcomeUiEvent.OnRetryDownload)
            }
        }
        composable<Destination.Home> {
            val viewModel = hiltViewModel<HomeViewModel>()
            val state = viewModel.state.collectAsStateWithLifecycle().value

            HomeScreen(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.large),
                state = state,
                onEvent = viewModel::onEvent
            )
        }
        composable<Destination.HomeWithPager> {
            val viewModel = hiltViewModel<HomeWIthPagerViewModel>()
            val pokemonList = viewModel.pagingDataFlow.collectAsLazyPagingItems()
            val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle().value

            HomeWithPagerScreen(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.large),
                pokemonList = pokemonList,
                searchString = searchQuery,
                onEvent = viewModel::onEvent
            )
        }
    }
}
