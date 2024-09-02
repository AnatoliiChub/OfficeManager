package com.chub.officemanager.ui.screens.addedit

import com.chub.officemanager.util.ErrorMessage
import com.chub.officemanager.util.OfficeItem

data class ItemUiState(
    val name: String = "",
    val description: String = "",
    val type: String = "",
    private val relations: List<OfficeItem> = emptyList(),
    val errorMessage: ErrorMessage = ErrorMessage.NONE
) {
    val fields = mutableListOf<Any>(
        InputFieldState(FieldType.NAME, name),
        InputFieldState(FieldType.DESCRIPTION, description),
        InputFieldState(FieldType.TYPE, type),
    ).apply {
        add(RelationsLabel)
        relations.forEach {
            add(it)
        }
        add(AddNewRelation)
    }
}

data class InputFieldState(val type: FieldType, val value: String)
data object RelationsLabel
data object AddNewRelation
enum class FieldType {
    NAME, DESCRIPTION, TYPE
}
