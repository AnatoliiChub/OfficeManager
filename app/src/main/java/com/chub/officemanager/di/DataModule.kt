package com.chub.officemanager.di

import android.content.Context
import androidx.room.Room
import com.chub.officemanager.data.OfficeDB
import com.chub.officemanager.data.dao.ItemEntityDao
import com.chub.officemanager.data.dao.RelationDao
import com.chub.officemanager.data.dao.TypeEntityDao
import com.chub.officemanager.data.repo.OfficeItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): OfficeDB {
        val db = Room.databaseBuilder(
            context,
            OfficeDB::class.java,
            "office_database"
        ).build()
        return db
    }

    @Provides
    fun provideItemEntityDAO(database: OfficeDB) = database.itemEntityDAO()

    @Provides
    fun provideRelationEntityDAO(database: OfficeDB) = database.relationDAO()

    @Provides
    fun provideTypeEntityDAO(database: OfficeDB) = database.typeEntityDAO()

    @Provides
    fun provideOfficeItemRepository(
        database: OfficeDB,
        itemEntityDao: ItemEntityDao,
        relationDao: RelationDao,
        typeEntityDao: TypeEntityDao
    ): OfficeItemRepository {
        return OfficeItemRepository(database, itemEntityDao, relationDao, typeEntityDao)
    }
}