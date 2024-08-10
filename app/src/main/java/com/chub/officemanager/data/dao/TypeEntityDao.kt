package com.chub.officemanager.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.chub.officemanager.data.entity.TypeEntity

@Dao
interface TypeEntityDao {

    @Insert
    suspend fun insert(user: TypeEntity): Long

    @Query("SELECT * FROM TypeEntity WHERE name = :name")
    suspend fun getByName(name: String): TypeEntity?
}