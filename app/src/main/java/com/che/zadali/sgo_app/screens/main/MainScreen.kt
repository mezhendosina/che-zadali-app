package com.che.zadali.sgo_app.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.dateToRussian
import com.che.zadali.sgo_app.data.diary.Diary
import com.che.zadali.sgo_app.data.getAnnouncements
import com.che.zadali.sgo_app.data.todayHomework
import com.che.zadali.sgo_app.ui.components.cards.Announcements
import com.che.zadali.sgo_app.ui.components.cards.DayItem
import com.che.zadali.sgo_app.ui.components.cards.GradeOverview
import com.che.zadali.sgo_app.ui.components.cards.Holiday


@Composable
fun MainScreen(
    diary: Diary
) {
    val visible by remember { mutableStateOf(false) }
    //val todayLessons = todayHomework(diary)[0]
    val todayLessons = diary.weekDays[0]
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            //TODO проверка HOLIDAY_VIEW
            Column(Modifier.fillMaxWidth()) {
                Holiday(holidayDay = "2022-03-22T00:00:00") {}//TODO onClick
                Text(
                    text = "${stringResource(id = R.string.today)} ${dateToRussian(todayLessons.date, false)}",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.h6
                )
                Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    DayItem(todayLessons, visible, false) { }//TODO onClick
                }
                Announcements(announcementsData = getAnnouncements())
                GradeOverview()
            }
        }
    }
}