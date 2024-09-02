package com.chub.officemanager

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chub.officemanager.OfficeDestinations.SEARCH_OFFICE_ITEMS_ROUTE
import com.chub.officemanager.ui.theme.OfficeManagerTheme
import com.chub.officemanager.ui.view.OfficeTopBar
import com.chub.officemanager.util.OfficeItem.Companion.NONE

enum class FabType {
    ADD, SAVE
}

data class FabState(
    val isVisible: Boolean = false,
    val type: FabType = FabType.ADD,
    val onClick: () -> Unit = {},
//    val onOperationDone: (() -> Unit) -> Unit = {}
)

@Composable
fun OfficeNavGraph() {
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val title = rememberSaveable { mutableStateOf("") }
    val onNavigationClick: MutableState<(() -> Unit)?> = remember { mutableStateOf({}) }
    var fabState by remember { mutableStateOf(FabState(false, FabType.ADD) {}) }

    OfficeManagerTheme {
        Scaffold(topBar = {
            OfficeTopBar(
                title.value,
                onNavigationClick = if (onNavigationClick.value != null) {
                    onNavigationClick.value!!
                } else null
            )
        }, floatingActionButton = {
            if (fabState.isVisible) {
                Fab(fabState)
            }
        }, snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = SEARCH_OFFICE_ITEMS_ROUTE,
            ) {
                searchOfficeItemsRoute(onItemClick = { navController.navigateToEditItem(it.id) }) {
                    title.value = stringResource(id = R.string.title_search_objects)
                    fabState = FabState(true, FabType.ADD, onClick = { navController.navigateToAddItem() })
                    onNavigationClick.value = null
                }

                searchToAddRoute(onItemClick = {
                    navController.popBackStack()
                    navController.currentBackStackEntry!!.savedStateHandle[NavArgs.RELATION] = it
                }
                ) {
                    title.value = stringResource(id = R.string.title_search_items_to_add)
                    fabState = FabState(false)
                    onNavigationClick.value = { navController.popBackStack() }
                }

                addEditOfficeItemRoute(
                    onAddNewItem = { navController.navigateToSearchToAdd() },
                    onFabStateUpdate = { fabState = it },
                    snackBarHostState = snackBarHostState
                ) {
                    title.value = if (it == NONE) stringResource(id = R.string.title_add_items)
                    else stringResource(id = R.string.title_edit_items)
                    onNavigationClick.value = { navController.popBackStack() }
                }
            }
        }
    }
}

@Composable
private fun Fab(state: FabState) {
    FloatingActionButton(onClick = { state.onClick() }) {
        when (state.type) {
            FabType.ADD -> Icon(Icons.Filled.Add, stringResource(id = R.string.add_action))
            FabType.SAVE -> Icon(Icons.Filled.Done, stringResource(id = R.string.done_action))
        }
    }
}