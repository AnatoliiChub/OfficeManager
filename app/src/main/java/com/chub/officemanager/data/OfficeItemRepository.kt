package com.chub.officemanager.data

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.chub.officemanager.data.entity.ItemEntity
import com.chub.officemanager.data.entity.RelationEntity
import com.chub.officemanager.data.entity.TypeEntity
import com.chub.officemanager.data.relations.ItemWithRelations
import com.chub.officemanager.domain.OfficeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfficeItemRepository @Inject constructor(
    private val officeDB: OfficeDB
) {
    suspend fun storeItem(item: OfficeItem) {
        val typeId = try {
            officeDB.typeEntityDAO().insert(TypeEntity(name = item.type))
        } catch (exception: SQLiteConstraintException) {
            Log.e("OfficeItemRepository get", "exception : $exception")
            officeDB.typeEntityDAO().getByName(item.type)!!.typeId
        }
        officeDB.itemEntityDAO().insert(
            ItemEntity(itemId = item.id, name = item.name, description = item.description, typeId = typeId)
        )
        item.relations.map {
            val relationId = it.id
            RelationEntity(parentId = item.id, childId = relationId)
        }.also {
            officeDB.relationDAO().delete(item.id)
            officeDB.relationDAO().insertAll(it)
        }
    }

    suspend fun getItemById(id: Long): OfficeItem {
        val itemWithRelations = officeDB.itemEntityDAO().getById(id)
        return map(itemWithRelations)
    }

    suspend fun removeItem(item: OfficeItem) {
        officeDB.itemEntityDAO().delete(item.id)
    }

    fun getAllItems(): Flow<List<OfficeItem>> {
        return officeDB.itemEntityDAO().getAll().map { items ->
            items.map { itemWithRelations ->
                map(itemWithRelations)
            }
        }
    }

    //TODO MOVE TO MAPPER
    private fun map(
        itemWithRelations: ItemWithRelations,
    ): OfficeItem {
        val item = itemWithRelations.item.item
        return OfficeItem(
            item.itemId,
            item.name,
            item.description,
            itemWithRelations.item.type,
            itemWithRelations.relations.map { relation ->
                val item = relation.item
                OfficeItem(
                    item.itemId,
                    item.name,
                    item.description,
                    relation.type
                )
            }
        )
    }
}