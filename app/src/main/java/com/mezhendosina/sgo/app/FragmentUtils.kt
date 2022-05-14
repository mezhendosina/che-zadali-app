package com.mezhendosina.sgo.app

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mezhendosina.sgo.app.ui.journal.JournalViewModel
import com.mezhendosina.sgo.app.ui.login.chooseSchool.ChooseSchoolViewModel
import com.mezhendosina.sgo.app.ui.main.MainViewModel

class ViewModelFactory(private val app: App) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> MainViewModel(
                app.todayHomeworkService,
                app.announcementsService
            )
            JournalViewModel::class.java -> JournalViewModel(
                app.journalService
            )
            ChooseSchoolViewModel::class.java -> ChooseSchoolViewModel(
                app.chooseSchoolService
            )

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
        return viewModel as T
    }

}


fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator