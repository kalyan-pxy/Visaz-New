package com.pxy.visaz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pxy.visaz.data.dtos.InspectionDto
import com.pxy.visaz.data.local.database.DatabaseConstants


@Dao
interface InspectionDao {

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_INSPECTIONS} order by sno")
    suspend fun getAllInspections(): List<InspectionDto>?

    @Query("DELETE FROM ${DatabaseConstants.TABLE_INSPECTIONS}")
    suspend fun clearInspectionsTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInspections(inspectionDtos: List<InspectionDto>): List<Long>

    @Update
    suspend fun updateInspection(inspectionDtos: InspectionDto): Int

}