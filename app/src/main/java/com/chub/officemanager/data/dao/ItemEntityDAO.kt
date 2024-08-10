package com.chub.officemanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.chub.officemanager.data.entity.ItemEntity
import com.chub.officemanager.data.relations.ItemWithRelations
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemEntityDAO {

    @Transaction
    @Query("SELECT * FROM ItemEntity")
    fun getAll(): Flow<List<ItemWithRelations>>

    @Query("SELECT * FROM ItemEntity WHERE itemId = :id")
    suspend fun getById(id: Long): ItemWithRelations

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ItemEntity): Long

    @Query("DELETE FROM ItemEntity WHERE itemId = :id")
    fun delete(id: Long)
}

