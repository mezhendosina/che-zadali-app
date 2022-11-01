package com.mezhendosina.sgo.app.model.grades

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.Singleton.at
import com.mezhendosina.sgo.data.grades.GradesFromHtml
import com.mezhendosina.sgo.data.requests.grades.entities.GradesItem
import com.mezhendosina.sgo.data.requests.grades.entities.gradeOptions.GradeOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias GradeActionListener = (grade: List<GradesItem>) -> Unit

class GradesRepository(
    private val gradesSource: GradesSource
) {

    private var grades = mutableListOf<GradesItem>()

    private val listeners = mutableSetOf<GradeActionListener>()

    suspend fun loadGradesOptions(): GradeOptions {
        val parentInfoLetter = gradesSource.getParentInfoLetter(at).body()?.string() ?: ""
        return GradesFromHtml().getOptions(parentInfoLetter)
    }


    suspend fun loadGrades(gradeOptions: GradeOptions, termid: String) {
        val at = Singleton.at
        val getGradesRequest = gradesSource.getGrades(
            at,
            gradeOptions.PCLID.value,
            gradeOptions.ReportType.first { it.is_selected }.value,
            termid,
            gradeOptions.SID.value
        ).body()?.string() ?: ""

        val gradesList = GradesFromHtml().extractGrades(getGradesRequest)

        grades = gradesList.filter { it.avg != null }.toMutableList()

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