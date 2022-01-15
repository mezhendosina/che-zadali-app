package com.che.zadali.sgo_app.ui.components.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.google.gson.Gson


@Composable
fun SchoolsListView(navController: NavController, userTypedSchool: String, schools: List<SchoolItem>) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(
            items = if (userTypedSchool != "") {
                schools.filter { it.school.contains(userTypedSchool) }
            } else {
                listOf()
            }
        ) { i ->
            Card(
                shape = MaterialTheme.shapes.medium,
                elevation = 2.dp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .clickable {
                        val a = Gson().toJson(i)
                        navController.navigate("login/$a")
                    }
            ) {
                Row(
                    Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = i.school,
                            modifier = Modifier.padding(bottom = 2.dp),
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.subtitle1
                        )
                        Text(
                            i.city,
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    }
}