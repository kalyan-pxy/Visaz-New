package com.pxy.visaz.data.dtos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pxy.visaz.data.local.database.DatabaseConstants

@Entity(tableName = DatabaseConstants.TABLE_INSPECTIONS)
data class InspectionDto(
    @PrimaryKey
    val sno: Int,
    val details: String? = null,
    val required: String? = null,
    val observation: String? = null,
    val remarks: String? = null,
    val sheetId: String? = null
)
