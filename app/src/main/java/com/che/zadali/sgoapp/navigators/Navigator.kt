package com.che.zadali.sgoapp.navigators

import com.che.zadali.sgo_app.data.schools.SchoolItem

interface Navigator {
    fun goBack()

    fun chooseSchool(typedSchool: String? = null)

    fun login(schoolId: Int, typedSchool:String)

    fun settings()

    fun changePassword()

    fun changeControlQuestion()

    fun messageItem(messageId: Int)

    fun messages()
}