package com.chub.officemanager

import com.chub.officemanager.NavArgs.ITEM_ID
import com.chub.officemanager.OfficeScreens.ADD_EDIT_OFFICE_ITEM
import com.chub.officemanager.OfficeScreens.OFFICE_ITEMS_LIST
import com.chub.officemanager.OfficeScreens.SEARCH_OFFICE_ITEMS_TO_ADD

object OfficeScreens {
    const val OFFICE_ITEMS_LIST = "office_items_list"
    const val ADD_EDIT_OFFICE_ITEM = "add_edit_office_item"
    const val SEARCH_OFFICE_ITEMS_TO_ADD = "search_to_add"
}

object NavArgs {
    const val ITEM_ID = "item_id"
}

object OfficeDestinations {
    const val SEARCH_TO_ADD_ROUTE = SEARCH_OFFICE_ITEMS_TO_ADD
    const val SEARCH_OFFICE_ITEMS_ROUTE = OFFICE_ITEMS_LIST
    const val ADD_EDIT_OFFICE_ITEM_ROUTE = "$ADD_EDIT_OFFICE_ITEM/{$ITEM_ID}"
}