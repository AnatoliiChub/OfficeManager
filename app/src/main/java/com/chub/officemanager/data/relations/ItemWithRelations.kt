package com.chub.officemanager.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.chub.officemanager.data.entity.ItemEntity
import com.chub.officemanager.data.entity.RelationEntity

data class ItemWithRelations(
    @Embedded val item: ItemWithType,

    @Relation(
        entity = ItemEntity::class,
        parentColumn = "itemId",
        entityColumn = "itemId",
        associateBy = androidx.room.Junction(
            value = RelationEntity::class,
            parentColumn = "parentId",
            entityColumn = "childId"
        )
    )
    val relations: List<ItemWithType>
)