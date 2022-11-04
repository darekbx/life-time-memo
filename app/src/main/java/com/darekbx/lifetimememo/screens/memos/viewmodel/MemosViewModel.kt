package com.darekbx.lifetimememo.screens.memos.viewmodel

import androidx.lifecycle.*
import com.darekbx.lifetimememo.screens.memos.MemosUiState
import com.darekbx.lifetimememo.screens.memos.model.Container
import com.darekbx.lifetimememo.screens.memos.model.Memo
import com.darekbx.lifetimememo.screens.memos.repository.MemosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MemosViewModel @Inject constructor(
    private val memosRepository: MemosRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<MemosUiState>(MemosUiState.InProgress)
    val uiState: LiveData<MemosUiState>
        get() = _uiState

    fun elements(parentId: String?): LiveData<List<Any>> =
        memosRepository.elements(containerId = parentId)
            .onEach { _uiState.value = MemosUiState.Done }
            .asLiveData()

    fun add(
        title: String,
        shortInfo: String,
        description: String,
        categoryId: String,
        parentId: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = MemosUiState.InProgress
            memosRepository.add(
                Memo(
                    UUID.randomUUID().toString(),
                    parentId,
                    title,
                    System.currentTimeMillis(),
                    categoryId,
                    shortInfo,
                    description
                )
            )
        }
    }

    fun add(
        title: String,
        subtitle: String,
        parentId: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = MemosUiState.InProgress
            memosRepository.add(
                Container(
                    UUID.randomUUID().toString(),
                    parentId,
                    title,
                    subtitle
                )
            )
        }
    }

    private suspend fun withProgress(block: suspend () -> Unit) {
        _uiState.value = MemosUiState.InProgress
        block()
        _uiState.value = MemosUiState.Done
    }
}
