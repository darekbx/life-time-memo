package com.darekbx.lifetimememo.screens.settings.model

import com.darekbx.lifetimememo.data.dto.CategoryDto
import com.darekbx.lifetimememo.data.dto.ContainerDto
import com.darekbx.lifetimememo.data.dto.LocationDto
import com.darekbx.lifetimememo.data.dto.MemoDto

data class BackupModel(
    val containers: List<ContainerDto>,
    val memos: List<MemoDto>,
    var categories: List<CategoryDto> = emptyList(),
    var locations: List<LocationDto> = emptyList()
)