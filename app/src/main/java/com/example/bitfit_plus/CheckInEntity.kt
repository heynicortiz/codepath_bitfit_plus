package com.example.bitfit_plus

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_in_table")
data class CheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "timeOfDay") val timeOfDay: String?,
    @ColumnInfo(name = "weight") val weight: Double?,
    @ColumnInfo(name = "bodyFat") val bodyFat: Double?,
    @ColumnInfo(name = "bodyImage") val bodyImage: String?,
    @ColumnInfo(name = "date") val date: String?
)