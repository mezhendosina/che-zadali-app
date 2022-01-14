package com.che.zadali.sgo_app.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.diaryRequest
import com.che.zadali.sgo_app.screens.Screen
import com.che.zadali.sgo_app.screens.main.Journal
import com.che.zadali.sgo_app.screens.main.MainScreen
import com.che.zadali.sgo_app.ui.components.bars.ModalDrawerContent
import com.che.zadali.sgo_app.ui.components.bars.TopBar
import com.che.zadali.sgo_app.ui.theme.SgoAppTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun BottomNavigation(scope: CoroutineScope, drawerState: DrawerState) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.MainScreen,
        Screen.Journal,
    )
    SgoAppTheme() {
        ModalDrawer(
            drawerContent = { ModalDrawerContent(scope, drawerState) },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    navController.currentDestination?.route?.let { route ->
                        TopBar(
                            navController = navController,
                            label = stringResource(items.filter { it.route == route }[0].resourceId),
                            modalDrawer = true,
                            backIcon = false,
                            scope = scope,
                            drawerState = drawerState
                        )
                    }
                },
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    BottomAppBar(
                        backgroundColor = MaterialTheme.colors.background,
                        contentColor = MaterialTheme.colors.primaryVariant,
                        elevation = 2.dp
                    ) {
                        items.forEach { screen ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        painterResource(
                                            when (screen.resourceId) {
                                                R.string.mainTab -> R.drawable.main
                                                R.string.journal -> R.drawable.journal_icon
                                                else -> R.drawable.close_icon
                                            }
                                        ), contentDescription = null
                                    )
                                },
                                label = {
                                    Text(
                                        stringResource(screen.resourceId),
                                        color = MaterialTheme.colors.primaryVariant
                                    )
                                },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                val diary = diaryRequest()
                NavHost(
                    navController,
                    startDestination = Screen.MainScreen.route,
                    Modifier.padding(innerPadding)
                ) {
                    composable(Screen.MainScreen.route) { MainScreen(diary = diary) }
                    composable(Screen.Journal.route) { Journal(diary = diary) }
                }
            }
        }
    }
}