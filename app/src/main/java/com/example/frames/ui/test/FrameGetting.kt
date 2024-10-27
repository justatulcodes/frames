//package com.example.frames.ui.test
//
//import android.graphics.Bitmap
//import androidx.annotation.OptIn
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.media3.common.MediaItem
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.exoplayer.SimpleExoPlayer
//import com.example.frames.videoUrl
//
//@Composable
//fun FramesGetScreen(captureBitmap: (Bitmap) -> Unit) {
//    // Define dimensions for the bitmap
//    val width = 800 // Set your desired width
//    val height = 600 // Set your desired height
//    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//
//    // Drawing content into the bitmap
//    Canvas(modifier = Modifier
//        .fillMaxSize()
//        .onDraw {
//            // Create a canvas to draw the bitmap
//            val canvas = android.graphics.Canvas(bitmap)
//            // Draw background
//            canvas.drawColor(android.graphics.Color.WHITE)
//
//            // Draw text
//            val paint = android.graphics.Paint().apply {
//                color = android.graphics.Color.BLACK
//                textSize = 48f // Set text size
//                textAlign = android.graphics.Paint.Align.CENTER
//            }
//            canvas.drawText("Captured Frame", width / 2f, 50f, paint)
//
//            // Draw boxes
//            paint.color = android.graphics.Color.RED
//            canvas.drawRect(50f, 100f, 150f, 200f, paint) // Red box
//
//            paint.color = android.graphics.Color.GREEN
//            canvas.drawRect(200f, 100f, 300f, 200f, paint) // Green box
//
//            // After drawing, call the capture callback
//            captureBitmap(bitmap.copy(Bitmap.Config.ARGB_8888, true))
//        }
//    )
//
//    // Use a Column to overlay the Composable UI elements
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Captured Frame", fontSize = 24.sp, color = Color.Black)
//        Spacer(modifier = Modifier.height(16.dp))
//        Box(modifier = Modifier
//            .size(100.dp)
//            .background(Color.Red)
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Box(modifier = Modifier
//            .size(100.dp)
//            .background(Color.Green)
//        )
//    }
//}