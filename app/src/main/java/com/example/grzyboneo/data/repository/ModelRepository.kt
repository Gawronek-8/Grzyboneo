package com.example.grzyboneo.data.repository

import android.content.Context
import android.util.Log
import com.example.grzyboneo.data.ml.MushroomModel
import com.example.grzyboneo.domain.model.Predictions
import org.json.JSONObject
import kotlin.math.exp

class ModelRepository(private val context: Context) {

    private val model = MushroomModel(context)

    private val labels: List<String> = loadLabels()

    fun analyzeImage(image: android.graphics.Bitmap) : Predictions{
        val rawLogits = model.predict(image)

        val logits = if (rawLogits.size > labels.size) {
            rawLogits.sliceArray(0 until labels.size)
        } else {
            rawLogits
        }

        val maxLogit = logits.maxOrNull() ?: 0f
        val expLogits = logits.map { exp(it - maxLogit) }
        val sumExp = expLogits.sum()
        val confidences = expLogits.map { (it / sumExp) }

        val resultList = confidences.mapIndexed { index, confidence ->
            labels.getOrElse(index) { "Unknown" } to confidence
        }

        val top3 = resultList.sortedByDescending { it.second }.take(3)

        return Predictions(
            topLabels = top3.map { it.first },
            topConfidences = top3.map { it.second }
        )
    }

    private fun loadLabels() : List<String> {
        return try {
            val jsonString = context.assets.open("config.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val id2label = jsonObject.getJSONObject("id2label")
            List(id2label.length()) { i -> id2label.getString(i.toString()) }
        } catch (e: Exception) {
            Log.e("ModelRepository", "Failed to load labels: ${e.message}")
            listOf("Error loading labels")
        }
    }

    fun close() {
        model.close()
    }
}
