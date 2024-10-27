import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SimpleExoPlayer
import com.example.bitmapdemo.ui.screens.RecordingSurface
import com.example.bitmapdemo.ui.services.createCombinedBitmap
import com.example.bitmapdemo.ui.services.saveFramesAsVideoAviWithMediaRefresh
import com.example.bitmapdemo.ui.surfaces.ExoPlayerWithMediaRetrivelView
import com.example.frames.TAG
import com.example.frames.ui.screens.DrawingLayer
import com.example.frames.videoUrl
import kotlinx.coroutines.launch
import java.io.File


@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    val context = LocalContext.current
    val player = remember { SimpleExoPlayer.Builder(context).build() }
    val isStartRecording = remember { mutableStateOf(false) }
    var capturedFrames by remember { mutableStateOf(listOf<Bitmap>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        player.setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
        player.prepare()
        player.playWhenReady = true
    }

    Scaffold (
        topBar = {
            TopAppBar(title = {Text("Video")})
        }
    ){ innerpadding->
        val padding = innerpadding
        Column (modifier = Modifier.padding(padding)){
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                RecordingSurface (player = player){ bitmap ->
                    if (isStartRecording.value){
                        capturedFrames = capturedFrames+bitmap
                        Log.d(TAG, "bitmap: ${capturedFrames.size}")
                    }

                }

            }

            Row {
                Button(
                    onClick = {
                        player.prepare()
                        player.playWhenReady = true
                    }
                ) {
                    Text("Prepare")
                }
                Button(
                    onClick = {
                        player.pause();
                    }
                ) {
                    Text("stop")
                }

                Button(
                    onClick = { player.play();}
                ) {
                    Text("play")
                }
            }
            Row {
                Button(
                    onClick = {isStartRecording.value = true}
                ) {Text("start recording")}

                if (isStartRecording.value){
                    Text("Recording...${capturedFrames.size}")
                }
                Button(
                    onClick = {
                        val outputpath = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "00sc.avi"
                        ).absolutePath

                        isStartRecording.value = false
                        if (!isStartRecording.value){
                            coroutineScope.launch {
                                saveFramesAsVideoAviWithMediaRefresh(context,capturedFrames, outputFilePath =outputpath )
                            }

                        }
                    }
                ) {
                    Text("Stop recording")
                }
            }




        }



    }



}