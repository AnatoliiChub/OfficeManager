package com.chub.officemanager.data.relations

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.chub.officemanager.data.entity.ItemEntity
import com.chub.officemanager.data.entity.RelationEntity
import com.chub.officemanager.data.entity.TypeEntity
import com.chub.officemanager.util.OfficeItem

data class ItemWithRelations(
    @ColumnInfo(name = "itemId")
    val itemId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "typeId")
    val typeId: Long,

    @Relation(
        parentColumn = "typeId",
        entityColumn = "typeId",
        entity = TypeEntity::class,
        projection = ["name"],
    )
    val type: String,

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
    val relations: List<ItemRelation>
) {
    fun toOfficeItem(): OfficeItem {
        return OfficeItem(
            itemId,
            name,
            description,
            type,
            relations.map { it.toOfficeItem() }
        )
    }
}