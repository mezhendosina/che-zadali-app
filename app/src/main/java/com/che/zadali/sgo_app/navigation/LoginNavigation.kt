package com.che.zadali.sgo_app.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgo_app.screens.Screen
import com.che.zadali.sgo_app.screens.login.ChooseSchool
import com.che.zadali.sgo_app.screens.login.Login
import com.che.zadali.sgo_app.screens.login.WelcomeScreen
import com.google.gson.Gson


fun NavGraphBuilder.loginNavigation(navController: NavController, route: String) {
    navigation(startDestination = Screen.Welcome.route, route = route) {
        composable(
            Screen.Welcome.route/*,
            enterTransition = {
                when (initialState.destination.route) {
                    Screen.ChooseSchool.route -> slideInHorizontally(
                        initialOffsetX = { 1000 },
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
            exitTransition = {
                when (initialState.destination.route) {
                    Screen.ChooseSchool.route -> slideOutHorizontally(
                        targetOffsetX = { -1000 },
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    Screen.ChooseSchool.route -> slideInHorizontally(
                        initialOffsetX = { 1000 },
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
            popExitTransition = {
                when (initialState.destination.route) {
                    Screen.ChooseSchool.route -> slideOutHorizontally(
                        targetOffsetX = { -1000 },
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            }*/
        ) { WelcomeScreen(navController = navController) }
        composable(
            "${Screen.ChooseSchool.route}/{school}",
            arguments = listOf(navArgument("school") {
                NavType.StringType; defaultValue = ""
            })
        ) { backStackEntry ->
            ChooseSchool(
                navController = navController,
                backStackEntry.arguments?.getString("school")
            )
        }
        composable(
            "${Screen.Login.route}/{schoolForLogin}",
            arguments = listOf(navArgument("schoolForLogin") {
                NavType.StringType; defaultValue = ""
            })
        ) {
            Login(
                navController = navController,
                school = Gson().fromJson(
                    it.arguments?.getString("schoolForLogin"),
                    SchoolItem::class.java
                )
            )
        }
    }
}
