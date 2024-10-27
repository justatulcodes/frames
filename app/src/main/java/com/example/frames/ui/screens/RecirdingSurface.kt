package com.example.bitmapdemo.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SimpleExoPlayer
import com.example.frames.ui.screens.DrawingLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(UnstableApi::class)
@Composable
fun RecordingSurface(player:SimpleExoPlayer,onFrameCaptured: (Bitmap) -> Unit){
    val context = LocalContext.current
    val textureView = remember { TextureView(context) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            while (true) {
                val videoBitmap = textureView.bitmap
                if (videoBitmap != null) {
//                    withContext(Dispatchers.Main) {
                        withFrameMillis { onFrameCaptured(videoBitmap)
//                    }
                    }
                }
            }
        }
    }

    AndroidView(factory = { textureView }) { view ->
         player.setVideoTextureView(view)
    }

    DrawingLayer()
}


//var
//var drawingBitmap by remember { mutableStateOf<Bitmap?>(null) }
//val coroutineScope = rememberCoroutineScope()

//with
//val combinedBitmap = if (drawingBitmap != null) {
//    combineBitmaps(videoBitmap, drawingBitmap!!)
//} else {
//    videoBitmap
//}
//
//withContext(Dispatchers.Main) {
//    withFrameMillis { onFrameCaptured(combinedBitmap)
//    }
//}

//DrawingLayer(onBitmapCaptured = { bitmap ->
//    coroutineScope.launch(Dispatchers.Default) {
//        drawingBitmap = bitmap
//    }
//})






