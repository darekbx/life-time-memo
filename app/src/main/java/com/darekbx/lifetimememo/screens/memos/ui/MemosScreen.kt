@file:OptIn(ExperimentalFoundationApi::class)

package com.darekbx.lifetimememo.screens.memos.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.darekbx.lifetimememo.R
import com.darekbx.lifetimememo.commonui.ChipMark
import com.darekbx.lifetimememo.commonui.ProgressIndicator
import com.darekbx.lifetimememo.commonui.theme.*
import com.darekbx.lifetimememo.screens.memos.MemosUiState
import com.darekbx.lifetimememo.screens.memos.model.Container
import com.darekbx.lifetimememo.screens.memos.model.Memo
import com.darekbx.lifetimememo.screens.memos.viewmodel.MemosViewModel

@Composable
fun MemosScreen(
    memosViewModel: MemosViewModel = hiltViewModel(),
    parentId: String? = null,
    onMemoClick: (id: String?) -> Unit,
    onContainerClick: (id: String?) -> Unit,
    onAddMemoClick: (parentId: String?) -> Unit,
    onAddContainerClick: (parentId: String?) -> Unit,
) {
    val context = LocalContext.current
    val elements by memosViewModel.elements(parentId).observeAsState(emptyList())
    val uiState by memosViewModel.uiState.observeAsState()
    var path by remember { mutableStateOf<List<String>>(emptyList()) }
    var confirmationDialogShow by remember { mutableStateOf(false) }
    var memoToDelete by remember { mutableStateOf<Memo?>(null) }

    LaunchedEffect(parentId) {
        path = memosViewModel.getPath(parentId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            CurrentLocation(path)
            ElementsList(
                elements,
                onContainerClick,
                onMemoClick,
                onMemoLongPress = { memo ->
                    memoToDelete = memo
                    confirmationDialogShow = true
                },
                onContainerLongPress = { container ->
                    if (container.childrenCount > 0) {
                        cantDeleteContainerToast(context)
                    } else {
                        memosViewModel.delete(container)
                    }
                }
            )
        }

        ActionButtons(parentId, onAddMemoClick, onAddContainerClick)

        if (uiState is MemosUiState.InProgress) {
            ProgressIndicator()
        }

        if (confirmationDialogShow && memoToDelete != null) {
            DeleteConfirmationDialog(memoToDelete, memosViewModel) {
                confirmationDialogShow = false
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    memoToDelete: Memo?,
    memosViewModel: MemosViewModel,
    onDismiss: () -> Unit = { }
) {
    val message = buildAnnotatedString {
        append("Delete ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(memoToDelete!!.title)
        }
        append(" memo?")
    }
    ConfirmationDialog(
        message = message,
        confirmButtonText = "Delete",
        onDismiss = { onDismiss() },
        onConfirm = { memosViewModel.delete(memoToDelete!!) }
    )
}

@Composable
private fun ActionButtons(
    parentId: String?,
    onAddMemoClick: (parentId: String?) -> Unit,
    onAddContainerClick: (parentId: String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Text(text = "TODO: ability to edit memo")

        ExtendedFloatingActionButton(
            onClick = { onAddMemoClick(parentId) },
            icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") },
            text = { Text(text = "Memo") },
            shape = CircleShape
        )
        Spacer(modifier = Modifier.height(16.dp))
        ExtendedFloatingActionButton(
            onClick = { onAddContainerClick(parentId) },
            icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add") },
            text = { Text(text = "Container") },
            shape = CircleShape
        )
    }
}

@Composable
private fun CurrentLocation(path: List<String>) {
    Card(
        modifier = Modifier
            .padding(start = 12.dp, top = 12.dp, end = 12.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkGreen.copy(alpha = 0.6F))
    ) {
        val annotetedString = buildAnnotatedString {
            when {
                path.isEmpty() -> append("\\")
                path.size == 1 -> append("\\${path.first()}")
                else -> {
                    append("\\${path.dropLast(1).joinToString("\\")}")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                        append("\\${path.last()}")
                    }
                }
            }
        }
        Text(
            modifier = Modifier.padding(8.dp),
            text = annotetedString,
            fontSize = 13.sp
        )
    }
}

@Composable
fun ElementsList(
    elements: List<Any>,
    onContainerClick: (id: String) -> Unit = { },
    onMemoClick: (id: String) -> Unit = { },
    onMemoLongPress: (Memo) -> Unit = { },
    onContainerLongPress: (container: Container) -> Unit = { }
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        items(elements) { item ->
            when (item) {
                is Container -> ContainerCard(
                    Modifier.combinedClickable(
                        onClick = { onContainerClick(item.uid) },
                        onLongClick = { onContainerLongPress(item) }
                    ),
                    item
                )
                is Memo -> MemoCard(
                    Modifier.combinedClickable(
                        onClick = { onMemoClick(item.uid) },
                        onLongClick = { onMemoLongPress(item) }
                    ),
                    item
                )
            }
        }
    }
}

@Composable
fun ContainerCard(modifier: Modifier, container: Container) {
    Card(
        modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Secondary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .fillMaxWidth(0.9F)
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp),
                        text = container.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    ChipMark(
                        modifier = Modifier,
                        text = "${container.childrenCount} entries",
                        colors = listOf(Color.White, Color.White.copy(alpha = 0.25F))
                    )
                }
                if (container.subtitle != null && container.subtitle.isNotBlank()) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp),
                        text = container.subtitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = ""
            )
        }
    }
}

@Composable
fun MemoCard(modifier: Modifier, memo: Memo) {
    Card(
        modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Secondary)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(Modifier.padding(top = 8.dp, end = 8.dp)) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        text = memo.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row {
                        if (memo.flag == Memo.Flag.IMPORTANT.value) {
                            ImportantFlag()
                        }
                        if (memo.flag == Memo.Flag.STICKED.value) {
                            StickedFlag()
                        }
                    }
                }
                Row {
                    ChipMark(
                        text = memo.categoryName,
                        colors = listOf(Highlight, Highlight.copy(alpha = 0.1f)),
                        icon = {
                            Icon(
                                modifier = Modifier.size(12.dp),
                                painter = painterResource(id = R.drawable.ic_category),
                                contentDescription = memo.categoryName,
                                tint = Highlight
                            )
                        }
                    )

                    if (memo.hasDescription) {
                        ChipMark(
                            text = "Description",
                            colors = listOf(Green, DarkGreen.copy(alpha = 0.4f))
                        )
                    }
                    if (memo.hasLocation) {
                        ChipMark(
                            text = "Location",
                            colors = listOf(Orange, DarkOrange.copy(alpha = 0.4f))
                        )
                    }
                    if (memo.hasLink) {
                        ChipMark(
                            text = "Link",
                            colors = listOf(Blue, DarkBlue.copy(alpha = 0.4f))
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (memo.hasSubtitle) {
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = memo.subtitle!!,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun StickedFlag() {
    Icon(
        modifier = Modifier
            .padding(top = 4.dp)
            .size(16.dp),
        painter = painterResource(id = R.drawable.ic_pin),
        contentDescription = "W",
        tint = Orange
    )
}

@Composable
private fun ImportantFlag() {
    Text(
        text = "!",
        fontSize = 17.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Red
    )
}

private fun cantDeleteContainerToast(context: Context) {
    Toast.makeText(context, "Cannot delete not empty Container!", Toast.LENGTH_LONG).show()
}
