package com.che.zadali.sgo_app.ui.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.announcements.AnnouncementsData

@Composable
fun Announcements(announcementsData: AnnouncementsData) {
    var itemsCounter = 0
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            stringResource(R.string.announcements),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Medium,
        )
        for (i in announcementsData) {
            var showAll by remember { mutableStateOf(false) }

            itemsCounter += 1
            if (itemsCounter >= 2) {
                AnimatedVisibility(visible = showAll) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)

                    ) {
                        Text(
                            text = i.name,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.subtitle1,
                        )
                        Text(
                            i.description,
                            style = MaterialTheme.typography.button,
                            color = MaterialTheme.colors.primaryVariant,
                            fontWeight = FontWeight.Normal,
                            maxLines = 2,
                            softWrap = true,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }
                TextButton({ showAll = !showAll }) {
                    Text(
                        stringResource(R.string.showMore),
                        style = MaterialTheme.typography.button
                    )
                }
            } else {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = i.name,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.subtitle1,
                    )
                    Text(
                        i.description,
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.Normal,
                        maxLines = 2,
                        softWrap = true,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}