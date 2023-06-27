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

package com.mezhendosina.sgo.app.ui.announcementsFlow.announcements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.ui.announcementsFlow.announcementsBottomSheet.BottomSheetAnnouncementsViewModel
import com.mezhendosina.sgo.app.ui.composeUi.components.EmptyState
import org.jsoup.Jsoup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementsScreen(
    announcementsViewModel: BottomSheetAnnouncementsViewModel,
    navController: NavController
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val announcements by announcementsViewModel.announcements.observeAsState()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.announcements)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Rounded.ArrowBack,
                            contentDescription = "Назад",
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        if (announcements != null) {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(announcements!!) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                R.id.action_announcementsFragment2_to_announcementsFragment,
                                bundleOf(Singleton.ANNOUNCEMENTS_ID to it.id)
                            )
                        }
                        .padding(20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        val a = Jsoup.parse(it.description).text()
                        Text(
                            text = Jsoup.parse(a).text(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            softWrap = true
                        )
                    }
                }
            }
        } else {
            EmptyState(null)
        }
    }
}

@Preview
@Composable
fun PreviewAnnouncementsScreen() {
//    AnnouncementsScreen(BottomSheetAnnouncementsViewModel(), NavController()
}