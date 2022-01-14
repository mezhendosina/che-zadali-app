package com.che.zadali.sgo_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgo_app.screens.login.ChooseSchool
import com.che.zadali.sgo_app.screens.login.Login
import com.che.zadali.sgo_app.screens.login.WelcomeScreen
import com.google.gson.Gson


fun NavGraphBuilder.loginNavigation(navController: NavController, route:String){
    navigation(startDestination = "welcome", route = route) {
        composable("welcome") { WelcomeScreen(navController = navController) }
        composable(
            "chooseSchool/{school}",
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
            "login/{schoolForLogin}",
            arguments = listOf(navArgument("schoolForLogin") { NavType.StringType;  defaultValue = "" })
        ) {
            Login(
                navController = navController,
                school = Gson().fromJson(it.arguments?.getString("schoolForLogin"), SchoolItem::class.java)
            )
        }
    }
}
