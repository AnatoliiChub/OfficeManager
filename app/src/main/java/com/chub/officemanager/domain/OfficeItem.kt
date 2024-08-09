package com.chub.officemanager.domain

data class OfficeItem(
    val id: Int = NONE,
    val name: String = "",
    val description: String = "",
    val type: String = "",
    val relations: List<OfficeItem> = emptyList()
) {
    companion object {
        const val NONE = -1
    }
}