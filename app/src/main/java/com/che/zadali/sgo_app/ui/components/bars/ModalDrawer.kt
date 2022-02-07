package com.che.zadali.sgo_app.ui.components.bars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.che.zadali.sgo_app.R

//TODO onClick

sealed class ModalDrawerContentButtons(val icon: Int, val string: Int, val route: String) {
    object Grades : ModalDrawerContentButtons(R.drawable.assessment, R.string.assessment, "grades")
    object DiaryScreen :
        ModalDrawerContentButtons(R.drawable.diary_icon, R.string.diary, "diaryScreen")

    object Messages :
        ModalDrawerContentButtons(R.drawable.messages_icon, R.string.messages, "messages")

    object Forum : ModalDrawerContentButtons(R.drawable.forum_icon, R.string.forum, "forum")
    object Settings :
        ModalDrawerContentButtons(R.drawable.settings_icon, R.string.settings, "settings")
}

@Composable
fun ModalDrawerContent(
    externalNavController: NavController,
    login: String?
) {
    val items = listOf(
        ModalDrawerContentButtons.Grades,
        ModalDrawerContentButtons.DiaryScreen,
        ModalDrawerContentButtons.Messages,
        ModalDrawerContentButtons.Forum,
        ModalDrawerContentButtons.Settings
    )
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Icon(
            painterResource(id = R.drawable.avatar_icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = MaterialTheme.colors.primaryVariant
        )
        //TODO profile data class
        Text(
            "Евгений Меньшенин",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 30.dp),
            maxLines = 2,
            softWrap = true,
            overflow = TextOverflow.Ellipsis
        )
        if (login != null) {
            Text(
                login,
                style = MaterialTheme.typography.caption
            )
        }
    }
    Divider(
        Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    )
    items.forEach { item ->
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()

                .clickable { externalNavController.navigate(item.route) }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primaryVariant
                )
                Text(
                    stringResource(id = item.string),
                    style = MaterialTheme.typography.button,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
