package com.mezhendosina.sgo.app.ui.grades

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.databinding.FragmentGradesBinding
import com.mezhendosina.sgo.app.ui.errorDialog
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.layouts.gradeOptions.TERMID
import com.mezhendosina.sgo.data.layouts.grades.GradesItem
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GradesViewModel(private val gradeServices: GradeService) : ViewModel() {

    private val _grades = MutableLiveData<List<GradesItem>>()
    val grades: LiveData<List<GradesItem>> = _grades

    private val _terms = MutableLiveData<List<TERMID>>()
    val terms: LiveData<List<TERMID>> = _terms

    private val gradeActionListener: GradeActionListener = {
        _grades.value = it
    }


    init {
        gradeServices.addListener(gradeActionListener)

    }

    fun loadGrades(
        context: Context,
        binding: FragmentGradesBinding? = null,
        reload: Boolean = false
    ) {
        if (reload && Singleton.grades.isNotEmpty()) {
            _grades.value = Singleton.grades
            return
        }
        binding?.apply {
            gradesRecyclerView.visibility = View.GONE
            loadGradesText.visibility = View.VISIBLE
            gradesProgressBar.visibility = View.VISIBLE
        }
        CoroutineScope(Dispatchers.IO).launch {
            val settings = Settings(context)
            try {
                val gradeOptions = gradeServices.loadGradesOptions()

                val findId = gradeOptions.TERMID.find {
                    it.value == settings.currentTrimId.first().toString()
                }

                if (findId == null) settings.changeTRIMId(gradeOptions.TERMID.first { it.is_selected }.value)

                withContext(Dispatchers.Main) { _terms.value = gradeOptions.TERMID }

                gradeServices.loadGrades(gradeOptions, settings.currentTrimId.first().toString())
            } catch (e: ResponseException) {
                withContext(Dispatchers.Main) {
                    errorDialog(context, e.response.body())
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding?.apply {
                        gradesRecyclerView.visibility = View.VISIBLE
                        loadGradesText.visibility = View.GONE
                        gradesProgressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun reloadGrades(context: Context, binding: FragmentGradesBinding, id: Int) {
        val settings = Settings(context)
        CoroutineScope(Dispatchers.Main).launch {

            binding.apply {
                termSelector.text = _terms.value?.get(id)?.name

                loadGradesText.visibility = View.VISIBLE
                gradesProgressBar.visibility = View.VISIBLE
                gradesRecyclerView.visibility = View.GONE
            }
            _terms.value?.get(id)?.value?.let { settings.changeTRIMId(it) }


            withContext(Dispatchers.IO) {
                loadGrades(context, binding)
            }
        }
    }

    fun setCurrentTerm(context: Context, button: Button, termList: List<TERMID>) {
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