package com.chub.officemanager.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chub.officemanager.R
import com.chub.officemanager.ui.OperationType
import com.chub.officemanager.ui.theme.OfficeManagerTheme
import com.chub.officemanager.ui.view.ErrorLayout
import com.chub.officemanager.ui.ItemOperation
import com.chub.officemanager.ui.view.Loading
import com.chub.officemanager.ui.view.OfficeItemsList
import com.chub.officemanager.ui.view.OfficeTopBar
import com.chub.officemanager.util.Result

@Composable
fun SearchItemsScreen(
    isSelectionScreen: Boolean = false,
    onNavigation: (SearchItemsScreenAction.Navigation) -> Unit,
    viewModel: SearchItemsViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    OfficeManagerTheme {
        Scaffold(topBar = {
            OfficeTopBar(stringResource(
                id = if (isSelectionScreen) R.string.title_search_items_to_add
                else R.string.title_search_objects
            ), onNavigationClick = if (isSelectionScreen) {
                { onNavigation(SearchItemsScreenAction.Navigation.GoBack) }
            } else null)
        }, floatingActionButton = {
            if (!isSelectionScreen) {
                FloatingActionButton(
                    onClick = { onNavigation(SearchItemsScreenAction.Navigation.AddItem) },
                ) {
                    Icon(Icons.Filled.Add, stringResource(id = R.string.done_action))
                }
            }
        }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (val content = state.value) {
                    Result.Loading -> Loading()
                    is Result.Error -> ErrorLayout(stringResource(id = R.string.error))
                    is Result.Success<SearchItemsUiState> -> Content(
                        !isSelectionScreen, {
                            when (it) {
                                is SearchItemsScreenAction.Navigation -> onNavigation(it)
                                is SearchItemsScreenAction.StateAction -> viewModel.onAction(it)
                            }
                        }, content
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    isOperable: Boolean = false,
    onAction: (SearchItemsScreenAction) -> Unit,
    content: Result.Success<SearchItemsUiState>
) {
    val operations = provideOperations(isOperable, onAction)
    Column {
        TextField(shape = RectangleShape,
            modifier = Modifier.fillMaxWidth(),
            value = content.data.filter,
            onValueChange = { onAction(SearchItemsScreenAction.StateAction.FilterChanged(it)) },
            placeholder = { Text(stringResource(id = R.string.search)) },
            trailingIcon = { Icon(Icons.Filled.Search, stringResource(id = R.string.search)) })
        OfficeItemsList(content.data.items, operations, onAction)
    }
}

@Composable
private fun provideOperations(
    isOperable: Boolean,
    onAction: (SearchItemsScreenAction) -> Unit
) = if (isOperable) listOf(ItemOperation(OperationType.Delete) {
    onAction(SearchItemsScreenAction.StateAction.ItemRemove(it))
}, ItemOperation(OperationType.Edit) {
    onAction(SearchItemsScreenAction.Navigation.ItemClicked(it))
}) else emptyList()

