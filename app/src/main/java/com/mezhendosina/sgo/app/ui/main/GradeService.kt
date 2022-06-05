package com.mezhendosina.sgo.app.ui.main

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.extractGrades
import com.mezhendosina.sgo.data.grades.GradeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias GradeActionListener = (grade: List<GradeItem>) -> Unit

class GradeService {

    private var grades = mutableListOf<GradeItem>()

    private val listeners = mutableSetOf<GradeActionListener>()

    suspend fun loadGrades(studentId: Int) {
//        val gradesRequests = Singleton.requests.loadGrades(Singleton.at, studentId.toString())
//
//        grades = extractGrades(gradesRequests).toMutableList()
//
//        withContext(Dispatchers.Main) {
//            notifyListeners()
//        }
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