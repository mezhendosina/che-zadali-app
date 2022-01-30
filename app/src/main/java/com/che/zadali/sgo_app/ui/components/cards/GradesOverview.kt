package com.che.zadali.sgo_app.ui.components.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.che.zadali.sgo_app.R

@Composable
fun GradeOverview() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            stringResource(id = R.string.grades), fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Оценок нет, но вы держитесь",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}