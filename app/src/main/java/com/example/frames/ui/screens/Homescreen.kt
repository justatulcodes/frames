import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SimpleExoPlayer
import com.example.bitmapdemo.ui.screens.RecordingSurface
import com.example.bitmapdemo.ui.services.saveFramesAsVideoAviWithMediaRefresh
import com.example.frames.TAG
import com.example.frames.ui.screens.CaptureBitmap
import com.example.frames.ui.screens.ScreenViewModel
import com.example.frames.ui.screens.captureToBitmap
import com.example.frames.videoUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: ScreenViewModel) {
    val context = LocalContext.current
    val player = remember { SimpleExoPlayer.Builder(context).build() }
    val isStartRecording = remember { mutableStateOf(false) }
    var capturedFrames by remember { mutableStateOf(listOf<Bitmap>()) }
    val coroutineScope = rememberCoroutineScope()
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    val scope = rememberCoroutineScope()
    val boxBitmapCapture = remember { CaptureBitmap() }

    LaunchedEffect(Unit) {
        player.setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
        player.prepare()
        player.playWhenReady = true
    }

//    Scaffold (
//        topBar = {
//            TopAppBar(title = {Text("Video")})
//        }
//    ){ innerpadding->
//        val padding = innerpadding
//        Column (modifier = Modifier.padding(padding)){
//            Box (
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(400.dp)
//            ) {
//                RecordingSurface (player = player){ bitmap ->
//                    if (isStartRecording.value){
//                        viewModel.createVideoFromBitmap(bitmap)
//                    }
//
//                }
//
//            }
//
//            Row {
//                Button(
//                    onClick = {
//                        player.prepare()
//                        player.playWhenReady = true
//                    }
//                ) {
//                    Text("Prepare")
//                }
//                Button(
//                    onClick = {
//                        player.pause();
//                    }
//                ) {
//                    Text("stop")
//                }
//
//                Button(
//                    onClick = { player.play();}
//                ) {
//                    Text("play")
//                }
//            }
//            Row {
//                Button(
//                    onClick = {isStartRecording.value = true}
//                ) {Text("start recording")}
//
//                if (isStartRecording.value){
//                    Text("Recording...${capturedFrames.size}")
//                }
//                Button(
//                    onClick = {
//
//                        isStartRecording.value = false
//                        if (!isStartRecording.value){
//                            viewModel.releaseVideoWriter()
//                        }
//                    }
//                ) {
//                    Text("Stop recording")
//                }
//            }
//
//
//
//
//        }
//
//
//
//    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Video") })
        }
    ) { innerPadding ->
        val padding = innerPadding
        Column(modifier = Modifier.padding(padding)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    // Use captureMethod instead of drawWithContent
                    .onGloballyPositioned { coordinates ->
                        boxSize = coordinates.size
                    }
                    .then(
                        if (isStartRecording.value) {
                            Modifier.captureToBitmap(boxBitmapCapture) { bitmap ->
                                viewModel.createVideoFromBitmap(bitmap)
                            }
                        } else Modifier
                    )
            ) {
                // Display your recording surface here if needed
                RecordingSurface(player = player) {

                }
            }

            Row {
                Button(onClick = { player.prepare(); player.playWhenReady = true }) {
                    Text("Prepare")
                }
                Button(onClick = { player.pause() }) {
                    Text("Stop")
                }
                Button(onClick = { player.play() }) {
                    Text("Play")
                }
            }

            Row {
                Button(onClick = {
                    isStartRecording.value = true
                    scope.launch(Dispatchers.Default) {
                        while (isStartRecording.value) {
                            withContext(Dispatchers.Main) {
                                boxBitmapCapture.capture()
                            }
//                            delay(1000L / 24) // Target 24 FPS
                        }
                    }
                }) {
                    Text("Start Recording")
                }

                if (isStartRecording.value) {
                    Text("Recording...")
                }

                Button(onClick = {
                    isStartRecording.value = false
                    viewModel.releaseVideoWriter()
                }) {
                    Text("Stop Recording")
                }
            }
        }
    }



}