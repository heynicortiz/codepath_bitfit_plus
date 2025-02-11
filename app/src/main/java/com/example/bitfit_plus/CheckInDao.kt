package com.example.bitfit_plus

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Query("SELECT * FROM check_in_table")
    fun getAll(): Flow<List<CheckInEntity>>

    @Insert
    fun insertAll(checkInItems: List<CheckInEntity>)

    @Insert
    fun insert(checkInItem: CheckInEntity)

    @Query("DELETE FROM check_in_table")
    fun deleteAll()
}