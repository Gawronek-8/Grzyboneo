package com.example.grzyboneo.data.repository

import com.example.grzyboneo.domain.model.Predictions

class ModelRepository {
    fun analyzeImage(image: android.graphics.Bitmap) : Predictions{
        return Predictions(
            topLabels = listOf("Boletus Edulis", "Amanita Muscaria", "Cantharell"),
            topConfidences = listOf(0.54f, 0.26f, 0.2f)
        )
    }
}