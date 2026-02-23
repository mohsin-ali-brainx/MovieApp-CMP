package com.brainx.ticket_tribe.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.brainx.domain.network.dto_mappers.movie.MediaDTO
import com.brainx.ticket_tribe.presentation.screens.detail.DetailScreen
import com.brainx.ticket_tribe.presentation.screens.main_home.ui.MainHomeScreen
import com.brainx.ticket_tribe.presentation.screens.main_home.viewmodel.MainHomeScreenViewModel
import com.brainx.utils_extensions.navigation.horizontallyAnimatedComposable
import com.brainx.utils_extensions.navigation.safeNavToNextScreen
import com.brainx.utils_extensions.navigation.toModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AppNavHostGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.MainHome
    ) {

        mainHomeScreenRoute(navController = navController)
        detailScreen(navController=navController)
    }
}

private fun NavGraphBuilder.mainHomeScreenRoute(
    navController: NavHostController
){
    composable<AppRoutes.MainHome> { backStackEntry ->
        val viewModel = koinViewModel<MainHomeScreenViewModel>()
        MainHomeScreen(
            dataState = viewModel.state,
            uiEvents = viewModel.eventFlow,
            onIntent = { viewModel.onIntent(it) },
            onNavigate = {
                when(it){
                    is AppRoutes.Detail->{
                        navController.safeNavToNextScreen(it, shouldClearBackStack = false)
                    }
                    else -> Unit

                }
            }
        )
    }
}


private fun NavGraphBuilder.detailScreen(
    navController: NavHostController
){
    composable<AppRoutes.Detail> { backStackEntry ->
        val args = backStackEntry.toRoute<AppRoutes.Detail>()

        DetailScreen(
            mediaDataModel = args.mediaDataModelJson.toModel<MediaDTO>(),
            onNavigate = {

            },
            onBack = {
                navController.popBackStack()
            }
        )
    }
}