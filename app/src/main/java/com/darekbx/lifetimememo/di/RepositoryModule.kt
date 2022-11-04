package com.darekbx.lifetimememo.di

import com.darekbx.lifetimememo.data.MemoDao
import com.darekbx.lifetimememo.screens.category.repository.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideCategoryRepository(memoDao: MemoDao): CategoryRepository {
        return CategoryRepository(memoDao)
    }
}