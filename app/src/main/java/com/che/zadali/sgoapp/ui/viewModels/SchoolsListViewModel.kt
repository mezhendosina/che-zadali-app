package com.che.zadali.sgoapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.che.zadali.sgo_app.data.schools.SchoolItem
import com.che.zadali.sgoapp.data.services.SchoolService
import com.che.zadali.sgoapp.data.services.SchoolsListener

class SchoolsListViewModel(private val schoolService: SchoolService) : ViewModel() {
    private var _schools = MutableLiveData<List<SchoolItem>>()
    var schools: LiveData<List<SchoolItem>> = _schools

    private var _inProgress = MutableLiveData(false)
    var inProgress: LiveData<Boolean> = _inProgress

    private val listener: SchoolsListener = {
        _schools.value = it
    }

    init {
        schoolService.addListener(listener)
        schoolService.loadSchools(_inProgress)
    }

    fun searchSchool(string: String) {
        schoolService.searchSchools(string)
    }

    override fun onCleared() {
        super.onCleared()
        schoolService.removeListener(listener)
    }
}