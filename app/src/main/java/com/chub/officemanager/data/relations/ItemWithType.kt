package com.chub.officemanager.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.chub.officemanager.data.entity.ItemEntity
import com.chub.officemanager.data.entity.TypeEntity


data class ItemWithType(
    @Embedded val item: ItemEntity,
    @Relation(
        parentColumn = "typeId",
        entityColumn = "typeId",
        entity = TypeEntity::class,
        projection = ["name"],
    )
    val type: String,
)