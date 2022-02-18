package com.che.zadali.sgoapp.navigators

import com.che.zadali.sgo_app.data.schools.SchoolItem

interface Navigator {
    fun goBack()

    fun chooseSchool(typedSchool: String? = null)

    fun login(schoolId: Int, typedSchool:String)

    fun journal()

    fun main()

    fun settings()

    fun forum()

    fun messages()
}