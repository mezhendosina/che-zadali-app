package com.che.zadali.sgo_app.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.data.whenHoliday

@Composable
fun Holiday(holidayDay: String, onClick: () -> Unit) {
    Text(
        text = stringResource(id = R.string.when_holiday),
        modifier = Modifier.padding(start = 8.dp, top = 8.dp),
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.h6
    )
    Card(
        Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        MaterialTheme.shapes.medium,
        MaterialTheme.colors.background,
        elevation = 8.dp
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${whenHoliday(holidayDay)} дней",
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.h6
            )
        }
    }
}