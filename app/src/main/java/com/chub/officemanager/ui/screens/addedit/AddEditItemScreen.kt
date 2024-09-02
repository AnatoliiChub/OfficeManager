package com.chub.officemanager.ui.screens.addedit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chub.officemanager.FabState
import com.chub.officemanager.FabType
import com.chub.officemanager.R
import com.chub.officemanager.ui.ItemOperation
import com.chub.officemanager.ui.OperationType
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.NavigationAction
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.NavigationAction.NavigateToAddItem
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.StateAction
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.StateAction.FieldChanged
import com.chub.officemanager.ui.screens.addedit.AddEditScreenAction.StateAction.RemoveItem
import com.chub.officemanager.ui.view.InputText
import com.chub.officemanager.ui.view.Loading
import com.chub.officemanager.ui.view.OfficeItemLayout
import com.chub.officemanager.util.ContentResult
import com.chub.officemanager.util.ErrorMessage
import com.chub.officemanager.util.OfficeItem
import kotlinx.coroutines.launch

const val DESCRIPTION_MAX_LINES = 3

@Composable
fun AddEditScreen(
    selectedRelation: OfficeItem?,
    onNavigation: (AddEditScreenAction) -> Unit,
    onFabStateUpdate: (FabState) -> Unit,
    snackBarHostState: SnackbarHostState,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val savedLabel = stringResource(id = R.string.object_was_saved)
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onFabStateUpdate(FabState(true, FabType.SAVE, onClick = {
            viewModel.saveItem {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar(savedLabel, withDismissAction = true)
                }
            }
        }))
    }

    //Default savedStateHandle from viewmodel can't handle result back case,
    //so we need to handle it manually
    LaunchedEffect(selectedRelation) {
        viewModel.onSelectedRelationToAdd(selectedRelation)
    }
    Box {
        when (val content = state.value) {
            ContentResult.Loading -> Loading()
            is ContentResult.Success<ItemUiState> -> {
                Content(content.data) {
                    when (it) {
                        is NavigationAction -> onNavigation(it)
                        is StateAction -> viewModel.onAction(it)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.Content(
    state: ItemUiState,
    onAction: (AddEditScreenAction) -> Unit
) {
    val fields = state.fields

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
    ) {
        items(fields.size, key = {
            when (val item = fields[it]) {
                is InputFieldState -> item.type
                is OfficeItem -> item.id
                else -> item.javaClass.name
            }
        }
        ) { index ->
            when (val listItem = fields[index]) {
                is InputFieldState -> {
                    val (value, label, maxLines) = when (listItem.type) {
                        FieldType.NAME -> Triple(state.name, R.string.name, Int.MAX_VALUE)
                        FieldType.DESCRIPTION -> Triple(state.description, R.string.description, DESCRIPTION_MAX_LINES)
                        FieldType.TYPE -> Triple(state.type, R.string.type, Int.MAX_VALUE)
                    }
                    InputText(value, { onAction(FieldChanged(listItem.type, it)) }, label, maxLines)
                }

                is RelationsLabel -> RelationsLabel()
                is OfficeItem -> {
                    Box(modifier = Modifier.animateItemPlacement()  ) {
                        OfficeItemLayout(item = listItem,
                            listOf(ItemOperation(OperationType.Delete) { onAction(RemoveItem(listItem)) }),
                            onClick = {})
                    }
                }

                is AddNewRelation -> AddNewRelationButton { onAction(NavigateToAddItem) }
            }
        }
    }
}

@Composable
private fun RelationsLabel() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = stringResource(id = R.string.relations),
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

