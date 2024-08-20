package com.chub.officemanager.ui.screens.addedit

import com.chub.officemanager.util.ErrorMessage
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.OfficeItem.Companion.NONE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TemporaryItemState(
    val currentItemId: MutableStateFlow<Long> = MutableStateFlow(NONE),
    val name: MutableStateFlow<String> = MutableStateFlow(""),
    val description: MutableStateFlow<String> = MutableStateFlow(""),
    val type: MutableStateFlow<String> = MutableStateFlow(""),
    val relations: MutableStateFlow<MutableList<OfficeItem>> = MutableStateFlow(mutableListOf()),
    val error : MutableStateFlow<ErrorMessage> = MutableStateFlow(ErrorMessage.NONE)
) {
    fun update(item: OfficeItem) {
        name.value = item.name
        description.value = item.description
        type.value = item.type
        relations.value = item.relations.toMutableList()
    }

    fun toOfficeItem(): OfficeItem {
        return OfficeItem(
            id = currentItemId.value,
            name = name.value,
            description = description.value,
            type = type.value,
            relations = relations.value
        )
    }

    fun addRelations(officeItem: OfficeItem) {
        relations.update {
            relations.value.toMutableList().apply { add(officeItem) }
        }
    }

    fun removeRelation(officeItem: OfficeItem) {
        relations.update {
            relations.value.toMutableList().apply { remove(officeItem) }
        }
    }
}