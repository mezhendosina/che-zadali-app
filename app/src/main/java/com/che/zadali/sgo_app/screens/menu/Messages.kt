package com.che.zadali.sgo_app.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.che.zadali.sgo_app.ui.components.bars.ModalDrawerContentButtons

@Composable
fun Messages(){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
       Text(text = "Видишь сообщения?\nИ я не вижу, а они есть!")
    }
}