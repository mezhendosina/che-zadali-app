package com.che.zadali.sgo_app.screens

import androidx.annotation.StringRes
import com.che.zadali.sgo_app.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object MainScreen : Screen("mainScreen", R.string.mainTab)
    object Journal : Screen("journal", R.string.journal)
}