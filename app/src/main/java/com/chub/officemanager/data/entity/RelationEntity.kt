package com.chub.officemanager.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(value = ["relationId"]),
        Index(value = ["childId"], unique = true),
    ]
)
data class RelationEntity(
    @PrimaryKey(autoGenerate = true)
    val relationId: Long = 0,
    val parentId: Long,
    val childId: Long
)