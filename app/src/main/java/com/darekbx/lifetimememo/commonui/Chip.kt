package com.darekbx.lifetimememo.commonui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChipMark(
    modifier: Modifier = Modifier.padding(top = 8.dp),
    text: String,
    colors: List<Color>,
    icon: @Composable () -> Unit = { }
) {
    Row(
        modifier = modifier
            .padding(end = 8.dp)
            .background(colors[1], shape = RoundedCornerShape(size = 10.dp))
            .padding(start = 6.dp, end = 2.dp, top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            icon()
        }
        Text(
            text = text,
            fontSize = 12.sp,
            color = colors[0],
            modifier = Modifier.padding(start = 2.dp, end = 6.dp)
        )
    }
}