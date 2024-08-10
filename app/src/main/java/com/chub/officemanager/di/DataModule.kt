package com.chub.officemanager.di

import android.content.Context
import androidx.room.Room
import com.chub.officemanager.data.OfficeDB
import com.chub.officemanager.data.OfficeItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ViewModelComponent::class)
@Module
class DataModule {

    @Provides
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
    fun provideOfficeItemRepository(database: OfficeDB): OfficeItemRepository {
        return OfficeItemRepository(database)
    }
}