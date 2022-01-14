package com.che.zadali.sgo_app.ui.components.bars

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DrawerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ColumnScope.ModalDrawerContent(scope: CoroutineScope, drawerState: DrawerState) {
    //TODO сделать modalDrawerContent
    Button(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 16.dp),
        onClick = { scope.launch { drawerState.close() } },
        content = { Text("Close Drawer") }
    )
}