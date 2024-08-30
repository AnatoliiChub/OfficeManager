package com.chub.officemanager.ui.screens.search

import com.chub.officemanager.util.OfficeItem

sealed class SearchItemsScreenAction {

    sealed class Navigation : SearchItemsScreenAction() {
        data object GoBack : Navigation()
        data object AddItem : Navigation()
        data class ItemClicked(val item: OfficeItem) : Navigation()
    }

    sealed class StateAction : SearchItemsScreenAction() {
        data class ItemRemove(val item: OfficeItem) : StateAction()
        data class FilterChanged(val filter: String) : StateAction()
    }
}