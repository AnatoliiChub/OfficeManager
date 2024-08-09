package com.chub.officemanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chub.officemanager.R
import com.chub.officemanager.domain.OfficeItem
import com.chub.officemanager.ui.view.OfficeItemsList
import com.chub.officemanager.ui.view.OfficeTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficeItemsScreen(items: List<OfficeItem>) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(topBar = {
        OfficeTopBar(scrollBehavior = scrollBehavior)
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { },
        ) { Icon(Icons.Filled.Add, stringResource(id = R.string.add_action)) }
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                placeholder = { Text(stringResource(id = R.string.search)) },
                trailingIcon = { Icon(Icons.Filled.Search, stringResource(id = R.string.search)) }
            )
            OfficeItemsList(items)
        }
    }
}