package com.che.zadali.sgo_app.ui.components.bars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.che.zadali.sgo_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TopBar(
    navController: NavController? = null,
    label: String,
    backIcon: Boolean = true,
    modalDrawer: Boolean = false,
    scope: CoroutineScope? = null,
    drawerState: DrawerState? = null
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        modifier = Modifier.height(56.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (backIcon) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_back),
                    "back",
                    tint = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .clickable { navController?.popBackStack() }
                )
            }
            if (modalDrawer) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "menu",
                    tint = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.clickable { scope?.launch { drawerState?.open() } })
            }
            Text(
                text = label,
                modifier = Modifier
                    .padding(start = 32.dp),
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.h6
            )
        }
    }
}
