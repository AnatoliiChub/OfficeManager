package com.chub.officemanager.ui.screens.search

import com.chub.officemanager.util.OfficeItem

sealed class SearchItemsScreenAction {
    sealed class Navigation : SearchItemsScreenAction() {
        data class ItemClicked(val item: OfficeItem) : Navigation()
    }

    sealed class StateChange : SearchItemsScreenAction() {
        data class ItemRemove(val item: OfficeItem) : StateChange()
        data class FilterChanged(val filter: String) : StateChange()
    }
}