package com.mezhendosina.sgo.app.ui
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.LargeTopAppBar
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.TopAppBarState
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.tooling.preview.Preview
//import com.mezhendosina.sgo.app.R
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LessonTopBar() {
//
//    Scaffold(topBar = {
//        LargeTopAppBar(
//            title = {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Image(painter = painterResource(id = R.drawable.done), contentDescription = "")
//                    Text(text = "123")
//                }
//            }, scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
//        )
//
//    }) {
//        Column(Modifier.padding(it)) {
//
//        }
//    }
//
//}
//
//@Preview
//@Composable
//fun PreviewLessonTopBar() {
//    LessonTopBar()
//}