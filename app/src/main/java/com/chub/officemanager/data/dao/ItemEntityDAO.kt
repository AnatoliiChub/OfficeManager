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
    @Query("SELECT * FROM ItemEntity" +
            " WHERE name LIKE '%' || :text || '%' OR description LIKE '%' || :text || '%'" +
            " OR typeId IN (SELECT typeId FROM TypeEntity WHERE name LIKE '%' || :text || '%')")
    fun search(text: String): Flow<List<ItemWithRelations>>

    @Query("SELECT * FROM ItemEntity WHERE itemId = :id")
    suspend fun getById(id: Long): ItemWithRelations

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ItemEntity): Long

    @Query("DELETE FROM ItemEntity WHERE itemId = :id")
    suspend fun delete(id: Long)
}

