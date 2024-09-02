package com.chub.officemanager.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chub.officemanager.ui.ItemOperation
import com.chub.officemanager.util.OfficeItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OfficeItemsList(
    list: List<OfficeItem>,
    operations: List<ItemOperation>,
    onOperation: (OfficeItem) -> Unit
) {
    val verticalPadding = 12.dp
    val horizontalPadding = 8.dp
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(verticalPadding),
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        items(list.size, key = { list[it].id }) { index ->
            Box(modifier = Modifier.animateItemPlacement()) {
                OfficeItemLayout(
                    list[index],
                    operations,
                    onClick = { onOperation(it) }
                )
            }
        }
    }
}