package com.darekbx.lifetimememo.screens.memos.viewmodel

import android.util.Log
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

    suspend fun getPath(parentId: String?): List<String> {
        val path = memosRepository.getPath(parentId)
        return path.reversed()
    }

    fun getContainer(parentId: String?): LiveData<Container> {
        if (parentId != null && parentId.isNotBlank() && parentId != "null") {
            return memosRepository.getContainer(parentId).asLiveData()
        } else {
            return liveData { }
        }
    }

    fun elements(parentId: String?): LiveData<List<Any>> =
        memosRepository.elements(containerId = parentId)
            .onEach { _uiState.value = MemosUiState.Done }
            .asLiveData()

    fun add(
        title: String,
        shortInfo: String,
        description: String,
        categoryId: String,
        flag: Int?,
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
                    description,
                    flag = flag
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

    fun delete(container: Container) {
        viewModelScope.launch {
            memosRepository.delete(container)
        }
    }

    private suspend fun withProgress(block: suspend () -> Unit) {
        _uiState.value = MemosUiState.InProgress
        block()
        _uiState.value = MemosUiState.Done
    }
}
