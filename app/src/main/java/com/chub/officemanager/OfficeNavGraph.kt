package com.chub.officemanager

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chub.officemanager.NavArgs.ITEM_ID
import com.chub.officemanager.OfficeDestinations.SEARCH_OFFICE_ITEMS_ROUTE
import com.chub.officemanager.OfficeDestinations.SEARCH_TO_ADD_ROUTE
import com.chub.officemanager.OfficeScreens.ADD_EDIT_OFFICE_ITEM
import com.chub.officemanager.domain.OfficeItem.Companion.NONE
import com.chub.officemanager.ui.screens.AddEditScreen
import com.chub.officemanager.ui.screens.SearchItemsScreen
import com.chub.officemanager.ui.screens.SearchToAddScreen

@Composable
fun OfficeNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SEARCH_OFFICE_ITEMS_ROUTE,
    ) {
        composable(
            SEARCH_OFFICE_ITEMS_ROUTE
        ) { entry ->
            SearchItemsScreen(onItemClicked = {
                navController.navigate("$ADD_EDIT_OFFICE_ITEM/${it.id}")
            }, onFabClick = {
                navController.navigate("$ADD_EDIT_OFFICE_ITEM/$NONE")
            })
        }
        composable(
            SEARCH_TO_ADD_ROUTE
        ) { entry ->
            SearchToAddScreen(onItemClicked = {
                //TODO add item as result
                navController.popBackStack()
            })
        }
        composable(
            OfficeDestinations.ADD_EDIT_OFFICE_ITEM_ROUTE,
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString(ITEM_ID)?.toInt() ?: NONE
            AddEditScreen(id, onItemClick = {
                navController.navigate("$ADD_EDIT_OFFICE_ITEM/${it.id}")
            }, onItemsSaved = {
                navController.popBackStack()
            }, onAddButtonClick = {
                navController.navigate(SEARCH_TO_ADD_ROUTE)
            })
        }
    }
}