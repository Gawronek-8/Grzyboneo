package com.example.grzyboneo.ui.screens.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.grzyboneo.ui.theme.GrzyboneoTheme

@Composable
fun HomeScreen(onNavigateToCamera: () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Greeting(
                name = "Grzyboneo",
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.4f))

            CameraButton(onClick = onNavigateToCamera)
        }
    }
}



@Composable
fun CameraButton(onClick: () -> Unit){

    val interactionSrc = remember { MutableInteractionSource() }

    val isPressed by interactionSrc.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        label = "scaleButton",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )

    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale),
        interactionSource = interactionSrc
    ) {
         Row(
             verticalAlignment = Alignment.CenterVertically,
             modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
         ) {
             Text(text = "SCAN MUSHROOM", style = MaterialTheme.typography.labelSmall)
             Spacer(modifier = Modifier.width(8.dp))
             Icon(
                 imageVector = Icons.Default.CameraAlt,
                 contentDescription = "Camera Icon"
             )
         }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Grass,
            contentDescription = null,
            tint = Color.Green,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Icon(
            imageVector = Icons.Default.Grass,
            contentDescription = null,
            tint = Color.Green,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GrzyboneoTheme {
        Greeting("Android")
    }
}