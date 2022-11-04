package com.darekbx.lifetimememo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.darekbx.lifetimememo.data.dto.CategoryDto
import com.darekbx.lifetimememo.data.dto.ContainerDto
import com.darekbx.lifetimememo.data.dto.LocationDto
import com.darekbx.lifetimememo.data.dto.MemoDto

@Database(
    entities = [
        CategoryDto::class,
        LocationDto::class,
        MemoDto::class,
        ContainerDto::class
    ],
    exportSchema = false,
    version = 1
)
abstract class MemoDatabase : RoomDatabase() {

    abstract fun memoDao(): MemoDao

    companion object {
        val DB_NAME = "memo_db"
    }
}