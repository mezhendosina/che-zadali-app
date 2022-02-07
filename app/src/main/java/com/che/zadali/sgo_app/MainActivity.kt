package com.che.zadali.sgo_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.DrawerValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.che.zadali.sgo_app.data.SettingsPrefs
import com.che.zadali.sgo_app.navigation.BottomNavigation
import com.che.zadali.sgo_app.screens.Screen
import com.che.zadali.sgo_app.screens.menu.*
import com.che.zadali.sgo_app.ui.theme.SgoAppTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalAnimationApi
class MainActivity() : ComponentActivity() {
    @SuppressLint("FlowOperatorInvokedInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SgoAppTheme() {
                val navController = rememberAnimatedNavController()
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                AnimatedNavHost(navController = navController, startDestination = "mainScreen") {
                    composable(
                        "mainScreen",
                        enterTransition = {
                            when (initialState.destination.route) {
                                Screen.Login.route -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left)
                                else -> null
                            }
                        }
                    ) {
                        BottomNavigation(
                            scope, drawerState, navController,
                            SettingsPrefs(LocalContext.current).login.collectAsState(
                                initial = "Не удалось получить логин"
                            ).value
                        )
                    }
                    composable(
                        Screen.Settings.route,
                        enterTransition = {
                            when (initialState.destination.route) {
                                Screen.ChangePassword.route -> slideIntoContainer(
                                    AnimatedContentScope.SlideDirection.Right,
                                    animationSpec = tween(250, easing = LinearEasing)
                                )
                                Screen.ChangeControlQuestion.route -> slideIntoContainer(
                                    AnimatedContentScope.SlideDirection.Right,
                                    animationSpec = tween(250, easing = LinearEasing)
                                )
                                else -> slideIntoContainer(
                                    AnimatedContentScope.SlideDirection.Left,
                                    animationSpec = tween(250, easing = LinearEasing)
                                )
                            }
                        },
                        exitTransition = {
                            when (targetState.destination.route) {
                                Screen.ChangePassword.route -> null
                                Screen.ChangeControlQuestion.route -> null
                                else -> slideOutOfContainer(
                                    AnimatedContentScope.SlideDirection.Right,
                                    animationSpec = tween(250, easing = LinearEasing)
                                )
                            }

                        }
                    ) { Settings(navController, this@MainActivity) }
                    composable(
                        Screen.DiaryScreen.route,
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
                        }
                    ) { DiaryScreen() }
                    composable(
                        Screen.Forum.route,
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
                        }
                    ) { Forum() }
                    composable(
                        Screen.Messages.route,
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
                        }
                    ) { Messages() }
                    composable(
                        Screen.Grades.route,
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
                        }
                    ) { Grades() }

                    composable(
                        Screen.ChangePassword.route,
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
                        }
                    ) { ChangePassword(context = this@MainActivity, navController = navController) }
                    composable(
                        Screen.ChangeControlQuestion.route,
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
                        }
                    ) { ChangeControlQuestion(navController = navController) }
                }
            }
        }
    }
}
