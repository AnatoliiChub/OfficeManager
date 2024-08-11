package com.chub.officemanager.data.relations

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.chub.officemanager.data.entity.TypeEntity
import com.chub.officemanager.util.OfficeItem

data class ItemRelation(
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
) {
    fun toOfficeItem(): OfficeItem {
        return OfficeItem(
            itemId,
            name,
            description,
            type,
        )
    }
}