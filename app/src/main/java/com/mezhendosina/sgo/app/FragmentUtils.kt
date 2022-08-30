package com.mezhendosina.sgo.app

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.mezhendosina.sgo.app.ui.grades.GradesViewModel
import com.mezhendosina.sgo.app.ui.lessonItem.LessonViewModel
import com.mezhendosina.sgo.app.ui.chooseSchool.ChooseSchoolViewModel
import com.mezhendosina.sgo.app.ui.container.ContainerViewModel


fun Fragment.findTopNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}