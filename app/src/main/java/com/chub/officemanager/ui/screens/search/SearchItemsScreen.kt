package com.chub.officemanager.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chub.officemanager.R
import com.chub.officemanager.ui.ItemOperation
import com.chub.officemanager.ui.OperationType
import com.chub.officemanager.ui.view.ErrorLayout
import com.chub.officemanager.ui.view.Loading
import com.chub.officemanager.ui.view.OfficeItemsList
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.Result

@Composable
fun SearchItemsScreen(
    isSelectionScreen: Boolean = false,
    onListItemClick: (OfficeItem) -> Unit,
    viewModel: SearchItemsViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    Box {
        when (val content = state.value) {
            Result.Loading -> Loading()
            is Result.Error -> ErrorLayout(stringResource(id = R.string.error))
            is Result.Success<SearchItemsUiState> -> Content(
                isOperable = !isSelectionScreen,
                onAction = {
                    when (it) {
                        is SearchItemsScreenAction.Navigation.ItemClicked -> onListItemClick(it.item)
                        is SearchItemsScreenAction.StateChange -> viewModel.onAction(it)
                    }
                }, content
            )
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
            onValueChange = { onAction(SearchItemsScreenAction.StateChange.FilterChanged(it)) },
            placeholder = { Text(stringResource(id = R.string.search)) },
            trailingIcon = { Icon(Icons.Filled.Search, stringResource(id = R.string.search)) })
        OfficeItemsList(content.data.items, operations) { onAction(SearchItemsScreenAction.Navigation.ItemClicked(it)) }
    }
}

@Composable
private fun provideOperations(
    isOperable: Boolean,
    onAction: (SearchItemsScreenAction) -> Unit
) = if (isOperable) listOf(ItemOperation(OperationType.Delete) {
    onAction(SearchItemsScreenAction.StateChange.ItemRemove(it))
}, ItemOperation(OperationType.Edit) {
    onAction(SearchItemsScreenAction.Navigation.ItemClicked(it))
}) else emptyList()

