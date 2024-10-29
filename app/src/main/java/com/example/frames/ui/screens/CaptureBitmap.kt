package com.example.frames.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CaptureBitmap {
    private var onBitmapCaptured: (() -> Unit)? = null

    fun setCallback(callback: () -> Unit) {
        onBitmapCaptured = callback
    }

    fun capture() {
        onBitmapCaptured?.invoke()
    }
}

fun Modifier.captureToBitmap(
    capture: CaptureBitmap,
    onBitmapCaptured: (Bitmap) -> Unit
) = composed {
    val view = LocalView.current
    val scope = rememberCoroutineScope()

    DisposableEffect(view) {
        capture.setCallback {
            scope.launch(Dispatchers.Main) {
                try {
                    val bitmap = Bitmap.createBitmap(
                        view.width,
                        view.height,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    view.draw(canvas)
                    onBitmapCaptured(bitmap)
                } catch (e: Exception) {
                    Log.e("BitmapCapture", "Failed to capture bitmap: ${e.message}")
                }
            }
        }

        onDispose {
            capture.setCallback {}
        }
    }

    this
}