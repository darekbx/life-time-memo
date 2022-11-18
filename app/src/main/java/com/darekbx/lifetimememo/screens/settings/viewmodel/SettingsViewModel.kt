package com.darekbx.lifetimememo.screens.settings.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darekbx.lifetimememo.screens.settings.model.BackupModel
import com.darekbx.lifetimememo.screens.settings.repository.BackupRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: BackupRepository,
    private val gson: Gson
) : ViewModel() {

    fun makeBackup(context: Context) {
        viewModelScope.launch {
            repository
                .allContainers()
                .zip(repository.allMemos()) { containers, memos -> BackupModel(containers, memos) }
                .zip(repository.allCategories()) { model, categories ->
                    model.also {
                        it.categories = categories
                    }
                }
                .zip(repository.allLocations()) { model, locations ->
                    model.also {
                        it.locations = locations
                    }
                }
                .map { model -> gson.toJson(model) }
                .collect { jsonData ->

                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, jsonData)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
        }
    }
}