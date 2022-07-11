package com.mezhendosina.sgo.app

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.mezhendosina.sgo.app.ui.grades.GradesViewModel
import com.mezhendosina.sgo.app.ui.journal.lessonItem.LessonViewModel
import com.mezhendosina.sgo.app.ui.login.chooseSchool.ChooseSchoolViewModel
import com.mezhendosina.sgo.app.ui.container.ContainerViewModel

class ViewModelFactory(private val app: App) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            ContainerViewModel::class.java -> ContainerViewModel(app.announcementsService)

            GradesViewModel::class.java -> GradesViewModel(app.gradesService)

            LessonViewModel::class.java -> LessonViewModel(app.lessonService)

            ChooseSchoolViewModel::class.java -> ChooseSchoolViewModel(
                app.chooseSchoolService
            )
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
        return viewModel as T
    }

}


fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.findTopNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}