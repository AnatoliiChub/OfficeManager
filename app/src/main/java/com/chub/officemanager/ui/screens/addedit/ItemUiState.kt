package com.chub.officemanager.ui.screens.addedit

import com.chub.officemanager.util.OfficeItem

data class ItemUiState(
    val name: String = "",
    val description: String = "",
    val type: String = "",
    val relations: List<OfficeItem> = emptyList()
)