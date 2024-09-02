package com.chub.officemanager

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chub.officemanager.NavArgs.ITEM_ID
import com.chub.officemanager.OfficeDestinations.ADD_EDIT_OFFICE_ITEM_ROUTE
import com.chub.officemanager.OfficeDestinations.SEARCH_OFFICE_ITEMS_ROUTE
import com.chub.officemanager.OfficeDestinations.SEARCH_TO_ADD_ROUTE
import com.chub.officemanager.OfficeScreens.ADD_EDIT_OFFICE_ITEM
import com.chub.officemanager.OfficeScreens.OFFICE_ITEMS_LIST
import com.chub.officemanager.OfficeScreens.SEARCH_OFFICE_ITEMS_TO_ADD
import com.chub.officemanager.ui.screens.addedit.AddEditScreen
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction
import com.chub.officemanager.ui.screens.search.SearchItemsScreen
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.OfficeItem.Companion.NONE

object OfficeScreens {
    const val OFFICE_ITEMS_LIST = "office_items_list"
    const val ADD_EDIT_OFFICE_ITEM = "add_edit_office_item"
    const val SEARCH_OFFICE_ITEMS_TO_ADD = "search_to_add"
}

object NavArgs {
    const val ITEM_ID = "item_id"
    const val RELATION = "relation"
}

object OfficeDestinations {
    const val SEARCH_OFFICE_ITEMS_ROUTE = OFFICE_ITEMS_LIST
    const val SEARCH_TO_ADD_ROUTE = SEARCH_OFFICE_ITEMS_TO_ADD
    const val ADD_EDIT_OFFICE_ITEM_ROUTE = "{$ADD_EDIT_OFFICE_ITEM}/{$ITEM_ID}"
}

fun NavGraphBuilder.searchOfficeItemsRoute(
    onItemClick: (OfficeItem) -> Unit,
    onOpen: @Composable () -> Unit
) {
    composable(SEARCH_OFFICE_ITEMS_ROUTE) {
        onOpen()
        SearchItemsScreen(onListItemClick = { onItemClick(it) })
    }
}


fun NavGraphBuilder.searchToAddRoute(
    onItemClick: (OfficeItem) -> Unit,
    onOpen: @Composable () -> Unit
) {
    composable(SEARCH_TO_ADD_ROUTE) {
        onOpen()
        SearchItemsScreen(true, onListItemClick = { onItemClick(it) })
    }
}

fun NavGraphBuilder.addEditOfficeItemRoute(
    onAddNewItem: () -> Unit,
    onFabStateUpdate: (FabState) -> Unit,
    snackBarHostState: SnackbarHostState,
    onOpen: @Composable (Long) -> Unit
) {
    composable(
        ADD_EDIT_OFFICE_ITEM_ROUTE,
        arguments = listOf(navArgument(ITEM_ID) { type = NavType.LongType })
    ) { entry ->
        val relation = entry.savedStateHandle.get<OfficeItem>(NavArgs.RELATION)
        val id = entry.arguments?.getLong(ITEM_ID) ?: NONE
        onOpen(id)
        AddEditScreen(
            relation, onNavigation = {
                when (it) {
                    is AddEditScreenAction.NavigationAction -> onAddNewItem()
                    else -> {}
                }
            }, onFabStateUpdate = { onFabStateUpdate(it) },
            snackBarHostState = snackBarHostState
        )
    }
}

fun NavController.navigateToEditItem(id: Long) {
    navigate("$ADD_EDIT_OFFICE_ITEM/$id")
}

fun NavController.navigateToAddItem() {
    navigate("$ADD_EDIT_OFFICE_ITEM/$NONE")
}

fun NavController.navigateToSearchToAdd() {
    navigate(SEARCH_TO_ADD_ROUTE)
}