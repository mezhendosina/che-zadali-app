package com.che.zadali.sgo_app.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.che.zadali.sgo_app.ui.components.bars.TopBar

@Composable
fun ChangeControlQuestion(navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = { TopBar(label = "To be filled", navController = navController)}) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Женя плохой человек, ничего не делает")
        }
    }
}