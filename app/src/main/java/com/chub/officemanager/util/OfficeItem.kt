package com.chub.officemanager.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfficeItem(
    val id: Long = NONE,
    val name: String = "",
    val description: String = "",
    val type: String = "",
    val relations: List<OfficeItem> = emptyList()
) : Parcelable {
    companion object {
        const val NONE = 0L
    }
}