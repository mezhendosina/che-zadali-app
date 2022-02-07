package com.che.zadali.sgo_app.ui.components.bars

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.che.zadali.sgo_app.R

@Composable
fun FAB(onClick: () -> Unit, loading: Boolean, clickable: Boolean = true) {
    val background by animateColorAsState(if (clickable) MaterialTheme.colors.primary else MaterialTheme.colors.error)
    FloatingActionButton(
        modifier = Modifier.padding(top = 16.dp),
        elevation = FloatingActionButtonDefaults.elevation(2.dp),
        onClick = onClick,
        backgroundColor = background
    )
    {
        when (loading) {
            true -> CircularProgressIndicator(
                Modifier.size(24.dp),
                color = MaterialTheme.colors.background
            )
            false -> Icon(
                painterResource(id = R.drawable.done),
                "done",
                tint = MaterialTheme.colors.background
            )
        }
    }
}