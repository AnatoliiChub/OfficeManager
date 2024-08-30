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
import com.chub.officemanager.ui.ItemOperation
import com.chub.officemanager.ui.OperationType
import com.chub.officemanager.util.OfficeItem

@Composable
fun PopupMenu(
    isMenuVisible: MutableState<Boolean>,
    pressOffset: DpOffset,
    itemHeight: Dp,
    officeItem: OfficeItem,
    dropdownItems: List<ItemOperation>) {
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
                    val text = when (it.operationType) {
                        OperationType.Edit -> stringResource(id = R.string.edit_action)
                        OperationType.Delete -> stringResource(id = R.string.delete_action)
                    }
                    Text(text)
                },
                onClick = {
                    it.callback(officeItem)
                    isMenuVisible.value = false
                },
            )
        }
    }
}