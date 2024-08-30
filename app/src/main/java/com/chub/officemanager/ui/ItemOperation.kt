package com.chub.officemanager.ui

import com.chub.officemanager.util.OfficeItem

data class ItemOperation(val operationType : OperationType, val callback : (OfficeItem) -> Unit)