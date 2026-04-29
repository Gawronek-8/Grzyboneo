package com.example.grzyboneo.ui.screens.camera

import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.grzyboneo.domain.model.Predictions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onReturnButton: () -> Unit,
    viewModel: CameraViewModel = viewModel()
){
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    if (cameraPermissionState.status.isGranted) {
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview(
                onFrameCaptured  = { bitmap : Bitmap -> 
                    viewModel.onFrameCaptured(bitmap)
                }
            )
            CameraOverlay(
                predictions = viewModel.uiState,
                onButtonClick = onReturnButton
            )
        }
    }else{

        PermissionColumn(cameraPermissionState = cameraPermissionState)
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionColumn(cameraPermissionState: PermissionState){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Camera permission is required to proceed")
        Button(onClick = {cameraPermissionState.launchPermissionRequest()}) {
            Text("Grant permission")
        }
    }
}


// TODO fix the bugged out animation when canceling the recording
@Composable
fun CameraPreview(onFrameCaptured: (Bitmap) -> Unit){
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val cameraProviderState = remember { mutableStateOf<ProcessCameraProvider?>(null) }


    DisposableEffect(Unit) {
        onDispose { cameraProviderState.value?.unbindAll() }
    }

    AndroidView(factory = { ctx ->
        val previewView = PreviewView(ctx).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            cameraProviderState.value = cameraProvider

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) {
                imageProxy ->
                val bitmap = imageProxy.toBitmap()
                onFrameCaptured(bitmap)
                imageProxy.close()
            }

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Log.e("CameraPreview", "Binding failed", e)
            }
        }, ContextCompat.getMainExecutor(ctx))

        previewView
    },
        modifier = Modifier.fillMaxSize()
    )

}

@Composable
fun CameraOverlay(
    predictions: Predictions,
    onButtonClick : () -> Unit
){
    Box(modifier = Modifier.fillMaxSize()) {

//        Canvas(modifier = Modifier.fillMaxSize()) {
//            val strokeWidth = 3.dp.toPx()
//            val boxSize = size.minDimension * 0.6f
//
//            drawRect(
//                color = Color.White.copy(alpha = 0.5f),
//                topLeft = Offset(
//                    x = (size.width - boxSize) / 2,
//                    y = (size.height - boxSize) / 2
//                ),
//                size = Size(boxSize, boxSize),
//                style = Stroke(
//                    width = strokeWidth
//                )
//            )
//        }

//        Text(
//            text = "ALIGN MUSHROOM IN FRAME",
//            style = MaterialTheme.typography.labelMedium,
//            color = Color.White,
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 64.dp)
//        )

        PredictionsTable(
            predictions = predictions,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
                .padding(horizontal = 32.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(48.dp)
        ) {
            ReturnButton(
                onClick = onButtonClick,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun PredictionsTable(predictions: Predictions, modifier: Modifier = Modifier) {
    val sortedPredictions = remember(predictions) {
        predictions.topLabels.zip(predictions.topConfidences)
            .sortedByDescending { it.second }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.4f))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Label", style = MaterialTheme.typography.labelSmall, color = Color.White)
            Text("Confidence", style = MaterialTheme.typography.labelSmall, color = Color.White)
        }
        sortedPredictions.forEach { (label, confidence) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
                Text(
                    text = "%.2f%%".format(confidence * 100),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ReturnButton(onClick: () -> Unit, modifier: Modifier = Modifier){
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        modifier=modifier,
        ) {
            Text(text = "CANCEL", style = MaterialTheme.typography.labelLarge)
    }
}