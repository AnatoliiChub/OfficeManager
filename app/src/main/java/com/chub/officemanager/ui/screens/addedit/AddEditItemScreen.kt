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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.chub.officemanager.R
import com.chub.officemanager.domain.OfficeItem
import com.chub.officemanager.domain.OfficeItem.Companion.NONE
import com.chub.officemanager.domain.Result
import com.chub.officemanager.ui.theme.OfficeManagerTheme
import com.chub.officemanager.ui.view.ErrorLayout
import com.chub.officemanager.ui.view.ItemOperation
import com.chub.officemanager.ui.view.Loading
import com.chub.officemanager.ui.view.OfficeItemLayout
import com.chub.officemanager.ui.view.OfficeTopBar

enum class FieldType {
    NAME, DESCRIPTION, TYPE
}

data class InputField(val type: FieldType, val value: String)
data class Label(val text: String)
data object AddNewRelation

const val NAME_MAX_LINES = 1
const val DESCRIPTION_MAX_LINES = 3
const val TYPE_MAX_LINES = 1

@Composable
fun AddEditScreen(
    itemId: Int,
    onItemClick: (OfficeItem) -> Unit,
    onItemsSaved: () -> Unit,
    onAddButtonClick: () -> Unit,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    val isCreatingNewItem = itemId == NONE
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val title =
        if (isCreatingNewItem) stringResource(id = R.string.title_add_items) else stringResource(id = R.string.title_edit_items)

    OfficeManagerTheme {
        Scaffold(topBar = {
            OfficeTopBar(title)
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = onItemsSaved,
            ) {
                Icon(Icons.Filled.Done, stringResource(id = R.string.done_action))
            }

        }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (val content = state.value) {
                    Result.Loading -> Loading()
                    is Result.Error -> ErrorLayout((state.value as Result.Error).errorMessage)
                    is Result.Success<ItemUiState> -> Content(
                        content.data,
                        viewModel::onNameChanged,
                        viewModel::onDescriptionChanged,
                        viewModel::onTypeChanged,
                        onItemClick,
                        onAddButtonClick,
                        viewModel::onActionClick
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.Content(
    state: ItemUiState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onTypeChanged: (String) -> Unit,
    onItemClick: (OfficeItem) -> Unit,
    onAddButtonClick: () -> Unit,
    onRemoveAction: (OfficeItem) -> Unit,
) {
    val fields = mutableListOf<Any>(
        InputField(FieldType.NAME, state.name),
        InputField(FieldType.DESCRIPTION, state.description),
        InputField(FieldType.TYPE, state.type),
    )
    fields.add(Label(stringResource(R.string.relations)))
    state.relations.forEach {
        fields.add(it)
    }
    fields.add(AddNewRelation)
    val verticalPadding = 16.dp
    val horizontalPadding = 8.dp
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(verticalPadding),
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        items(fields.size) { index ->
            when (val listItem = fields[index]) {
                is InputField -> {
                    InputFieldLayout(
                        listItem, state.name, state.description, state.type,
                        onNameChanged = onNameChanged,
                        onDescriptionChanged = onDescriptionChanged, onTypeChanged = onTypeChanged
                    )
                }

                is Label -> RelationsLabel(listItem)
                is OfficeItem -> {
                    OfficeItemLayout(item = listItem, listOf(ItemOperation.Delete),
                        onClick = onItemClick, onActionClick = {
                            if (it == ItemOperation.Delete) {
                                onRemoveAction(listItem)
                            }
                        })
                }

                is AddNewRelation -> AddNewRelationButton(onAddButtonClick)
            }
        }
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
            .align(Alignment.Center),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),
        onClick = onAddButtonClick
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

@Composable
private fun InputFieldLayout(
    listItem: InputField,
    name: String,
    description: String,
    type: String,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onTypeChanged: (String) -> Unit
) {
    when (listItem.type) {
        FieldType.NAME -> {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = onNameChanged,
                label = { Text(stringResource(id = R.string.name)) },
                maxLines = NAME_MAX_LINES
            )
        }

        FieldType.DESCRIPTION -> {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = onDescriptionChanged,
                label = { Text(stringResource(id = R.string.description)) },
                maxLines = DESCRIPTION_MAX_LINES
            )
        }

        FieldType.TYPE -> {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = type,
                onValueChange = onTypeChanged,
                label = { Text(stringResource(id = R.string.type)) },
                maxLines = TYPE_MAX_LINES
            )
        }
    }
}