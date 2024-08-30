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
import com.chub.officemanager.ui.theme.OfficeManagerTheme
import com.chub.officemanager.ui.view.ErrorLayout
import com.chub.officemanager.ui.view.ItemOperation
import com.chub.officemanager.ui.view.Loading
import com.chub.officemanager.ui.view.OfficeItemsList
import com.chub.officemanager.ui.view.OfficeTopBar
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.Result

@Composable
fun SearchItemsScreen(
    isSelectionScreen: Boolean = false,
    onItemClick: (OfficeItem) -> Unit,
    onFabClick: () -> Unit = {},
    viewModel: SearchItemsViewModel = hiltViewModel(),
    onBack: (() -> Unit)? = null
) {
    val filter = viewModel.filter.collectAsStateWithLifecycle()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    OfficeManagerTheme {
        Scaffold(topBar = {
            OfficeTopBar(
                stringResource(
                    id = if (isSelectionScreen) R.string.title_search_items_to_add
                    else R.string.title_search_objects
                ),
                onNavigationClick = onBack
            )
        }, floatingActionButton = {
            if (!isSelectionScreen) {
                FloatingActionButton(
                    onClick = onFabClick,
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
                        filter.value,
                        content,
                        viewModel::onItemRemove,
                        viewModel::onFilterChanged,
                        !isSelectionScreen,
                        onItemClick
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    filter: String,
    content: Result.Success<SearchItemsUiState>,
    onItemRemove: (OfficeItem) -> Unit,
    onFilterChanged: (String) -> Unit,
    isOperable: Boolean,
    onItemClicked: (OfficeItem) -> Unit
) {
    Column {
        TextField(
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth(),
            value = filter,
            onValueChange = { onFilterChanged(it) },
            placeholder = { Text(stringResource(id = R.string.search)) },
            trailingIcon = { Icon(Icons.Filled.Search, stringResource(id = R.string.search)) }
        )
        val operations = if (isOperable) listOf(ItemOperation.Edit, ItemOperation.Delete) else emptyList()
        OfficeItemsList(content.data.items, operations, onItemClicked, onItemRemove)
    }
}

