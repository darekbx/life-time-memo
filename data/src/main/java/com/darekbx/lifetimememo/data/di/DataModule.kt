package com.darekbx.lifetimememo.data.di

import android.content.Context
import androidx.room.Room
import com.darekbx.lifetimememo.data.BackupDao
import com.darekbx.lifetimememo.data.MemoDao
import com.darekbx.lifetimememo.data.MemoDatabase
import com.darekbx.lifetimememo.data.SearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WorkoutsModule {

    @Provides
    fun provideMemoDao(database: MemoDatabase): MemoDao {
        return database.memoDao()
    }

    @Provides
    fun provideSearchDao(database: MemoDatabase): SearchDao {
        return database.searchDao()
    }

    @Provides
    fun provideBackupDao(database: MemoDatabase): BackupDao {
        return database.backupDao()
    }

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): MemoDatabase {
        return Room.databaseBuilder(
            appContext,
            MemoDatabase::class.java,
            MemoDatabase.DB_NAME
        ).build()
    }
}
