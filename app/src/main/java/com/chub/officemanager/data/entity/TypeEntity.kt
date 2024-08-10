package com.chub.officemanager.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(value = ["typeId"]),
        Index(value = ["name"], unique = true)
    ]
)
data class TypeEntity(
    @PrimaryKey(autoGenerate = true)
    val typeId: Long = 0,
    val name: String
)