package com.darekbx.lifetimememo.data

import androidx.room.Dao
import androidx.room.Query
import com.darekbx.lifetimememo.data.dto.CategoryDto
import com.darekbx.lifetimememo.data.dto.ContainerDto
import com.darekbx.lifetimememo.data.dto.LocationDto
import com.darekbx.lifetimememo.data.dto.MemoDto
import kotlinx.coroutines.flow.Flow

@Dao
interface BackupDao {

    @Query("SELECT * FROM memo")
    fun allMemos(): Flow<List<MemoDto>>

    @Query("SELECT * FROM container")
    fun allContainers(): Flow<List<ContainerDto>>

    @Query("SELECT * FROM category")
    fun allCategories(): Flow<List<CategoryDto>>

    @Query("SELECT * FROM location")
    fun alLocations(): Flow<List<LocationDto>>
}