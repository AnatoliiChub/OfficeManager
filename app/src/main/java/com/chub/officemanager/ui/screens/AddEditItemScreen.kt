package com.chub.officemanager.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chub.officemanager.R
import com.chub.officemanager.domain.OfficeItem
import com.chub.officemanager.domain.OfficeItem.Companion.NONE
import com.chub.officemanager.ui.theme.OfficeManagerTheme
import com.chub.officemanager.ui.view.ItemOperation
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
    onAddButtonClick: () -> Unit
) {
    val isCreatingNewItem = itemId == NONE
    val item = if (isCreatingNewItem) OfficeItem() else OfficeItem(
        id = itemId,
        name = "35",
        description = "A simple Desk",
        type = "Desk",
        relations = listOf(
            OfficeItem(8, "Dexter", "A person for working", "Employee"),
            OfficeItem(9, "John", "A person for working", "Employee"),
        )
    )
    val title =
        if (isCreatingNewItem) stringResource(id = R.string.title_add_items) else stringResource(id = R.string.title_edit_items)

    val fields = mutableListOf<Any>(
        InputField(FieldType.NAME, item.name),
        InputField(FieldType.DESCRIPTION, item.description),
        InputField(FieldType.TYPE, item.type),
    )
    fields.add(Label(stringResource(R.string.relations)))
    item.relations.forEach {
        fields.add(it)
    }
    fields.add(AddNewRelation)
    var name by rememberSaveable {
        mutableStateOf(item.name)
    }

    var description by rememberSaveable {
        mutableStateOf(item.description)
    }

    var type by rememberSaveable {
        mutableStateOf(item.type)
    }

    val verticalPadding = 16.dp
    val horizontalPadding = 8.dp

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
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(verticalPadding),
                    contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding)
                ) {
                    items(fields.size) { index ->
                        when (val listItem = fields[index]) {
                            is InputField -> {
                                InputFieldLayout(listItem, name, description, type, onNameChanged = {
                                    name = it
                                }, onDescriptionChanged = {
                                    description = it
                                }, onTypeChanged = {
                                    type = it
                                })
                            }

                            is Label -> RelationsLabel(listItem)
                            is OfficeItem -> {
                                OfficeItemLayout(item = listItem, listOf(ItemOperation.Delete),
                                    onClick = onItemClick, onActionClick = {
                                        // Handle action click
                                    })
                            }

                            is AddNewRelation -> AddNewRelationButton(onAddButtonClick)

                        }
                    }
                }
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