@file:OptIn(ExperimentalMaterial3Api::class)

package com.darekbx.lifetimememo.screens.memo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.darekbx.lifetimememo.commonui.theme.Paddings
import com.darekbx.lifetimememo.screens.category.model.Category
import com.darekbx.lifetimememo.screens.category.viewmodel.CategoryViewModel
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
    var subtitle by remember { mutableStateOf("Pid: $parentId") }
    var description by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        if (!memoId.isNullOrEmpty() && memoId != "null") {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "TODO: Not implemented!",
                color = Color.Red
            )
        }
        Column {
            TitleField(title) { title = it }
            SubtitleField(subtitle) { subtitle = it }
            DescriptionField(description) { description = it }
            CategorySelection { category -> categoryId = category.uid }
            Text(
                modifier = Modifier.padding(16.dp),
                text = "TODO: datetime, flag, reminder"
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = "TODO: Cancel/Save button in appbar??"
            )
        }
        ButtonsRow(
            onCancel = { onClose() },
            onSave = {
                memosViewModel.add(
                    title,
                    subtitle,
                    description,
                    categoryId,
                    parentId = parentId
                )
                onClose()
            }
        )
    }
}

@Composable
private fun ButtonsRow(
    onCancel: () -> Unit = { },
    onSave: () -> Unit = { }
) {
    Row(Modifier.padding(Paddings.Big)) {
        Button(modifier = Modifier
            .fillMaxWidth(0.5F),
            onClick = { onCancel() }) {
            Text(text = "Cancel")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(1F)
                .padding(start = Paddings.Big),
            onClick = { onSave() }) {
            Text(text = "Save")
        }
    }
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
private fun TitleField(value: String, onChanged: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .inputPadding()
            .fillMaxWidth(),
        value = value,
        onValueChange = { onChanged(it) },
        singleLine = true,
        label = { Text("Title") }
    )
}

private fun Modifier.inputPadding() =
    padding(top = Paddings.Default, start = Paddings.Big, end = Paddings.Big)

@Composable
fun CategorySelection(
    categoryViewModel: CategoryViewModel = hiltViewModel(),
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