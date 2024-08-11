package com.chub.officemanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chub.officemanager.data.dao.ItemEntityDao
import com.chub.officemanager.data.dao.RelationDao
import com.chub.officemanager.data.dao.TypeEntityDao
import com.chub.officemanager.data.entity.ItemEntity
import com.chub.officemanager.data.entity.RelationEntity
import com.chub.officemanager.data.entity.TypeEntity

@Database(
    entities = [ItemEntity::class,
        TypeEntity::class,
        RelationEntity::class], version = 1,
    exportSchema = false
)
abstract class OfficeDB : RoomDatabase() {
    abstract fun itemEntityDAO(): ItemEntityDao
    abstract fun typeEntityDAO(): TypeEntityDao
    abstract fun relationDAO(): RelationDao
}