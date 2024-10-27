package com.example.frames.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class drawingData(
    val start:Offset,
    val end:Offset,
    val color:Color=Color.Red,
    val strockWidth: Dp =1.dp
)
