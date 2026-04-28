package com.example.grzyboneo.domain.model

data class Predictions(
    val topLabels: List<String>,
    val topConfidences: List<Float>
)

