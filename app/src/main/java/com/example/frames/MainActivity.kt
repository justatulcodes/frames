package com.example.frames

import HomeScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.frames.ui.screens.DrawingLayer
import com.example.frames.ui.theme.FramesTheme
import org.opencv.android.OpenCVLoader


const val TAG = "nishan"
const val videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"


class MainActivity : ComponentActivity() {
    private lateinit var audioPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var storagePermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        OpenCVLoader.initDebug();

        audioPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d(TAG, "audio permission granted")
            } else {
                Log.d(TAG, "audio permission denied")
            }
        }

        storagePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d(TAG, "storage permission granted")
            } else {
                Log.d(TAG, "storage permission denied")
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FramesTheme {

                HomeScreen()
              //  DrawingLayer()
            }
        }
    }
}
