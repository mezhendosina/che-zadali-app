package com.mezhendosina.sgo.app.ui.login

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.activities.MainActivity
import com.mezhendosina.sgo.app.model.login.LoginRepository
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.layouts.schools.SchoolItem
import com.mezhendosina.sgo.data.toMD5
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val loginRepository: LoginRepository = Singleton.loginRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(context: Context, schoolId: Int, login: String, password: String) {
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val passwordHash = password.toMD5()
                loginRepository.login(context, schoolId, login, passwordHash)
                loginRepository.logout()
                withContext(Dispatchers.Main) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(context, intent, null)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    fun findSchool(schoolId: Int): SchoolItem? {
        return Singleton.schools.find { it.schoolId == schoolId }
    }
}

