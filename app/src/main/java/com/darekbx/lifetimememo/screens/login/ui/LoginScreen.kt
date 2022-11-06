package com.darekbx.lifetimememo.screens.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darekbx.lifetimememo.commonui.theme.*

@Composable
fun LoginScreen(authorized: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { authorized() }) {
            Text(text = "Open")
        }
    }
}

//@Preview
@Composable
fun ContainerCard(
    title: String = "Sigma",
    subtitle: String = "Senior Android Developmer 2021.07 - 2023.01",
    children: List<Any> = listOf()
) {
    Card(Modifier.padding(4.dp)) {
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
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    ChipMark(modifier = Modifier, text="${children.size} entries", colors = listOf(Color.White, OnSurface.copy(alpha = 0.5F)))
                }
                Text(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp),
                    text = subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = ""
            )
        }
    }
}

//@Preview
@Composable
fun CardItem(
    withDescription: Boolean = true,
    withShortInfo: Boolean = true,
    withLocation: Boolean = true,
    withLink: Boolean = true,
    flags: List<Int> = listOf(1, 2)
) {
    Card(Modifier.padding(4.dp)) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(Modifier.padding(top = 8.dp, end = 8.dp)) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        text = "Jan Kovalsky",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(Modifier.padding(top = 4.dp)) {
                        // Important
                        if (flags.contains(1)) {
                            WarningFlag()
                        }
                        // Sticked
                        if (flags.contains(2)) {
                            StickedFlag()
                        }
                    }
                }
                Row {
                    if (withDescription) {
                        ChipMark(text = "Description", colors = listOf(DarkGreen, Green.copy(alpha = 0.4f)))
                    }
                    if (withLocation) {
                        ChipMark(text = "Location", colors = listOf(DarkOrange, Orange.copy(alpha = 0.4f)))
                    }
                    if (withLink) {
                        ChipMark(text = "Link", colors = listOf(DarkBlue, Blue.copy(alpha = 0.4f)))
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (withShortInfo) {
                    Text(
                        text = "Embedded Programmer, left Sigma",
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

@Composable
private fun ChipMark(modifier: Modifier = Modifier.padding(top = 8.dp), text: String, colors: List<Color>) {
    Text(
        text = text, fontSize = 12.sp,
        color = colors[0],
        modifier = modifier
            .padding(end = 8.dp)
            .background(
                colors[1],
                shape = RoundedCornerShape(size = 10.dp)
            )
            .padding(start = 6.dp, end = 6.dp, top = 2.dp, bottom = 2.dp)
    )
}

@Preview(showSystemUi = true)
@Composable
fun DemoItem() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        ContainerCard(children = listOf(1, 2))
        ContainerCard(title = "Mobica", subtitle = "Android developer 2018 - 2020", children = listOf(1, 2, 3, 4, 5, 6, 7, 8))
        CardItem(false, false, true, false, emptyList())
        CardItem(true, true, false, true, listOf(1))
        CardItem(false, false, false, false, emptyList())
        CardItem(false, true, false, true, listOf(2))
    }
}