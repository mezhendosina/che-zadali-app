package com.che.zadali.sgoapp.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.che.zadali.sgoapp.App
import com.che.zadali.sgoapp.navigators.Navigator
import com.che.zadali.sgoapp.ui.viewModels.JournalViewModel
import com.che.zadali.sgoapp.ui.viewModels.LoginViewModel
import com.che.zadali.sgoapp.ui.viewModels.MainScreenViewModel
import com.che.zadali.sgoapp.ui.viewModels.SchoolsListViewModel

class ViewModelFactory(private val app: App) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            SchoolsListViewModel::class.java -> {
                SchoolsListViewModel(app.schoolsService)
            }
            LoginViewModel::class.java -> {
                LoginViewModel(app.schoolsService)
            }
            MainScreenViewModel::class.java -> {
                MainScreenViewModel(app.diaryService)
            }
            JournalViewModel::class.java -> {
                JournalViewModel(app.diaryService)
            }
            else -> {
                throw IllegalStateException("ViewModel Error")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator