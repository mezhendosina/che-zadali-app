package com.che.zadali.sgo_app.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.che.zadali.sgo_app.data.diary.Diary
import com.che.zadali.sgo_app.ui.components.cards.DayItem
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.dateFormatter
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@SuppressLint("SimpleDateFormat")
@Composable
fun Journal(diary: Diary) {


    CollapsingToolbarScaffold(
        Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        state = rememberCollapsingToolbarScaffoldState(),
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbar = {
            Row(
                Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "back",
                    modifier = Modifier.clickable {
                      //  visible = !visible
                    }//TODO переход на предыдущую неделю
                )
                Text(
                    "${dateFormatter(diary.weekStart)} - ${dateFormatter(diary.weekEnd)}",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.h6
                )
                Icon(
                    painter = painterResource(id = R.drawable.back_icon),
                    contentDescription = "next",
                    modifier = Modifier.clickable {
                      //  visible = !visible
                    }//TODO переход на следующую неделю
                )
            }
        }) {
        LazyColumn( //Journal days
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            )
        ) {
            items(items = diary.weekDays) { item ->
                DayItem(
                    item = item,
                    visible = false,
                    date = true,
                    onClick = {/*TODO переход ко дню*/ })
            }
        }
    }
}





