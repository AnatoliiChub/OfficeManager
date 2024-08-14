package com.chub.officemanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    val name: String,
    val description: String,
    val typeId: Long
)







