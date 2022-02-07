package com.che.zadali.sgo_app.screens.menu

import android.content.Context
import androidx.compose.material.AlertDialog
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.changePassword
import com.che.zadali.sgo_app.ui.components.bars.FAB
import com.che.zadali.sgo_app.ui.components.bars.TopBar
import kotlinx.coroutines.launch

@Composable
fun ChangePassword(context: Context, navController: NavController) {
    var loading by remember { mutableStateOf(false) }

    var newPassword by remember { mutableStateOf("") }

    var error by remember { mutableStateOf(false) }
    var textError by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopBar(
                label = stringResource(id = R.string.change_password),
                navController = navController
            )
        },
        floatingActionButton = {
            FAB(onClick = {
                scope.launch {
                    loading = true
                    val a = changePassword(newPassword, context)
                    if (a != 200) {
                        loading = false
                        error = true
                    } else {
                        navController.popBackStack()
                    }
                }
            }, loading = loading)
        }
    ) {

        if (error) {
            ErrorDialog(textError = textError) { error = false }
        }
    }
}

@Composable
private fun ErrorDialog(textError: String, onDismissRequest: () -> Unit) {
    AlertDialog(onDismissRequest = { onDismissRequest }, confirmButton = {})
}