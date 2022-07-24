package com.mezhendosina.sgo.app.ui.chooseSchool

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.data.layouts.responseExceptionHandler
import com.mezhendosina.sgo.data.layouts.schools.SchoolItem
import kotlinx.coroutines.*

class ChooseSchoolViewModel(private val schoolService: ChooseSchoolService) : ViewModel() {
    private val _schools = MutableLiveData<List<SchoolItem>>()
    val schools: LiveData<List<SchoolItem>> = _schools

    private val actionListener: schoolsActionListener = {
        _schools.value = it
    }


    fun findSchool(string: String): List<SchoolItem>? {
        return _schools.value?.filter { it.school.contains(string) }
    }

    fun loadSchools(context: Context, loadingState: MutableLiveData<Boolean>) {
        schoolService.addListener(actionListener)
        loadingState.value = true
        CoroutineScope(Dispatchers.IO).launch(responseExceptionHandler()) {
//            try {
                schoolService.loadSchools()
//            } catch (e: ResponseException) {
//                withContext(Dispatchers.Main) {
//                    errorDialog(context, e.message ?: "")
//                }
//            } finally {
//                withContext(Dispatchers.Main) {
//                    loadingState.value = false
//                }
//            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        schoolService.removeListener(actionListener)
    }
}