package com.darekbx.lifetimememo.screens.search.viewmodel

import androidx.lifecycle.ViewModel
import com.darekbx.lifetimememo.screens.memos.repository.MemosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val memosRepository: MemosRepository
) : ViewModel() {

}