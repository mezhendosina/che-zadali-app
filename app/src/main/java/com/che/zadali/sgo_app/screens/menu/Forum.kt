package com.che.zadali.sgo_app.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Forum() {

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()

    ) {
        Text(text = "Это форум... Кто-нибудь им вообще пользуется?")
    }
    Row(
        horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Никита дурачок, ничего не делает")
    }

}