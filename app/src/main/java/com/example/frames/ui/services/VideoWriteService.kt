package com.example.bitmapdemo.ui.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaScannerConnection
import android.util.Log
import androidx.compose.runtime.withFrameMillis
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.frames.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.videoio.VideoWriter
import java.io.File




suspend fun saveFramesAsVideoAviWithMediaRefresh(context: Context, frames: List<Bitmap>, outputFilePath: String) {
    if (frames.isEmpty()) { return }
    val finalOutputPath = outputFilePath
    val outputFile = File(finalOutputPath)
    if (!outputFile.parentFile.exists() && !outputFile.parentFile.mkdirs()) {
        Log.e(TAG, "Failed to create directory: ${outputFile.parentFile.absolutePath}")
        return
    }
    try {
        Log.d(TAG,"path is $finalOutputPath")
        val fourcc = VideoWriter.fourcc('M', 'J', 'P', 'G')
        val frameSize = org.opencv.core.Size(frames[0].width.toDouble(), frames[0].height.toDouble())
        val videoWriter = VideoWriter(finalOutputPath, fourcc, 24.0, frameSize, true)
        if (!videoWriter.isOpened) {
            Log.e(TAG, "Failed to open VideoWriter for the path: $finalOutputPath")
            return
        }
        Log.d(TAG, "VideoWriter initialized: $videoWriter")
        withContext(Dispatchers.Default) {
            for ((index, frame) in frames.withIndex()) {
                val mat = BitmapToMat(frame)
                videoWriter.write(mat)
                Log.d(TAG,"processing....$index")
            }

            Log.d(TAG,"processing completed")
        }

        videoWriter.release()
        MediaScannerConnection.scanFile(context, arrayOf(finalOutputPath), null) { path, uri ->
            Log.d(TAG, "Scanned $path with URI: $uri") }
    } catch (e: Exception) {
        Log.e(TAG, "Error while saving frames as video: ${e.message}")
        e.printStackTrace()
    }
}


fun BitmapToMat(bitmap: Bitmap): Mat {
    val mat = Mat(bitmap.height, bitmap.width, CvType.CV_8UC4)
    Utils.bitmapToMat(bitmap, mat)
    return mat
}





fun createCombinedBitmap(videoFrame: Bitmap): Bitmap {
    val combinedBitmap = Bitmap.createBitmap(videoFrame.width, videoFrame.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(combinedBitmap)
    canvas.drawBitmap(videoFrame, 0f, 0f, null)
    val paint = Paint().apply {
        color = Color.RED
        textSize = 64f
        isAntiAlias = true
    }
    canvas.drawText("Recording...", 100f, 100f, paint)
    return combinedBitmap
}