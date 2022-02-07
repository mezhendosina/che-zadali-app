package com.che.zadali.sgo_app.screens.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.che.zadali.sgo_app.MainActivity
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.LoginData
import com.che.zadali.sgo_app.data.SettingsPrefs
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgo_app.data.sendCheckLogin
import com.che.zadali.sgo_app.screens.Screen
import com.che.zadali.sgo_app.ui.components.OnErrorDialog
import com.che.zadali.sgo_app.ui.components.bars.FAB
import com.che.zadali.sgo_app.ui.components.bars.TopBar
import com.che.zadali.sgo_app.ui.theme.SgoAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.gson.Gson


@ExperimentalAnimationApi
@Composable
fun Login(
    navController: NavController,
    Activity: AppCompatActivity,
    schoolItem: String?,
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var loginError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    var isError by remember { mutableStateOf(false) }
    var errorDescription by remember { mutableStateOf("") }

    val context = LocalContext.current
    var school: SchoolItem? = null

    LaunchedEffect(
        key1 = school,
        block = { school = Gson().fromJson(schoolItem, SchoolItem::class.java) })
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
                FAB(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            if (login.isNotEmpty() && password.isNotEmpty()) {
                                loading = true
                                val loginData = LoginData(
                                    login,
                                    password,
                                    school.schoolId,
                                    school.cityId,
                                    school.provinceId
                                )
                                val res = sendCheckLogin(loginData)
                                SettingsPrefs(context).saveAll(loginData)

                                withContext(Dispatchers.Main) {
                                    if (res.loggedIn) {
                                        startActivity(
                                            Activity,
                                            Intent(Activity, MainActivity::class.java),
                                            null
                                        )
                                        Activity.finish()

                                    } else {
                                        isError = true
                                        loading = false
                                        errorDescription = res.output
                                    }
                                }
                            } else {
                                if (login.isEmpty()) {
                                    loginError = true
                                }
                                if (password.isEmpty()) {
                                    passwordError = true
                                }
                            }
                        }
                    },
                    loading = loading,
                    clickable = login.isNotEmpty() && password.isNotEmpty()
                )
            }) {
            Column(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                Text(
                    stringResource(id = R.string.choosed_school),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primaryVariant
                )
                Card(
                    Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .clickable {
                            navController.navigate("${Screen.ChooseSchool.route}/${school.school}")
                        },
                    backgroundColor = MaterialTheme.colors.background,
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, MaterialTheme.colors.primaryVariant)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
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