package com.che.zadali.sgo_app.ui.components.bars

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.che.zadali.sgo_app.R

@Composable
fun FAB(onClick: () -> Unit, loading: Boolean) {
    FloatingActionButton(
        modifier = Modifier.padding(top = 16.dp),
        elevation = FloatingActionButtonDefaults.elevation(2.dp),
        onClick = onClick,
        backgroundColor = MaterialTheme.colors.primary
    ) {
        if (loading) {
            CircularProgressIndicator(
                Modifier.size(24.dp),
                color = MaterialTheme.colors.background
            )
        } else {
            Icon(
                painterResource(id = R.drawable.done),
                "done",
                tint = MaterialTheme.colors.background
            )
        }
    }
}