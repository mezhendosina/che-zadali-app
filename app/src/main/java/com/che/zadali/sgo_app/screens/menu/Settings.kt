package com.che.zadali.sgo_app.screens.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.ui.components.bars.TopBar

sealed class TextFields(val text: String, val resource: Int) {
    object Surname : TextFields("Меньшенин", R.string.surname)
    object Name : TextFields("Евгений", R.string.name)
    object Patronymic : TextFields("Андреевич", R.string.patronymic)
    object Birthday : TextFields("22.09.2005", R.string.birthday_date)
}

@Composable
fun Settings(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                label = stringResource(id = R.string.settings)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        val editableName = false //TODO
        val phoneNumber = "+79020228864" //TODO
        val eMail = "mensh.zheya@gmail.com" //TODO
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(
                    stringResource(id = R.string.profile),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                for (i in listOf(
                    TextFields.Surname,
                    TextFields.Name,
                    TextFields.Patronymic,
                    TextFields.Birthday //TODO date picker
                )) {
                    TextField(
                        labelResource = i.resource,
                        value = i.text,
                        readOnly = !editableName
                    )
                }

                TextField(
                    labelResource = R.string.phone_number,
                    value = phoneNumber,
                    readOnly = false
                )//TODO formatting phone number
                TextField(
                    labelResource = R.string.email,
                    value = eMail,
                    readOnly = false
                )//TODO formatting email

            }
            item {

                Text(
                    text = stringResource(id = R.string.security),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.change_password),
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }

                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(id = R.string.change_control_question),
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.app_settings),
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.current_year),
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { }) {

//TODO
                    }
                }
            }

        }
    }
    if (showDialog){
        AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = {
            TextButton(onClick = { /*TODO*/ }) {

            }
        })
    }
}

@Composable
private fun TextField(
    labelResource: Int,
    value: String,
    readOnly: Boolean,
) {
    var s by remember { mutableStateOf(value) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp)
    ) {
        Text(
            stringResource(id = labelResource),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = s,
            onValueChange = { s = it },
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            placeholder = { Text(text = stringResource(id = labelResource)) },
            trailingIcon = {
                if (value.isNotEmpty() && !readOnly) {
                    Icon(
                        painterResource(id = R.drawable.close_icon),
                        "",
                        modifier = Modifier.clickable { s = "" })
                }
            }
        )
    }
}
