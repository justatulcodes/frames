package com.example.frames.ui.screens


import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import com.example.frames.data.drawingData



@Composable
fun DrawingLayer() {
    val lines = remember { mutableStateListOf<drawingData>() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val line = drawingData(
                        start = change.position - dragAmount,
                        end = change.position
                    )
                    lines.add(line) } }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            lines.forEach { line ->
                drawLine(
                    color = line.color,
                    start = line.start,
                    end = line.end,
                    strokeWidth = line.strockWidth.toPx(),
                    cap = StrokeCap.Square
                )
            }
        }
    }
}

// Capture the drawing layer as a bitmap and send it through the callback
//val drawingBitmap = Bitmap.createBitmap(
//    size.width.toInt(),
//    size.height.toInt(),
//    Bitmap.Config.ARGB_8888
//)
//val bitmapCanvas = android.graphics.Canvas(drawingBitmap)
//bitmapCanvas.drawBitmap(drawingBitmap, 0f, 0f, null)
//onBitmapCaptured(drawingBitmap)


private fun captureCanvasBitmap(): Bitmap {
    val width = 1080 // Set the desired width for the bitmap
    val height = 1920 // Set the desired height for the bitmap

    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
        android.graphics.Canvas(this).apply {
            // The drawing code already applied via drawIntoCanvas will reflect here
        }
    }
}