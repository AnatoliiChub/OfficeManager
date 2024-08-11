package com.chub.officemanager.exceptioin

class ItemDuplicatedException : Exception() {
    override val message: String
        get() = "item duplicated"
}