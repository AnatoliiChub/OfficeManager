package com.chub.officemanager.ui.screens.addedit

import com.chub.officemanager.util.ErrorMessage
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.OfficeItem.Companion.NONE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class TemporaryItemState {
    private val _currentItemId: MutableStateFlow<Long> = MutableStateFlow(NONE)
    private val _name: MutableStateFlow<String> = MutableStateFlow("")
    private val _description: MutableStateFlow<String> = MutableStateFlow("")
    private val _type: MutableStateFlow<String> = MutableStateFlow("")
    private val _relations: MutableStateFlow<MutableList<OfficeItem>> = MutableStateFlow(mutableListOf())
    private val _error : MutableStateFlow<ErrorMessage> = MutableStateFlow(ErrorMessage.NONE)

    val currentItemId: StateFlow<Long> = _currentItemId
    val name: StateFlow<String> = _name
    val description: StateFlow<String> = _description
    val type: StateFlow<String> = _type
    val relations: StateFlow<List<OfficeItem>> = _relations
    val error: StateFlow<ErrorMessage> = _error

    fun update(item: OfficeItem) {
        _name.value = item.name
        _description.value = item.description
        _type.value = item.type
        _relations.value = item.relations.toMutableList()
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
        _relations.update {
            relations.value.toMutableList().apply { add(officeItem) }
        }
    }

    fun removeRelation(officeItem: OfficeItem) {
        _relations.update {
            relations.value.toMutableList().apply { remove(officeItem) }
        }
    }

    fun currentItemId(currentItemId: Long) {
        this._currentItemId.value = currentItemId
    }

    fun name(name: String) {
        this._name.value = name
    }

    fun description(description: String) {
        this._description.value = description
    }

    fun type(type: String) {
        this._type.value = type
    }

    fun error(message: ErrorMessage) {
        this._error.value = message
    }
}