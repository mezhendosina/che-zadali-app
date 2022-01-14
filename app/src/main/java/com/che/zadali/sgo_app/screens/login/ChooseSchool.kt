package com.che.zadali.sgo_app.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgo_app.data.schoolsRequest
import com.che.zadali.sgo_app.ui.components.bars.TopBar
import com.che.zadali.sgo_app.ui.components.lists.SchoolsListView
import com.che.zadali.sgo_app.ui.theme.SgoAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState


@Composable
fun ChooseSchool(
    navController: NavController,
    typedSchool: String?,
) {

    var userTypedSchool by remember {
        mutableStateOf(
            when (typedSchool) {
                null -> ""
                "EMPTY" -> ""
                else -> typedSchool
            }
        )
    }
    var res by remember { mutableStateOf(listOf<SchoolItem>()) }
    LaunchedEffect(key1 = res) { CoroutineScope(Dispatchers.IO).launch { res = schoolsRequest() } }
    SgoAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            CollapsingToolbarScaffold(
                modifier = Modifier.fillMaxSize(),
                state = rememberCollapsingToolbarScaffoldState(),
                scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
                toolbar = {
                    TopBar(
                        navController = navController,
                        label = stringResource(id = R.string.choose_school)
                    )
                }
            ) {
                CollapsingToolbarScaffold(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    state = rememberCollapsingToolbarScaffoldState(),
                    scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                    toolbar = {
                        OutlinedTextField(
                            value = userTypedSchool,
                            trailingIcon = {
                                if (userTypedSchool.isNotEmpty()) {
                                    Icon(
                                        painterResource(id = R.drawable.close_icon),
                                        "close_button",
                                        Modifier.clickable { userTypedSchool = "" }
                                    )
                                }
                            },
                            onValueChange = {
                                userTypedSchool = it
                            },
                            label = {
                                Text(
                                    stringResource(id = R.string.choose_school)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                ) { SchoolsListView(navController, userTypedSchool, res) }
            }
        }
    }
}
