package com.chub.officemanager

import com.chub.officemanager.NavArgs.ITEM_ID
import com.chub.officemanager.OfficeScreens.ADD_EDIT_OFFICE_ITEM

private object OfficeScreens {
    const val OFFICE_ITEMS_LIST = "office_items_list"
    const val ADD_EDIT_OFFICE_ITEM = "add_edit_office_item"
}

object NavArgs {
    const val ITEM_ID = "item_id"
}

object OfficeDestinations {
    const val OFFICE_ITEMS_ROUTE = "office_items"
    const val ADD_EDIT_OFFICE_ITEM_ROUTE = "$ADD_EDIT_OFFICE_ITEM/{$ITEM_ID}"
}