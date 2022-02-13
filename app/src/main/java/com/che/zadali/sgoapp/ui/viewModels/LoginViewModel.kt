package com.che.zadali.sgoapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgoapp.data.layout.schools.SchoolService

class LoginViewModel(private val schoolService: SchoolService) : ViewModel() {

    private val _school = MutableLiveData<SchoolItem>()
    val school: LiveData<SchoolItem> = _school

    fun loadSchool(id: Int){
        _school.value = schoolService.getBySchoolId(id)
    }

    fun onClick(){

    }
}