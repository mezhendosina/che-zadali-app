package com.che.zadali.sgo_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.DrawerValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.che.zadali.sgo_app.navigation.BottomNavigation
import com.che.zadali.sgo_app.navigation.loginNavigation
import com.che.zadali.sgo_app.screens.Screen
import com.che.zadali.sgo_app.screens.menu.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            NavHost(navController = navController, startDestination = "loginScreen") {
                loginNavigation(navController, "loginScreen")
                navigation("mainScreen", "main") {
                    composable("mainScreen") { BottomNavigation(scope, drawerState, navController) }

                }
                composable(Screen.Profile.route) { Profile() }
                composable(Screen.Settings.route) { Settings() }
                composable(Screen.DiaryScreen.route) { DiaryScreen() }
                composable(Screen.Forum.route) { Forum() }
                composable(Screen.Messages.route) { Messages() }
                composable(Screen.Grades.route) { Grades() }

            }
        }
    }
}

