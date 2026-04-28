package com.example.grzyboneo.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.grzyboneo.data.repository.ModelRepository
import com.example.grzyboneo.ui.screens.camera.CameraScreen
import com.example.grzyboneo.ui.screens.camera.CameraViewModel
import com.example.grzyboneo.ui.screens.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = {slideInHorizontally { -it }},
        popExitTransition = {slideOutHorizontally { it }}
    ){
        composable("home"){
            HomeScreen {navController.navigate("camera")}
        }
        composable("camera") {
            val repository = ModelRepository()
            val viewModel: CameraViewModel = viewModel(
                factory = viewModelFactory{
                    initializer { CameraViewModel(repository) }
                }
            )
            CameraScreen({navController.popBackStack()} , viewModel)
        }

    }
}