package com.chub.officemanager.ui.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.chub.officemanager.domain.OfficeItem

@Composable
fun OfficeItemsList(list: List<OfficeItem>, onItemClicked: (OfficeItem) -> Unit) {
    val verticalPadding = 12.dp
    val horizontalPadding = 8.dp
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(verticalPadding),
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding)
    ) {
        items(list.size) { index ->
            OfficeItemLayout(list[index], listOf(ItemOperation.Edit, ItemOperation.Delete), {
                onItemClicked(it)
                Log.d("OfficeItemsList", "Item clicked : $it")
            }, {
                Log.d("OfficeItemsList", "Action clicked: $it")
            })
        }
    }
}