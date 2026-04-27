package com.example.grzyboneo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.grzyboneo.ui.screens.camera.CameraScreen
import com.example.grzyboneo.ui.screens.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = "home"){
        composable("home"){
            HomeScreen {navController.navigate("camera")}
        }
        composable("camera") {
            CameraScreen{navController.navigate("home")}
        }
    }
}