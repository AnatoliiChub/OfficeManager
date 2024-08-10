package com.chub.officemanager

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chub.officemanager.NavArgs.ITEM_ID
import com.chub.officemanager.OfficeDestinations.ADD_EDIT_OFFICE_ITEM_ROUTE
import com.chub.officemanager.OfficeDestinations.SEARCH_OFFICE_ITEMS_ROUTE
import com.chub.officemanager.OfficeDestinations.SEARCH_TO_ADD_ROUTE
import com.chub.officemanager.OfficeScreens.ADD_EDIT_OFFICE_ITEM
import com.chub.officemanager.ui.screens.addedit.AddEditScreen
import com.chub.officemanager.ui.screens.search.SearchItemsScreen
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.OfficeItem.Companion.NONE

@Composable
fun OfficeNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SEARCH_OFFICE_ITEMS_ROUTE,
    ) {
        composable(SEARCH_OFFICE_ITEMS_ROUTE) {
            SearchItemsScreen(onItemClicked = {
                navController.navigate("$ADD_EDIT_OFFICE_ITEM/${it.id}")
            }, onFabClick = {
                navController.navigate("$ADD_EDIT_OFFICE_ITEM/$NONE")
            })
        }

        composable(SEARCH_TO_ADD_ROUTE) {
            SearchItemsScreen(true, onItemClicked = {
                navController.popBackStack()
                navController.currentBackStackEntry!!.savedStateHandle[NavArgs.RELATION] = it
            })
        }

        composable(
            ADD_EDIT_OFFICE_ITEM_ROUTE,
            arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
        ) { entry ->
            val relation = entry.savedStateHandle.get<OfficeItem>(NavArgs.RELATION)
            val id = entry.arguments?.getLong(ITEM_ID) ?: NONE
            AddEditScreen(id, relation, onItemClick = {
                navController.navigate("$ADD_EDIT_OFFICE_ITEM/${it.id}")
            }, onItemsSaved = {
                navController.popBackStack()
            }, onAddButtonClick = {
                navController.navigate(SEARCH_TO_ADD_ROUTE)
            })
        }
    }
}