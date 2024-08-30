package com.chub.officemanager.ui.screens.addedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chub.officemanager.R
import com.chub.officemanager.ui.ItemOperation
import com.chub.officemanager.ui.OperationType
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.NavigationAction
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.NavigationAction.NavigateBack
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.NavigationAction.NavigateToAddItem
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.StateAction
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.StateAction.FieldChanged
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.StateAction.RemoveItem
import com.chub.officemanager.ui.theme.OfficeManagerTheme
import com.chub.officemanager.ui.view.InputText
import com.chub.officemanager.ui.view.Loading
import com.chub.officemanager.ui.view.OfficeItemLayout
import com.chub.officemanager.ui.view.OfficeTopBar
import com.chub.officemanager.util.ContentResult
import com.chub.officemanager.util.ErrorMessage
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.OfficeItem.Companion.NONE
import kotlinx.coroutines.launch

const val DESCRIPTION_MAX_LINES = 3

enum class FieldType {
    NAME, DESCRIPTION, TYPE
}

private data class InputField(val type: FieldType, val value: String)
private data class Label(val text: String)
private data object AddNewRelation

@Composable
fun AddEditScreen(
    itemId: Long,
    selectedRelation: OfficeItem?,
    onNavigation: (NavigationAction) -> Unit,
    viewModel: AddEditViewModel = hiltViewModel()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    //Default savedStateHandle from viewmodel can't handle result back case,
    //so we need to handle it manually
    LaunchedEffect(selectedRelation) {
        viewModel.onSelectedRelationToAdd(selectedRelation)
    }

    OfficeManagerTheme {
        Scaffold(topBar = {
            OfficeTopBar(
                if (itemId == NONE) stringResource(id = R.string.title_add_items)
                else stringResource(id = R.string.title_edit_items),
                onNavigationClick = { onNavigation(NavigateBack) }
            )
        }, floatingActionButton = {
            if (state.value is ContentResult.Success<ItemUiState>) {
                Fab(viewModel::saveItem, snackBarHostState)
            }
        }, snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (val content = state.value) {
                    ContentResult.Loading -> Loading()
                    is ContentResult.Success<ItemUiState> -> {
                        Content(content.data) {
                            when (it) {
                                is StateAction -> viewModel.onAction(it)
                                is NavigationAction -> onNavigation(it)
                            }
                        }
                        content.data.errorMessage.let {
                            if (it != ErrorMessage.NONE) {
                                ErrorMessage(it, snackBarHostState) { viewModel.setErrorMessage(ErrorMessage.NONE) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    it: ErrorMessage,
    snackBarHostState: SnackbarHostState,
    onShown: () -> Unit
) {
    val errorText = when (it) {
        ErrorMessage.ITEM_IS_ALREADY_RELATED -> stringResource(id = R.string.relation_already_related)
        ErrorMessage.ITEM_HAS_BEEN_ALREADY_ADDED -> stringResource(id = R.string.relation_already_added)
        else -> stringResource(id = R.string.error)

    }
    LaunchedEffect(it) {
        snackBarHostState.showSnackbar(
            errorText,
            withDismissAction = true,
        )
        onShown()
    }
}

@Composable
private fun Fab(
    onSaveItem: (() -> Unit) -> Unit,
    snackBarHostState: SnackbarHostState,
) {

    val coroutineScope = rememberCoroutineScope()
    val savedLabel = stringResource(id = R.string.object_was_saved)

    FloatingActionButton(
        onClick = {
            onSaveItem {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(savedLabel, withDismissAction = true)
                }
            }
        },
    ) {
        Icon(Icons.Filled.Done, stringResource(id = R.string.done_action))
    }

}

@Composable
private fun BoxScope.Content(
    state: ItemUiState,
    onAction: (AddEditScreenAction) -> Unit
) {
    val fields = mutableListOf<Any>(
        InputField(FieldType.NAME, state.name),
        InputField(FieldType.DESCRIPTION, state.description),
        InputField(FieldType.TYPE, state.type),
    ).apply {
        add(Label(stringResource(R.string.relations)))
        state.relations.forEach {
            add(it)
        }
        add(AddNewRelation)
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
    ) {
        items(fields.size, key = {
            when (val item = fields[it]) {
                is InputField -> item.type
                is Label -> item.text
                is OfficeItem -> item.id
                else -> item.javaClass.name
            }
        }
        ) { index ->
            when (val listItem = fields[index]) {
                is InputField -> InputFieldLayout(listItem, state, onAction)
                is Label -> RelationsLabel(listItem)
                is OfficeItem -> {
                    OfficeItemLayout(item = listItem,
                        listOf(ItemOperation(OperationType.Delete) { onAction(RemoveItem(listItem)) }),
                        onClick = {})
                }

                is AddNewRelation -> AddNewRelationButton { onAction(NavigateToAddItem) }
            }
        }
    }
}

@Composable
private fun InputFieldLayout(
    listItem: InputField,
    state: ItemUiState,
    onAction: (AddEditScreenAction) -> Unit
) {
    when (listItem.type) {
        FieldType.NAME -> InputText(
            state.name,
            { onAction(FieldChanged(FieldType.NAME, it)) },
            R.string.name
        )

        FieldType.DESCRIPTION -> {
            InputText(
                state.description,
                { onAction(FieldChanged(FieldType.DESCRIPTION, it)) },
                R.string.description,
                DESCRIPTION_MAX_LINES
            )
        }

        FieldType.TYPE -> InputText(
            state.type,
            { onAction(FieldChanged(FieldType.TYPE, it)) },
            R.string.type
        )
    }
}

@Composable
private fun RelationsLabel(listItem: Label) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = listItem.text,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun BoxScope.AddNewRelationButton(onAddButtonClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.Center), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ), onClick = onAddButtonClick
    ) {
        Icon(
            modifier = Modifier
                .padding(vertical = 36.dp)
                .fillMaxWidth(),
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.done_action)
        )
    }
}

