package com.chub.officemanager.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chub.officemanager.R
import com.chub.officemanager.domain.OfficeItem
import com.chub.officemanager.ui.theme.OfficeManagerTheme
import com.chub.officemanager.ui.view.OfficeItemsList
import com.chub.officemanager.ui.view.OfficeTopBar

@Composable
fun SearchToAddScreen(onItemClicked: (OfficeItem) -> Unit) {
    val items = listOf(
        OfficeItem(1, "35", "A desk for working", "Desk"),
        OfficeItem(2, "32", "A chair for sitting", "Chair"),
        OfficeItem(3, "10w LED", "A lamp for lighting", "Lamp"),
        OfficeItem(4, "Dell 24HL", "A monitor for viewing", "Monitor"),
        OfficeItem(5, "Logitech 121", "A keyboard for typing", "Keyboard"),
        OfficeItem(6, "Logitech 121", "A mouse for clicking", "Mouse"),
        OfficeItem(7, "Max", "A person for working", "Employee"),
        OfficeItem(8, "Dexter", "A person for working", "Employee"),
        OfficeItem(9, "John", "A person for working", "Employee"),
    )

    OfficeManagerTheme {
        Scaffold(topBar = {
            OfficeTopBar(stringResource(id = R.string.title_search_items_to_add))
        }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Column {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = "",
                        onValueChange = {},
                        placeholder = { Text(stringResource(id = R.string.search)) },
                        trailingIcon = { Icon(Icons.Filled.Search, stringResource(id = R.string.search)) }
                    )
                    OfficeItemsList(items, onItemClicked)
                }
            }
        }
    }
}