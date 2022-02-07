package com.che.zadali.sgo_app.screens

import androidx.annotation.StringRes
import com.che.zadali.sgo_app.R
import com.che.zadali.sgo_app.ui.components.bars.ModalDrawerContentButtons

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object MainScreen : Screen("mainScreen", R.string.mainTab)
    object Journal : Screen("journal", R.string.journal)

    object Welcome : Screen("welcome", R.string.welcome)
    object ChooseSchool : Screen("chooseSchool", R.string.choose_school)
    object Login : Screen("login", R.string.login_button)

    object Grades : Screen(ModalDrawerContentButtons.Grades.route, R.string.assessment)
    object DiaryScreen : Screen(ModalDrawerContentButtons.DiaryScreen.route, R.string.diary)
    object Messages : Screen(ModalDrawerContentButtons.Messages.route, R.string.messages)
    object Forum : Screen(ModalDrawerContentButtons.Forum.route, R.string.forum)
    object Settings : Screen(ModalDrawerContentButtons.Settings.route, R.string.settings)

    object ChangePassword : Screen("changePassword", R.string.change_password)
    object ChangeControlQuestion : Screen("changeControlQuestion", R.string.change_control_question)
}