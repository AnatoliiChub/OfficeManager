package com.chub.officemanager

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chub.officemanager.OfficeDestinations.SEARCH_OFFICE_ITEMS_ROUTE

@Composable
fun OfficeNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SEARCH_OFFICE_ITEMS_ROUTE,
    ) {
        searchOfficeItemsRoute(
            onItemClick = {
                navController.navigateToEditItem(it.id)
            }, onFabClick = {
                navController.navigateToAddItem()
            }
        )

        searchToAddRoute(
            onItemClick = {
                navController.popBackStack()
                navController.currentBackStackEntry!!.savedStateHandle[NavArgs.RELATION] = it
            },
            onBack = {
                navController.popBackStack()
            }
        )

        addEditOfficeItemRoute(
            onAddButtonClick = {
                navController.navigateToSearchToAdd()
            }, onBack = {
                navController.popBackStack()
            }
        )
    }
}