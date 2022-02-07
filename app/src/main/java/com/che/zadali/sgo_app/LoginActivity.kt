package com.che.zadali.sgo_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.che.zadali.sgo_app.data.SettingsPrefs
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgo_app.screens.Screen
import com.che.zadali.sgo_app.screens.login.ChooseSchool
import com.che.zadali.sgo_app.screens.login.Login
import com.che.zadali.sgo_app.screens.login.WelcomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.gson.Gson

@ExperimentalAnimationApi
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (SettingsPrefs(this).loggedIn.collectAsState(initial = false).value) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = navController,
                    startDestination = Screen.Welcome.route
                ) {
                    composable(
                        Screen.Welcome.route,
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(250, easing = LinearEasing)
                            )
                        },
                    ) { WelcomeScreen(navController = navController) }
                    composable(
                        "${Screen.ChooseSchool.route}/{school}",
                        enterTransition = {
                            when (initialState.destination.route) {
                                Screen.Login.route -> slideIntoContainer(
                                    AnimatedContentScope.SlideDirection.Right,
                                    animationSpec = tween(250, easing = LinearEasing)
                                )
                                Screen.Welcome.route -> slideIntoContainer(
                                    AnimatedContentScope.SlideDirection.Left,
                                    animationSpec = tween(250, easing = LinearEasing)
                                )
                                else -> null
                            }
                        },
                        arguments = listOf(navArgument("school") {
                            NavType.StringType; defaultValue = ""
                        })
                    ) { backStackEntry ->
                        ChooseSchool(navController, backStackEntry.arguments?.getString("school"))
                    }
                    composable(
                        "${Screen.Login.route}/{schoolForLogin}",
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentScope.SlideDirection.Left,
                                animationSpec = tween(250, easing = LinearEasing)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentScope.SlideDirection.Right,
                                animationSpec = tween(250, easing = LinearEasing)
                            )
                        },
                        arguments = listOf(navArgument("schoolForLogin") {
                            NavType.StringType; defaultValue = ""
                        })
                    ) {
                        Login(
                            navController = navController,
                            this@LoginActivity,
                            school = Gson().fromJson(
                                it.arguments?.getString("schoolForLogin"),
                                SchoolItem::class.java,
                            )
                        )
                    }
                }
            }
        }
    }
}