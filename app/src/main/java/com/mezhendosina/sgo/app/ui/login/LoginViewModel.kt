package com.mezhendosina.sgo.app.ui.login

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.activities.MainActivity
import com.mezhendosina.sgo.app.databinding.FragmentLoginBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.app.ui.hideAnimation
import com.mezhendosina.sgo.app.ui.showAnimation
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.SettingsLoginData
import com.mezhendosina.sgo.data.layouts.Error
import com.mezhendosina.sgo.data.layouts.schools.SchoolItem
import com.mezhendosina.sgo.data.toMD5
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.util.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    fun onClickLogin(
        binding: FragmentLoginBinding,
        context: Context,
        schoolId: Int
    ) {

        showAnimation(binding.progressIndicator)

        CoroutineScope(Dispatchers.IO).launch {
//            Пригодится, когда будут другие города
//            val school = Singleton.schools.find { it.schoolId == schoolId }
            val loginEditTextValue = binding.loginTextField.editText?.text.toString()
            val login = if (loginEditTextValue.last() == ' ') loginEditTextValue.dropLast(1)
            else loginEditTextValue
            val password = binding.passwordTextField.editText?.text.toString().toMD5()
            val settingsLoginData = SettingsLoginData(
                "2",
                "1",
                "-1",
                "1",
                "2",
                "$schoolId",
                login,
                password
            )

            val settings = Settings(context)
            val singleton = Singleton
            try {
                singleton.login(settingsLoginData)
                val diaryInit = singleton.requests.diaryInit(singleton.at)
                val userId = diaryInit.currentStudentId
//                if (userId == 0) {
                settings.setCurrentUserId(userId)
//                } else{
//
//                }

                singleton.requests.logout()
                settings.saveALl(settingsLoginData)

                withContext(Dispatchers.Main) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(context, intent, null)
                }
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(context, e.response.body<Error>().message)
                }
            } catch (e: UnresolvedAddressException) {
                withContext(Dispatchers.Main) {
                    errorDialog(context, "Похоже, что нету итернета :(")
                }
            } finally {
                withContext(Dispatchers.Main) {
                    hideAnimation(binding.progressIndicator, View.GONE)
                }
            }

        }

    }

    fun findSchool(schoolId: Int): SchoolItem? {
        return Singleton.schools.find { it.schoolId == schoolId }
    }
}

