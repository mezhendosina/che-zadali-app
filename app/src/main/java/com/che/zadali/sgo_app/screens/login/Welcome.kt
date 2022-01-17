package com.che.zadali.sgo_app.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.screens.Screen
import com.che.zadali.sgo_app.ui.theme.SgoAppTheme

@Composable
fun WelcomeScreen(navController: NavController) {
    SgoAppTheme {
        Surface(Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.main_icon),
                    "mainIcon",
                    tint = Color.Unspecified
                )
                Text(
                    "Добро пожаловать в \nМобильное приложение для \nСетевого Города!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
                    fontSize = 20.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(bottom = 48.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate("${Screen.ChooseSchool.route}/EMPTY")
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onBackground
                    )
                ) {
                    Text(
                        stringResource(id = R.string.login_button),
                        color = MaterialTheme.colors.background,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}