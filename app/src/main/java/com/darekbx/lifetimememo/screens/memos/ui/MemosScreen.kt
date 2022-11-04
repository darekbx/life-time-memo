package com.darekbx.lifetimememo.screens.memos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    val elements by memosViewModel.elements(parentId).observeAsState()
    val uiState by memosViewModel.uiState.observeAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        elements?.let {
            ElementsList(it, onContainerClick, onMemoClick)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            ExtendedFloatingActionButton(
                onClick = { onAddMemoClick(parentId) },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add memo") },
                text = { Text(text = "Memo") },
                shape = CircleShape
            )
            Spacer(modifier = Modifier.height(16.dp))
            ExtendedFloatingActionButton(
                onClick = { onAddContainerClick(parentId) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add container"
                    )
                },
                text = { Text(text = "Container") },
                shape = CircleShape
            )
        }

        if (uiState is MemosUiState.InProgress) {
            ProgressIndicator()
        }
    }
}

@Composable
fun ElementsList(
    elements: List<Any>,
    onContainerClick: (id: String?) -> Unit = { },
    onMemoClick: (id: String?) -> Unit = { }
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        items(elements) { item ->
            when (item) {
                is Container -> ContainerCard(
                    Modifier.clickable { onContainerClick(item.uid) },
                    item
                )
                is Memo -> MemoCard(Modifier.clickable { onMemoClick(item.uid) }, item)
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
                if (container.subtitle != null) {
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
                    Row(Modifier.padding(top = 4.dp)) {
                        if (memo.flag == Memo.Flag.IMPORTANT.value) {
                            WarningFlag()
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
                    .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (memo.hasSubtitle) {
                    Text(
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
        modifier = Modifier.size(14.dp),
        imageVector = Icons.Outlined.Star,
        contentDescription = "W"
    )
}

@Composable
private fun WarningFlag() {
    Icon(
        modifier = Modifier.size(14.dp),
        imageVector = Icons.Outlined.Warning,
        contentDescription = "W"
    )
}
