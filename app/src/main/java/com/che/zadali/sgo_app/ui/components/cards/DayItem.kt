package com.che.zadali.sgo_app.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.dateToRussian
import com.che.zadali.sgo_app.data.diary.WeekDay
import com.che.zadali.sgo_app.ui.theme.SecondGradeFont
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

@Composable
fun DayItem(item: WeekDay, visible: Boolean, date:Boolean, onClick: () -> Unit) {
    //val diary = diaryRequest()
    //val item = diary.weekDays[0]
    Card(//Day card
        Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
            .clickable { onClick }//TODO переход ко дню
            .placeholder(
                false,
                highlight = PlaceholderHighlight.fade()
            ),
        MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp
    ) {
        Column(//Lessons column
            Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        ) {
            if(date){
                Text(//Date
                    dateToRussian(item.date, true),
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 8.dp,
                    ),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6
                )
            }
            for (i in item.lessons.sortedBy { s -> s.number }) {
                Row(//Lesson row
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp
                        ), //Modifiers
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(//Lesson number
                        i.number.toString(),
                        color = MaterialTheme.colors.primary,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Column(//Lesson\time column
                        Modifier.padding(
                            start = 16.dp,
                            bottom = 4.dp
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (i.isEaLesson) { //Check ea lesson
                                Icon(
                                    painter = painterResource(id = R.drawable.strange_puzzle_piece),
                                    contentDescription = "strange_puzzle_piece",
                                    tint = MaterialTheme.colors.primaryVariant,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                            Text(
                                when (i.subjectName.lowercase()) {
                                    "основы безопасности жизнедеятельности" -> "ОБЖ"
                                    else -> i.subjectName
                                },//check lesson name
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        Text(
                            "${i.startTime}-${i.endTime}",
                            color = MaterialTheme.colors.primary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row( //Grade row
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if(i.assignments != null){
                            for (a in i.assignments) {
                                if (a.mark != null) {
                                    if (a.mark.dutyMark) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.point),
                                            contentDescription = "point",
                                            tint = MaterialTheme.colors.onError,
                                            modifier = Modifier.size(6.dp)
                                        )
                                    }
                                    if (a.mark.mark != null) {
                                        Text(
                                            a.mark.mark.toString(),
                                            modifier = Modifier.padding(
                                                start = 4.dp
                                            ),
                                            fontSize = 24.sp,
                                            color = when (a.mark.mark) {
                                                2 -> MaterialTheme.colors.onError
                                                else -> MaterialTheme.colors.primary
                                            },
                                            fontFamily = SecondGradeFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } //Lessons generator
        }
    }
} //Days generator