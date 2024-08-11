package com.chub.officemanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chub.officemanager.data.entity.RelationEntity

@Dao
interface RelationDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(relationEntities: List<RelationEntity>)

    @Query("DELETE FROM RelationEntity WHERE parentId = :id")
    fun delete(id: Long)
}