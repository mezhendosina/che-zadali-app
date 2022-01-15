package com.che.zadali.sgo_app.ui.components.bars

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.che.zadali.sgo_app.R
import kotlinx.coroutines.CoroutineScope

//TODO onClick
sealed class ModalDrawerContentButtons(val icon: Int, val string: Int, val onClick: () -> Unit) {
    object Profile : ModalDrawerContentButtons(R.drawable.profile_icon, R.string.profile, {})
    object Grades : ModalDrawerContentButtons(R.drawable.assessment, R.string.assessment, {})
    object DiaryScreen : ModalDrawerContentButtons(R.drawable.diary_icon, R.string.diary, {})
    object Messages : ModalDrawerContentButtons(R.drawable.messages_icon, R.string.messages, {})
    object Forum : ModalDrawerContentButtons(R.drawable.forum_icon, R.string.forum, {})
    object Settings : ModalDrawerContentButtons(R.drawable.settings_icon, R.string.settings, {})
}

@Composable
fun ColumnScope.ModalDrawerContent(scope: CoroutineScope, drawerState: DrawerState) {
    val items = listOf(
        ModalDrawerContentButtons.Profile,
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
            modifier = Modifier.padding(top = 30.dp)
        )
        Text(
            "Логин",
            style = MaterialTheme.typography.caption
        )
    }
    Divider(
        Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    )
    items.forEach { item ->
        Button(
            item.onClick,
            Modifier
                .fillMaxWidth(),
            colors = buttonColors(MaterialTheme.colors.background),
            elevation = ButtonDefaults.elevation(0.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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