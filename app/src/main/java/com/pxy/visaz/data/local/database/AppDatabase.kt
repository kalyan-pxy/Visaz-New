package com.pxy.visaz.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pxy.visaz.data.dtos.InspectionDto
import com.pxy.visaz.data.local.dao.InspectionDao

@Database(entities = [InspectionDto::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inspectionDao(): InspectionDao
}
