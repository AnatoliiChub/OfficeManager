package com.chub.officemanager.ui.view

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.chub.officemanager.R

enum class ItemOperation {
    Edit,
    Delete
}

@Composable
fun PopupMenu(
    isMenuVisible: MutableState<Boolean>,
    pressOffset: DpOffset,
    itemHeight: Dp,
    dropdownItems: List<ItemOperation>,
    onActionClick: (ItemOperation) -> Unit
) {
    DropdownMenu(
        expanded = isMenuVisible.value,
        onDismissRequest = {
            isMenuVisible.value = false
        },
        offset = pressOffset.copy(
            y = pressOffset.y - itemHeight
        )
    ) {
        dropdownItems.forEach {
            DropdownMenuItem(
                text = @Composable {
                    val text = when (it) {
                        ItemOperation.Edit -> stringResource(id = R.string.edit_action)
                        ItemOperation.Delete -> stringResource(id = R.string.delete_action)
                    }
                    Text(text)
                },
                onClick = {
                    onActionClick(it)
                    isMenuVisible.value = false
                },
            )
        }
    }
}