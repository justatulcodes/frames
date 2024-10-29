package com.example.frames.ui.screens

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.VideoWriter
import java.io.File
import kotlin.concurrent.fixedRateTimer

class ScreenViewModel : ViewModel() {
    private var videoWriter: VideoWriter? = null

    val outputpath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "video${System.currentTimeMillis()}.mp4").absolutePath

    var FrameCount = 0

    fun createVideoFromBitmap(bitmap: Bitmap) {
        try {
            if (videoWriter != null && videoWriter!!.isOpened) {
                val mat = Mat(bitmap.height, bitmap.width, CvType.CV_8UC3)
                Utils.bitmapToMat(bitmap, mat)
                FrameCount++
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2BGR)
                videoWriter!!.write(mat)
                mat.release()
            } else {

                val outputFile = File(outputpath)
                outputFile.parentFile?.mkdirs()
                 val fourcc = VideoWriter.fourcc('H', '2', '6', '4')

                val adjustedWidth = bitmap.width
                val adjustedHeight = bitmap.height

                Log.d("VideoWriter", "Attempting to create writer with:" +
                        "\nPath: $outputpath" +
                        "\nFourCC: $fourcc" +
                        "\nSize: ${adjustedWidth}x${adjustedHeight}")

                videoWriter = VideoWriter()
                if (!videoWriter!!.open(outputpath, fourcc, 40.0,
                        Size(adjustedWidth.toDouble(), adjustedHeight.toDouble()), true)) {
                    Log.e("VideoWriter", "Failed to open VideoWriter with first attempt")
                }

                val mat = Mat(adjustedHeight, adjustedWidth, CvType.CV_8UC3)
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2BGR)
                videoWriter!!.write(mat)
                mat.release()
            }
        } catch (e: Exception) {
            Log.e("VideoWriter", "Error in createVideoFromBitmap: ${e.message}")
            e.printStackTrace()
        }
    }

    fun releaseVideoWriter() {
        try {
            videoWriter?.apply {
                release()
                Log.d("VideoWriter", "Frame COunt = ${FrameCount}}")
                videoWriter = null
            }
        } catch (e: Exception) {
            Log.e("VideoWriter", "Error releasing writer: ${e.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        releaseVideoWriter()
    }
}