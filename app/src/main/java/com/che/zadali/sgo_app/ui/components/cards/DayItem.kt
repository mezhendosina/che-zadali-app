package com.che.zadali.sgo_app.ui.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.dateToRussian
import com.che.zadali.sgo_app.data.diary.WeekDay
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

@Composable
fun DayItem(item: WeekDay, visible: Boolean, today: Boolean = true, onClick: () -> Unit) {
    var expandAll by remember { mutableStateOf(false) }
    Card(
        Modifier
            .fillMaxWidth()
            .placeholder(
                visible,
                highlight = PlaceholderHighlight.fade()
            ),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.Start) {
                    Text(
                        dateToRussian(item.date, !today),
                        modifier = Modifier.padding(start = 16.dp),
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.h6
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        stringResource(id = R.string.expand_all),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { expandAll = !expandAll },
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
            for (i in item.lessons.sortedBy { s -> s.number }) {
                var expanded by remember { mutableStateOf(expandAll) }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp)
                        .clickable { expanded = !expanded },
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            i.number.toString(),
                            color = MaterialTheme.colors.primary,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Column(Modifier.padding(start = 16.dp)) {
                            Row() {
                                if (i.isEaLesson) {
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
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (i.assignments != null) {
                                for (a in i.assignments) {
                                    if (a.mark != null) {
                                        if (a.mark.dutyMark) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.point),
                                                contentDescription = "point",
                                                tint = MaterialTheme.colors.onError,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                        if (a.mark.mark != null) {
                                            Text(
                                                a.mark.mark.toString(),
                                                modifier = Modifier.padding(
                                                    start = 4.dp
                                                ),
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = when (a.mark.mark) {
                                                    2 -> MaterialTheme.colors.onError
                                                    else -> MaterialTheme.colors.primary
                                                }
                                            )
                                        }
                                    }
                                }

                                Icon(
                                    painter = when (expandAll or expanded) {
                                        false -> painterResource(id = R.drawable.expand_more)
                                        true -> painterResource(id = R.drawable.expand_less)
                                    },
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.primaryVariant,
                                    modifier = Modifier
                                        .clickable { expanded = !expanded }
                                        .padding(start = 4.dp)
                                )
                            }
                        }
                    }
                    AnimatedVisibility(visible = expanded || expandAll) {

                        Text(
                            "Д/З: ${i.assignments?.get(0)?.assignmentName}",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

/*
Column(//Lessons column
            Modifier
                .fillMaxWidth()
        ) {
            if (date) {
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
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    var expanded by remember { mutableStateOf(false) }
                    Row(//Lesson row
                        Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded }
                            .padding(16.dp, 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(//Lesson number
                            i.number.toString(),
                            color = MaterialTheme.colors.primary,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Column(
                            //Lesson\time column
                            Modifier
                                .padding(
                                    start = 16.dp,
                                    bottom = 16.dp
                                ),
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
                                /*Text(
                                    "${i.startTime}-${i.endTime}",
                                    color = MaterialTheme.colors.primary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(start = 8.dp)
                                )*/
                            }
                            if (i.assignments != null) {
                                Text(
                                    i.assignments[0].assignmentName,
                                    style = MaterialTheme.typography.subtitle1,
                                    maxLines = 1,
                                    softWrap = true,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row( //Grade row
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (i.assignments != null) {
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

                }
            } //Lessons generator
        }*/