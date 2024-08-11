package com.chub.officemanager.data.repo

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.chub.officemanager.data.OfficeDB
import com.chub.officemanager.data.entity.ItemEntity
import com.chub.officemanager.data.entity.RelationEntity
import com.chub.officemanager.data.entity.TypeEntity
import com.chub.officemanager.exceptioin.ItemDuplicatedException
import com.chub.officemanager.util.OfficeItem
import com.chub.officemanager.util.OfficeItem.Companion.NONE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfficeItemRepository @Inject constructor(
    private val officeDB: OfficeDB
) {
    suspend fun storeItem(item: OfficeItem) {
        var itemId = item.id
        val typeId = try {
            officeDB.typeEntityDAO().insert(TypeEntity(name = item.type))
        } catch (exception: SQLiteConstraintException) {
            Log.e("OfficeItemRepository get", "exception : $exception")
            officeDB.typeEntityDAO().getByName(item.type)!!.typeId
        }
        val result = officeDB.itemEntityDAO().insert(
            ItemEntity(itemId = item.id, name = item.name, description = item.description, typeId = typeId)
        )
        if (itemId == NONE) {
            itemId = result
        }
        item.relations.map {
            val relationId = it.id
            RelationEntity(parentId = itemId, childId = relationId)
        }.also {
            officeDB.relationDAO().delete(itemId)
            try {
                officeDB.relationDAO().insertAll(it)
            } catch (exception: SQLiteConstraintException) {
                Log.e("OfficeItemRepository get", "exception : $exception")
                throw ItemDuplicatedException()
            }
        }
    }

    suspend fun getItemById(id: Long): OfficeItem {
        val itemWithRelations = officeDB.itemEntityDAO().getById(id)
        return itemWithRelations.toOfficeItem()
    }

    suspend fun removeItem(item: OfficeItem) {
        officeDB.itemEntityDAO().delete(item.id)
    }

    fun search(text: String): Flow<List<OfficeItem>> {
        return officeDB.itemEntityDAO().search(text).map { items -> items.map { it.toOfficeItem() } }
    }
}