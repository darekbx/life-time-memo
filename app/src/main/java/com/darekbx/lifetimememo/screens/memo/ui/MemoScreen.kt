@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.darekbx.lifetimememo.screens.memo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.darekbx.lifetimememo.commonui.CancelIcon
import com.darekbx.lifetimememo.commonui.SaveIcon
import com.darekbx.lifetimememo.commonui.theme.Paddings
import com.darekbx.lifetimememo.screens.category.model.Category
import com.darekbx.lifetimememo.screens.category.viewmodel.CategoryViewModel
import com.darekbx.lifetimememo.screens.memos.model.Memo
import com.darekbx.lifetimememo.screens.memos.viewmodel.MemosViewModel

/**
 * @param memoId Id of the memo which should be loaded into view
 * @param parentId Id of parent container to which memo should be added
 */
@Composable
fun MemoScreen(
    memosViewModel: MemosViewModel = hiltViewModel(),
    memoId: String? = null,
    parentId: String? = null,
    onClose: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var titleValid by remember { mutableStateOf(true) }
    var subtitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }
    var categoryValid by remember { mutableStateOf(true) }
    var important by remember { mutableStateOf(false) }
    var sticked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Container") },
                actions = {
                    IconButton(onClick = { onClose() }) { CancelIcon() }
                    IconButton(onClick = {
                        titleValid = true
                        categoryValid = true
                        if (title.isBlank()) {
                            titleValid = false
                        } else if (categoryId.isEmpty()) {
                            categoryValid = false
                        } else {
                            memosViewModel.add(
                                title,
                                subtitle,
                                description,
                                categoryId,
                                computeFlag(important, sticked),
                                parentId = parentId
                            )
                            onClose()
                        }
                    }) { SaveIcon() }
                }
            )
        },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(top = 8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                TitleField(title, titleValid) { title = it }
                SubtitleField(subtitle) { subtitle = it }
                DescriptionField(description) { description = it }
                CategorySelection(categoryValid = categoryValid) { category ->
                    categoryId = category.uid
                }
                FlagsRow(
                    important,
                    sticked,
                    importantChanged = {
                        important = it
                        if (important) {
                            sticked = false
                        }
                    },
                    stickedChanged = {
                        sticked = it
                        if (sticked) {
                            important = false
                        }
                    }
                )

                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "TODO: location, datetime, reminder"
                )
            }
        }
    )
}

private fun computeFlag(important: Boolean, sticked: Boolean): Int? {
    return when {
        important -> Memo.Flag.IMPORTANT.value
        sticked -> Memo.Flag.STICKED.value
        else -> null
    }
}

@Composable
private fun FlagsRow(
    isImportant: Boolean,
    isSticked: Boolean,
    importantChanged: (Boolean) -> Unit,
    stickedChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(start = 4.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(checked = isImportant, onCheckedChange = importantChanged)
            Text(text = "Important")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(checked = isSticked, onCheckedChange = stickedChanged)
            Text(text = "Sticked")
        }
    }
}

@Composable
private fun TitleField(
    value: String,
    isValid: Boolean,
    onChanged: (String) -> Unit
) {
    TextField(
        modifier = Modifier
            .inputPadding()
            .fillMaxWidth(),
        value = value,
        isError = !isValid,
        onValueChange = { onChanged(it) },
        singleLine = true,
        label = { Text("Title") }
    )
}

@Composable
private fun SubtitleField(value: String, onChanged: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .inputPadding()
            .fillMaxWidth()
            .height(100.dp),
        value = value,
        onValueChange = { onChanged(it) },
        singleLine = false,
        maxLines = 3,
        label = { Text("Short info") }
    )
}

@Composable
private fun DescriptionField(value: String, onChanged: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .inputPadding()
            .fillMaxWidth()
            .height(150.dp),
        value = value,
        onValueChange = { onChanged(it) },
        singleLine = false,
        maxLines = 6,
        label = { Text("Description") }
    )
}

private fun Modifier.inputPadding() =
    padding(top = Paddings.Default, start = Paddings.Big, end = Paddings.Big)

@Composable
fun CategorySelection(
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    categoryValid: Boolean,
    selectedCategory: (Category) -> Unit = { }
) {
    val categories by categoryViewModel.categories.observeAsState()
    val text = remember { mutableStateOf("") }
    val isOpen = remember { mutableStateOf(false) }
    categories?.let { categoryList ->
        Box(Modifier.inputPadding()) {
            Column {
                OutlinedTextField(
                    value = text.value,
                    isError = !categoryValid,
                    onValueChange = { text.value = it },
                    label = { Text(text = "Category") },
                    modifier = Modifier.fillMaxWidth()
                )
                DropDownList(
                    requestToOpen = isOpen.value,
                    list = categoryList,
                    request = { isOpen.value = it },
                    selectedCategory = {
                        text.value = it.name
                        selectedCategory(it)
                    }
                )
            }
            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .padding(10.dp)
                    .clickable(
                        onClick = { isOpen.value = true }
                    )
            )
        }
    }
}

@Composable
fun DropDownList(
    requestToOpen: Boolean = false,
    list: List<Category>,
    request: (Boolean) -> Unit,
    selectedCategory: (Category) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(),
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        list.forEach {
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    selectedCategory(it)
                },
                text = { Text(it.name) }
            )
        }
    }
}