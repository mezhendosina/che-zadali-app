package com.che.zadali.sgoapp.navigators

import com.che.zadali.sgo_app.data.schools.SchoolItem

interface LoginNavigator {
    fun goBack()

    fun chooseSchool()

    fun login(schoolId: Int)
}