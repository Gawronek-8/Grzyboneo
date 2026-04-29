package com.example.grzyboneo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.grzyboneo.ui.navigation.NavGraph
import com.example.grzyboneo.ui.theme.GrzyboneoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrzyboneoTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
