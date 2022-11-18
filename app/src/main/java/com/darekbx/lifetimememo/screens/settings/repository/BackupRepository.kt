package com.darekbx.lifetimememo.screens.settings.repository

import com.darekbx.lifetimememo.data.BackupDao
import com.darekbx.lifetimememo.data.dto.CategoryDto
import com.darekbx.lifetimememo.data.dto.ContainerDto
import com.darekbx.lifetimememo.data.dto.LocationDto
import com.darekbx.lifetimememo.data.dto.MemoDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BackupRepository @Inject constructor(
    private val backupDao: BackupDao
) {

    fun allMemos(): Flow<List<MemoDto>> = backupDao.allMemos()

    fun allContainers(): Flow<List<ContainerDto>> = backupDao.allContainers()

    fun allCategories(): Flow<List<CategoryDto>> = backupDao.allCategories()

    fun allLocations(): Flow<List<LocationDto>> = backupDao.alLocations()
}