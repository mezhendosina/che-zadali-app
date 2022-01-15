package com.che.zadali.sgo_app.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgo_app.ui.components.OnErrorDialog
import com.che.zadali.sgo_app.ui.components.bars.FAB
import com.che.zadali.sgo_app.ui.components.bars.TopBar
import com.che.zadali.sgo_app.ui.theme.SgoAppTheme


@Composable
fun Login(
    navController: NavController,
    school: SchoolItem,
) {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var loginError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    val loading by remember { mutableStateOf(false) }

    var isError by remember { mutableStateOf(false) }
    val errorDescription by remember { mutableStateOf("") }

    if (isError) {
        OnErrorDialog(errorDescription) { isError = false }
    }
    SgoAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    navController = navController,
                    label = stringResource(id = R.string.login_button)
                )
            },
            floatingActionButton = {
                FAB(onClick = {
                    if (login.isNotEmpty() && password.isNotEmpty()) {
                        navController.navigate("main")
                    } else {
                        if (login.isEmpty()) {
                            loginError = true
                        }
                        if (password.isEmpty()) {
                            passwordError = true
                        }
                    }
                }, loading = loading)
            }) {
            Column(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .clickable {
                            navController.navigate("chooseSchool/${school.school}")
                        },
                    backgroundColor = MaterialTheme.colors.background,
                    elevation = 2.dp,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                    ) {
                        Text(
                            "${school.school}, ${school.city}",
                            softWrap = true,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
                OutlinedTextField(
                    value = login,
                    onValueChange = {
                        login = it
                        loginError = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    label = {
                        Text(
                            stringResource(id = R.string.login)
                        )
                    },
                    isError = loginError

                )
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            stringResource(id = R.string.password),
                        )
                    },
                    isError = passwordError,
                    visualTransformation = PasswordVisualTransformation(),
                )
            }
        }
    }
}