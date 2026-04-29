package com.example.grzyboneo.data.ml

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.scale
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MushroomModel(private val context: Context) {

    @Volatile private var isClosed = false
    private val env: OrtEnvironment = OrtEnvironment.getEnvironment()
    private val session: OrtSession
    private val lock = Any()

    init {
        val modelFile = java.io.File(context.filesDir, "model.onnx")
        val dataFile = java.io.File(context.filesDir, "model.onnx_data")

        context.assets.open("model.onnx").use { input ->
            modelFile.outputStream().use { output -> input.copyTo(output) }
        }

        context.assets.open("model.onnx_data").use { input ->
            dataFile.outputStream().use { output -> input.copyTo(output) }
        }

        session = env.createSession(modelFile.absolutePath)
    }

    fun predict(image: Bitmap): FloatArray = synchronized(lock) {
        if (isClosed) return FloatArray(0)

        val resizedImage = image.scale(288, 288)

        val startX = (288 - 256) / 2
        val startY = (288 - 256) / 2
        val croppedImage = Bitmap.createBitmap(resizedImage, startX, startY, 256, 256)


        val imgData = ByteBuffer.allocateDirect(2 * 3 * 256 * 256 * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        imgData.rewind()

        val pixels = IntArray(256 * 256)
        croppedImage.getPixels(pixels, 0, 256, 0, 0, 256, 256)

        for (i in 0 until 256 * 256) {
            val p = pixels[i]
            val r = (((p shr 16) and 0xFF) / 255f )
            val g = (((p shr 8) and 0xFF) / 255f )
            val b = ((p and 0xFF) / 255f )

            imgData.put(i, r)
            imgData.put(i + 256 * 256, g)
            imgData.put(i + 2 * 256 * 256, b)
        }

        val inputName = session.inputNames.iterator().next()
        val shape = longArrayOf(2, 3, 256, 256)
        val inputTensor = OnnxTensor.createTensor(env, imgData, shape)

        inputTensor.use {
            val results = session.run(mapOf(inputName to inputTensor))
            results.use {
                @Suppress("UNCHECKED_CAST")
                val output = results.get(0).value as Array<FloatArray>
                Log.d("MushroomModel", "Full model output: ${output.contentDeepToString()}")
                return output[0]
            }
        }
    }

    fun close() = synchronized(lock) {
        if (!isClosed) {
            isClosed = true
            session.close()
            env.close()
        }
    }
}
