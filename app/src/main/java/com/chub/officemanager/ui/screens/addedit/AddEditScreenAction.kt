package com.chub.officemanager.ui.screens.addedit

import com.chub.officemanager.util.OfficeItem

sealed class AddEditScreenAction {

    sealed class NavigationAction : AddEditScreenAction() {
        data object NavigateToAddItem : NavigationAction()
        data object NavigateBack : NavigationAction()
    }

    sealed class StateAction : AddEditScreenAction() {
        data class FieldChanged(val type: FieldType, val value: String) : StateAction()
        data class RemoveItem(val item: OfficeItem) : StateAction()
    }
}