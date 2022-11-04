package com.darekbx.lifetimememo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.darekbx.lifetimememo.data.dto.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Query("""
        SELECT 
            category.uid AS uid, 
            category.name AS name,
			(SELECT COUNT(memo.uid) FROM memo WHERE memo.category_uid = category.uid) AS count 
        FROM category
        """)
    fun categories(): Flow<List<ExtendedCategoryDto>>

    @Query("SELECT * FROM memo WHERE container_uid = :containerId")
    fun memos(containerId: String?): Flow<List<MemoDto>>

    @Query("SELECT * FROM container WHERE parent_uid IS NULL")
    fun containers(): Flow<List<ContainerDto>>

    @Query("SELECT * FROM container WHERE parent_uid = :containerId")
    fun containers(containerId: String): Flow<List<ContainerDto>>

    @Query("SELECT COUNT(uid) FROM container WHERE parent_uid = :containerId")
    suspend fun countContainers(containerId: String): Int

    @Query("SELECT COUNT(uid) FROM memo WHERE container_uid = :containerId")
    suspend fun countMemos(containerId: String): Int

    @Query("SELECT COUNT(uid) FROM location WHERE memo_id = :memoId")
    suspend fun countLocations(memoId: String): Int

    @Query("SELECT * FROM category WHERE uid = :categoryId")
    suspend fun getCategory(categoryId: String): CategoryDto

    @Insert
    suspend fun add(categoryDto: CategoryDto): Long

    @Query("DELETE FROM category WHERE uid = :uid")
    suspend fun delete(uid: String)

    @Insert
    fun add(locationDto: LocationDto): Long

    @Insert
    fun add(entryDto: MemoDto): Long

    @Insert
    fun add(containerDto: ContainerDto): Long
}