package com.che.zadali.sgo_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OnErrorDialog(errorDescription: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
                    .fillMaxWidth()
            ) {
                TextButton(
                    onDismiss,
                    modifier = Modifier.padding(8.dp)
                ) { Text("Ок") }
            }
        },
        title = {
            Text(
                "Ошибка входа",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6
            )
        },
        text = { Text(errorDescription) })
}