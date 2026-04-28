package com.example.grzyboneo.ui.screens.camera

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.AndroidFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.grzyboneo.ui.screens.home.CameraButton
import com.example.grzyboneo.ui.screens.home.Greeting
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.intellij.lang.annotations.JdkConstants

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(onReturnButton: () -> Unit){
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    if (cameraPermissionState.status.isGranted) {
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview()
            CameraOverlay(onButtonClick = onReturnButton)
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
fun CameraPreview(){
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

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview
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
fun CameraOverlay(onButtonClick : () -> Unit){
    Box(modifier = Modifier.fillMaxSize()) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 3.dp.toPx()
            val boxSize = size.minDimension * 0.6f

            drawRect(
                color = Color.White.copy(alpha = 0.5f),
                topLeft = Offset(
                    x = (size.width - boxSize) / 2,
                    y = (size.height - boxSize) / 2
                ),
                size = Size(boxSize, boxSize),
                style = Stroke(
                    width = strokeWidth
                )
            )
        }

        Text(
            text = "ALIGN MUSHROOM IN FRAME",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 64.dp)
        )

        ReturnButton(
            onClick = onButtonClick,
            modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(48.dp)

        )
    }
}

@Composable
fun ReturnButton(onClick: () -> Unit, modifier: Modifier){
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        modifier=modifier,
        ) {
            Text(text = "CANCEL", style = MaterialTheme.typography.labelLarge)
    }
}