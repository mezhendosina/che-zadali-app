package com.mezhendosina.sgo.app.ui.login

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.activities.MainActivity
import com.mezhendosina.sgo.app.databinding.LoginFragmentBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.ErrorResponse
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.SettingsLoginData
import com.mezhendosina.sgo.data.layouts.schools.SchoolItem
import com.mezhendosina.sgo.data.toMD5
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    fun onClickLogin(
        binding: LoginFragmentBinding,
        context: Context,
        activity: FragmentActivity,
        schoolId: Int
    ) {

        showAnimation(binding.progressIndicator)

        CoroutineScope(Dispatchers.IO).launch {
//            Пригодится, когда будут другие города
//            val school = Singleton.schools.find { it.schoolId == schoolId }

            val password = binding.passwordTextField.editText?.text.toString().toMD5()
            val settingsLoginData = SettingsLoginData(
                "2",
                "1",
                "-1",
                "1",
                "2",
                "$schoolId",
                binding.loginTextField.editText?.text.toString(),
                password
            )
            try {
                val settings = Settings(context)
                val singleton = Singleton

                singleton.login(settingsLoginData)
                val diaryInit = singleton.requests.diaryInit(singleton.at)
                val userId = diaryInit.currentStudentId
//                if (userId == 0) {
                settings.setCurrentUserId(diaryInit.students[0].studentId)
//                } else{
//
//                }

                singleton.requests.logout()
                settings.saveALl(settingsLoginData)

                withContext(Dispatchers.Main) {
                    startActivity(context, Intent(context, MainActivity::class.java), null)
                    activity.finish()
                }

            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(context, e.response.body<ErrorResponse>().message)
                    hideAnimation(binding.progressIndicator, View.GONE)

                }
            }

        }

    }

    fun findSchool(schoolId: Int): SchoolItem? {
        return Singleton.schools.find { it.schoolId == schoolId }
    }
}

