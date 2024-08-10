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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chub.officemanager.R
import com.chub.officemanager.domain.OfficeItem
import com.chub.officemanager.domain.Result
import com.chub.officemanager.ui.theme.OfficeManagerTheme
import com.chub.officemanager.ui.view.ErrorLayout
import com.chub.officemanager.ui.view.Loading
import com.chub.officemanager.ui.view.OfficeItemsList
import com.chub.officemanager.ui.view.OfficeTopBar

@Composable
fun SearchItemsScreen(
    isSelectionScreen: Boolean = false,
    onItemClicked: (OfficeItem) -> Unit,
    onFabClick: () -> Unit = {},
    viewModel: SearchItemsViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val titleId = if (isSelectionScreen) R.string.title_search_items_to_add else R.string.title_search_items
    OfficeManagerTheme {
        Scaffold(topBar = {
            OfficeTopBar(stringResource(id = titleId))
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
                    is Result.Error -> ErrorLayout((state.value as Result.Error).errorMessage)
                    is Result.Success<SearchItemsUiState> -> Content(content, viewModel, onItemClicked)
                }
            }
        }
    }
}

@Composable
private fun Content(
    content: Result.Success<SearchItemsUiState>,
    viewModel: SearchItemsViewModel,
    onItemClicked: (OfficeItem) -> Unit
) {
    Column {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = content.data.filter,
            onValueChange = { viewModel.onFilterChanged(it) },
            placeholder = { Text(stringResource(id = R.string.search)) },
            trailingIcon = { Icon(Icons.Filled.Search, stringResource(id = R.string.search)) }
        )
        OfficeItemsList(content.data.items, onItemClicked)
    }
}

