package com.mezhendosina.sgo.app.ui.chooseSchool

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.chooseSchool.ChooseSchoolRepository
import com.mezhendosina.sgo.app.model.chooseSchool.SchoolUiEntity
import com.mezhendosina.sgo.app.model.chooseSchool.schoolsActionListener
import com.mezhendosina.sgo.app.toDescription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChooseSchoolViewModel(
    private val schoolService: ChooseSchoolRepository = Singleton.chooseSchoolRepository
) : ViewModel() {
    private val _schools = MutableLiveData<List<SchoolUiEntity>>()
    val schools: LiveData<List<SchoolUiEntity>> = _schools

    private val actionListener: schoolsActionListener = {
        _schools.value = it
    }

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        schoolService.addListener(actionListener)
    }


    fun findSchool(string: String): List<SchoolUiEntity>? {
        return _schools.value?.filter { it.school.contains(string) }
    }

    fun loadSchools() {
        _isError.value = false
        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    if (Singleton.schools != emptyList<SchoolUiEntity>()) _schools.value =
                        Singleton.schools
                    else schoolService.loadSchools()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                    _isError.value = true
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        schoolService.removeListener(actionListener)
    }
}