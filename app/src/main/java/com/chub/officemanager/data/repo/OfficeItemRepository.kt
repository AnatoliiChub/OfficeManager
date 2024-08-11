package com.chub.officemanager.data.repo

import android.database.sqlite.SQLiteConstraintException
import com.chub.officemanager.data.OfficeDB
import com.chub.officemanager.data.dao.ItemEntityDao
import com.chub.officemanager.data.dao.RelationDao
import com.chub.officemanager.data.dao.TypeEntityDao
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
    private val db: OfficeDB,
    private val itemDao: ItemEntityDao,
    private val relationDao: RelationDao,
    private val typeDao: TypeEntityDao
) {
    suspend fun storeItem(item: OfficeItem) {
        db.runInTransaction {
            val typeId = getTypeId(item.type)
            val itemId = if (item.id == NONE) itemDao.insert(createItemEntity(item, typeId)) else item.id
            storeRelations(itemId, item.relations.map { RelationEntity(parentId = itemId, childId = it.id) })
        }
    }

    suspend fun getItemById(id: Long): OfficeItem {
        return itemDao.getById(id).toOfficeItem()
    }

    suspend fun removeItem(item: OfficeItem) {
        itemDao.delete(item.id)
    }

    fun search(text: String): Flow<List<OfficeItem>> {
        return itemDao.search(text).map { items -> items.map { it.toOfficeItem() } }
    }

    private fun getTypeId(type: String): Long {
        return try {
            typeDao.insert(TypeEntity(name = type))
        } catch (exception: SQLiteConstraintException) {
            typeDao.getByName(type)!!.typeId
        }
    }

    private fun storeRelations(itemId: Long, relations: List<RelationEntity>) {
        relationDao.delete(itemId)
        try {
            relationDao.insertAll(relations)
        } catch (exception: SQLiteConstraintException) {
            throw ItemDuplicatedException()
        }
    }

    private fun createItemEntity(item: OfficeItem, typeId: Long) = ItemEntity(
        itemId = item.id, name = item.name, description = item.description, typeId = typeId
    )
}