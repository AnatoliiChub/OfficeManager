package com.chub.officemanager.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.chub.officemanager.util.OfficeItem

@Composable
fun OfficeItemsList(
    list: List<OfficeItem>,
    operations : List<ItemOperation>,
    onItemClicked: (OfficeItem) -> Unit,
    onRemoveItem: (OfficeItem) -> Unit = {}
) {
    val verticalPadding = 12.dp
    val horizontalPadding = 8.dp
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(verticalPadding),
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        items(list.size) { index ->
            OfficeItemLayout(list[index], operations, {
                onItemClicked(it)
                Log.d("OfficeItemsList", "Item clicked : $it")
            }, {
                if (operations.isNotEmpty()) {
                    val item = list[index]
                    when (it) {
                        ItemOperation.Edit -> onItemClicked(item)
                        ItemOperation.Delete -> onRemoveItem(item)
                    }
                }
            })
        }
    }
}