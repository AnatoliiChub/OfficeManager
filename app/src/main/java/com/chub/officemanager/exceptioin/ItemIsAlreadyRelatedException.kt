package com.chub.officemanager.exceptioin

class ItemIsAlreadyRelatedException : Exception() {
    override val message: String
        get() = "item duplicated"
}