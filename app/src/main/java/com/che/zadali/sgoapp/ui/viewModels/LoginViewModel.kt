package com.che.zadali.sgoapp.ui.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgoapp.data.LoginData
import com.che.zadali.sgoapp.data.services.SettingsPrefs
import com.che.zadali.sgoapp.data.services.SchoolService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val schoolService: SchoolService) : ViewModel() {

    private val _school = MutableLiveData<SchoolItem>()
    val school: LiveData<SchoolItem> = _school

    fun loadSchool(id: Int) {
        _school.value = schoolService.getBySchoolId(id)
    }

    fun onClick(context: Context, loginData: LoginData): Boolean {
        //TODO
        CoroutineScope(Dispatchers.IO).launch {
            SettingsPrefs(context).saveAll(loginData)
        }
        return true
    }

}