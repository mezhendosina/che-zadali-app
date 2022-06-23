package com.mezhendosina.sgo.app.ui.settings.changePassword

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.layouts.password.Password
import com.mezhendosina.sgo.data.toMD5
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordViewModel : ViewModel() {
    fun changePassword(oldPassword: String, newPassword: String, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val password = newPassword.toMD5()
                Singleton.requests.changePassword(
                    oldPassword.toMD5(),
                    password,
                    Settings(context).currentUserId.first()
                )
                Settings(context).changePassword(password)
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main){
                    errorDialog(context, e.response.body())
                }
            }
        }
    }


}