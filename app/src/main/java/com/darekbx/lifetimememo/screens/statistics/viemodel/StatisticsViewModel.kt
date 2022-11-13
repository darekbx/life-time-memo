package com.darekbx.lifetimememo.screens.statistics.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.darekbx.lifetimememo.screens.memos.repository.MemosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val memosRepository: MemosRepository
) : ViewModel() {

    fun countMemos(): LiveData<Int> = memosRepository.countMemos().asLiveData()

    fun countContainers(): LiveData<Int> = memosRepository.countContainers().asLiveData()

}
