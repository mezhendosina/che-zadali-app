package com.mezhendosina.sgo.app.ui.grades

import android.content.Context
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.model.grades.GradeActionListener
import com.mezhendosina.sgo.app.model.grades.GradesRepository
import com.mezhendosina.sgo.app.toDescription
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.grades.entities.gradeOptions.SelectTag
import com.mezhendosina.sgo.data.requests.grades.entities.GradesItem
import com.mezhendosina.sgo.data.requests.grades.entities.gradeOptions.GradeOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GradesViewModel(
    private val gradeServices: GradesRepository = Singleton.gradesRepository
) : ViewModel() {

    private val _grades = MutableLiveData<List<GradesItem>>()
    val grades: LiveData<List<GradesItem>> = _grades

    private val _terms = MutableLiveData<List<SelectTag>>()
    val terms: LiveData<List<SelectTag>> = _terms

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _gradeOptions = MutableLiveData<GradeOptions>()



    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val gradeActionListener: GradeActionListener = {
        _grades.value = it
    }


    init {
        gradeServices.addListener(gradeActionListener)

    }

    fun load(context: Context, reload: Boolean = false) {
        if (!reload && Singleton.grades.isNotEmpty()) {
            _grades.value = Singleton.grades
            _terms.value = Singleton.gradesOptions?.TERMID
            return
        }

        // start firebase performance trace
        val trace = Firebase.performance.newTrace("load_grades_trace")
        trace.start()


        _isLoading.value = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settings = Settings(context)

                // gradesOption request
                withContext(Dispatchers.Main){
                    _gradeOptions.value = gradeServices.loadGradesOptions()
                }

                // save result
                Singleton.gradesOptions = _gradeOptions.value

                // find saved termId in response
                val findId = _gradeOptions.value!!.TERMID.find {
                    it.value == settings.currentTrimId.first().toString()
                }

                // if termId not find save and set selected termId
                if (findId == null) settings.changeTRIMId(_gradeOptions.value!!.TERMID.first { it.is_selected }.value)

                withContext(Dispatchers.Main) { _terms.value = _gradeOptions.value!!.TERMID }

                loadGrades(_gradeOptions.value!!, settings.currentTrimId.first().toString())

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toDescription()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    trace.stop()
                    _isLoading.value = false
                }
            }
        }
    }

    fun reload(context: Context, termID: Int) {
        val settings = Settings(context)
        CoroutineScope(Dispatchers.IO).launch {
            _terms.value?.get(termID)?.value?.let { settings.changeTRIMId(it) }
            withContext(Dispatchers.Main) {
                settings.currentTrimId.first()?.let {
                    loadGrades(
                        _gradeOptions.value!!,
                        it
                    )
                }
            }
        }
    }

    private suspend fun loadGrades(gradesOptions: GradeOptions, termID: String) {
        gradeServices.loadGrades(gradesOptions, termID)
    }

    fun setCurrentTerm(context: Context, button: Button, termList: List<SelectTag>) {
        val settings = Settings(context)
        CoroutineScope(Dispatchers.Main).launch {
            button.text = termList.first { it.value == settings.currentTrimId.first() }.name
        }
    }


    override fun onCleared() {
        super.onCleared()

        gradeServices.removeListener(gradeActionListener)
    }
}