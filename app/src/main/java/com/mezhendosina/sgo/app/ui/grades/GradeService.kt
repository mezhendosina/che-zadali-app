package com.mezhendosina.sgo.app.ui.grades

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.Singleton.at
import com.mezhendosina.sgo.data.GradesFromHtml
import com.mezhendosina.sgo.data.layouts.gradeOptions.GradeOptions
import com.mezhendosina.sgo.data.layouts.grades.GradesItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias GradeActionListener = (grade: List<GradesItem>) -> Unit

class GradeService {

    private var grades = mutableListOf<GradesItem>()

    private val listeners = mutableSetOf<GradeActionListener>()

    suspend fun loadGradesOptions(): GradeOptions {
        val parentInfoLetter = Singleton.requests.getParentInfoLetter(at)
        return GradesFromHtml().getOptions(parentInfoLetter)
    }


    suspend fun loadGrades(gradeOptions: GradeOptions, termid: String) {
        val at = Singleton.at
        grades = Singleton.requests.getGrades(
            at,
            gradeOptions.PCLID.value,
            gradeOptions.ReportType.first { it.is_selected }.value,
            termid,
            gradeOptions.SID.value
        ).filter { it.avg != null }.toMutableList()
        withContext(Dispatchers.Main) {
            Singleton.grades = grades
            notifyListeners()
        }
    }

    fun addListener(listener: GradeActionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: GradeActionListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke(grades) }
    }

}