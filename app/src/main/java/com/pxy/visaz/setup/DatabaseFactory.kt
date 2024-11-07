package com.pxy.visaz.setup

import android.content.Context
import androidx.room.Room
import com.pxy.visaz.data.local.database.AppDatabase
import com.pxy.visaz.data.local.database.DatabaseConstants

class DatabaseFactory {
    fun getOrCreateDatabaseInstance(appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, DatabaseConstants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}

