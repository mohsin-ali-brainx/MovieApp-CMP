package com.brainx.utils_extensions.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun NavHostController.safeNavToNextScreen(route: Any, shouldNavigateUp: Boolean = false, shouldClearTop: Boolean = false, shouldClearBackStack: Boolean = false) {

    if (shouldNavigateUp)
    {
        navigateUp()
        navigate(route)
        return
    }
    if (shouldClearBackStack) {
        navigate(route) {
            // Clear the entire back stack
            popUpTo(0) { inclusive = true }
        }
        return
    }
    if (shouldClearTop){
        navigate(route) {
            popUpTo(graph.findStartDestination().id) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }
        return
    }
    navigate(route)
}