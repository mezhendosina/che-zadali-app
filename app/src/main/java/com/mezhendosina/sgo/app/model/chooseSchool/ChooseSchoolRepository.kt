package com.mezhendosina.sgo.app.model.chooseSchool

import com.mezhendosina.sgo.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

typealias schoolsActionListener = (List<SchoolUiEntity>) -> Unit

class ChooseSchoolRepository(private val schoolsSource: SchoolsSource) {

    var schools = mutableListOf<SchoolUiEntity>()

    private val listeners = mutableSetOf<schoolsActionListener>()

    suspend fun findSchool(name: String) {
        val schoolsList = schoolsSource.getSchools(name).map { it.toUiEntity() }

        withContext(Dispatchers.Main) {
            schools = schoolsList.toMutableList()
            Singleton.schools = schools
            notifyListeners()
        }
    }

    fun addListener(listener: schoolsActionListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: schoolsActionListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke(schools) }
    }


}