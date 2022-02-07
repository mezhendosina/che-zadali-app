package com.che.zadali.sgo_app.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DrawerState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.diary.Diary
import com.che.zadali.sgo_app.data.getAnnouncements
import com.che.zadali.sgo_app.data.todayHomework
import com.che.zadali.sgo_app.ui.components.bars.TopBar
import com.che.zadali.sgo_app.ui.components.cards.Announcements
import com.che.zadali.sgo_app.ui.components.cards.DayItem
import com.che.zadali.sgo_app.ui.components.cards.GradeOverview
import kotlinx.coroutines.CoroutineScope


@Composable
fun MainScreen(
    diary: Diary,
    scope: CoroutineScope,
    drawerState: DrawerState,
    externalNavController: NavController
) {
    Scaffold(
        topBar = {
            TopBar(
                label = stringResource(R.string.mainTab),
                modalDrawer = true,
                backIcon = false,
                scope = scope,
                drawerState = drawerState
            )
        }
    ) {
        val visible by remember { mutableStateOf(false) }
        val todayLessons = todayHomework(diary)[0]
        //val todayLessons = diary.weekDays[0]
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                DayItem(todayLessons, visible, true)
            }
            item {
                Announcements(announcementsData = getAnnouncements())
            }
            item {
                GradeOverview()
            }
        }
    }
}
