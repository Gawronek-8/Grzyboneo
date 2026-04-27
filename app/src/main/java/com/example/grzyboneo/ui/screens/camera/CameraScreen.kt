package com.example.grzyboneo.ui.screens.camera

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grzyboneo.ui.screens.home.CameraButton
import com.example.grzyboneo.ui.screens.home.Greeting

@Composable
fun CameraScreen(onReturnButton: () -> Unit){
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        CameraOverlay(Modifier.fillMaxSize())
        ReturnButton(onReturnButton, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun CameraOverlay(modifier: Modifier){
    Text(text = "CameraScreen")
}

@Composable
fun ReturnButton(onClick: () -> Unit, modifier: Modifier){
    FloatingActionButton(onClick = onClick, containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary, modifier=modifier) {
            Text(text = "RETURN HOME", style = MaterialTheme.typography.labelSmall)
    }
}