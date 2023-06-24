/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.app.ui.loginFlow.chooseSchool

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.ui.composeUi.components.Button
import com.mezhendosina.sgo.app.ui.loginFlow.login.LoginFragment
import com.mezhendosina.sgo.app.uiEntities.SchoolUiEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseSchoolScreen(viewModel: ChooseSchoolViewModel, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var showButton by remember { mutableStateOf(false) }
    var selectedSchool by remember { mutableStateOf(SchoolUiEntity(-1, "", "")) }

    val schools by viewModel.schools.observeAsState()
    val isError by viewModel.isError.observeAsState()
    val errorDescription by viewModel.errorMessage.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.ic_school),
            contentDescription = stringResource(
                id = R.string.choose_school
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .widthIn(24.dp, 148.dp)
                .heightIn(24.dp, 148.dp)
        )
        Text(
            text = stringResource(id = R.string.choose_school),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, 8.dp),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                coroutineScope.launch {
                    viewModel.findSchool(it)
                }
            },
            label = { Text(text = stringResource(id = R.string.find_school)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, 4.dp),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = { Image(painterResource(id = R.drawable.ic_search), "") }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(schools ?: emptyList()) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showButton = !showButton
                            selectedSchool = if (showButton) it else SchoolUiEntity(-1, "", "")
                        }
                        .background(if (selectedSchool.id == it.id) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                        .padding(16.dp, 12.dp)
                    ) {
                        Text(text = it.name, style = MaterialTheme.typography.bodyLarge)
                        Text(text = it.city, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            if (isError == true) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Text(text = errorDescription ?: "Что-то пошло не так")
                    Spacer(modifier = Modifier.size(12.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.findSchool(query)
                            }
                        }
                    ) {
                        Text(text = stringResource(id = R.string.retry_button))
                    }
                }
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 64.dp)
            ) {
                Button(onClick = {
                    viewModel.editSelectedItem(selectedSchool)
                    navController.navigate(
                        R.id.action_chooseSchoolFragment_to_loginFragment,
                        bundleOf(LoginFragment.ARG_SCHOOL_ID to viewModel.selectedItem.value?.id)
                    )
                }) {
                    Text(text = stringResource(id = R.string.continue_))
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_forward),
                        contentDescription = stringResource(id = R.string.continue_)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChooseSchoolScreen() {
    Mdc3Theme {
//        ChooseSchoolScreen(ChooseSchoolViewModel(NetSchoolSingleton.loginRepository), NavController)
    }
}