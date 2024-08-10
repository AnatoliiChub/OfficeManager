package com.chub.officemanager.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(value = ["itemId"]),
        Index(value = ["typeId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = TypeEntity::class,
            parentColumns = ["typeId"],
            childColumns = ["typeId"],
        )]
)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    val name: String,
    val description: String,
    val typeId: Long
)







