package com.example.grzyboneo.ui.screens.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.grzyboneo.ui.theme.GrzyboneoTheme
import kotlinx.coroutines.Runnable

@Composable
fun HomeScreen(onNavigateToCamera: () -> Unit) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Greeting(
            name = "Android",
            modifier = Modifier.padding(innerPadding)
        )
        CameraButton(onClick = onNavigateToCamera)
    }
}



@Composable
fun CameraButton(onClick: () -> Unit){
    FloatingActionButton(onClick = onClick, containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary) {
         Row(verticalAlignment = Alignment.CenterVertically) {
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
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GrzyboneoTheme {
        Greeting("Android")
    }
}