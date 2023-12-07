package com.mezhendosina.sgo.app.model.grades

import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.grades.GradesFromHtml
import com.mezhendosina.sgo.data.netschool.api.grades.entities.GradesItem
import com.mezhendosina.sgo.data.netschool.api.grades.entities.gradeOptions.GradeOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface GradesRepositoryInterface {
    suspend fun loadGrades(gradeOptions: GradeOptions, termid: String, sortType: Int)

    suspend fun loadGradesOptions(): GradeOptions

    fun addListener(listener: GradeActionListener)

    fun removeListener(listener: GradeActionListener)
}