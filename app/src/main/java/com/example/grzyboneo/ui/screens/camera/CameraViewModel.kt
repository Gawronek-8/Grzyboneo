package com.example.grzyboneo.ui.screens.camera

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grzyboneo.data.repository.ModelRepository
import com.example.grzyboneo.domain.model.Predictions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraViewModel(private val repository: ModelRepository) : ViewModel() {
    var uiState by mutableStateOf(Predictions(emptyList(), emptyList()))
        private set

    private var isAnalyzing = false

    fun onFrameCaptured(bitmap: android.graphics.Bitmap) {

        if (isAnalyzing) return
        isAnalyzing = true

        viewModelScope.launch {
            val result = withContext(Dispatchers.Default){
                repository.analyzeImage(bitmap)
            }
            uiState = result
            isAnalyzing = false
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
        repository.close()
        super.onCleared()

    }

}