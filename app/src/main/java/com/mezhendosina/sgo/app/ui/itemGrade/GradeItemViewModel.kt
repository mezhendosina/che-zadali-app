package com.mezhendosina.sgo.app.ui.itemGrade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.data.grades.CalculateGradeItem
import com.mezhendosina.sgo.data.grades.GradesCalculator
import com.mezhendosina.sgo.data.requests.sgo.grades.entities.GradesItem

class GradeItemViewModel : ViewModel() {

    lateinit var gradesCalculator: GradesCalculator
    private val _calculatedGrade = MutableLiveData<CalculateGradeItem>()
    val calculatedGrade: LiveData<CalculateGradeItem> = _calculatedGrade

    private val _oldCalculatedGrade = MutableLiveData<CalculateGradeItem>()
    val oldCalculatedGrade: LiveData<CalculateGradeItem> = _oldCalculatedGrade

    private val _oldChangeToGrade = MutableLiveData<Float>()
    val oldChangeToGrade: LiveData<Float> = _oldChangeToGrade

    private val _grade = MutableLiveData<CalculateGradeItem>()
    val grade: LiveData<CalculateGradeItem> = _grade

    fun initCalculator(gradeItem: GradesItem) {
        _calculatedGrade.value = gradeItem.toCalculateItem()
        _grade.value = _calculatedGrade.value
//
//        gradesCalculator = GradesCalculator(gradeItem)
//        CoroutineScope(Dispatchers.IO).launch {
//            if (gradeItem.avg != null) {
//                val calculatedGrade: CalculateGradeItem? =
//                    when (gradeItem.avgGrade()) {
//                        in 0f..2.5f -> gradesCalculator.autoCalculateGrade(2.5f)
//                        in 2.6f..3.5f -> gradesCalculator.autoCalculateGrade(3.5f)
//                        in 3.6f..4.5f -> gradesCalculator.autoCalculateGrade(4.5f)
//                        else -> null
//                    }
//                withContext(Dispatchers.Main) {
//                    if (calculatedGrade != null) _oldCalculatedGrade.value = calculatedGrade!!
//                    println(_oldCalculatedGrade.value)
//                }
//
//            }
//        }
    }

    fun editGrade(grade: Int, delta: Int) {
        _calculatedGrade.value = _calculatedGrade.value?.changeGrade(grade, delta)
    }

    fun calculateGrade(targetGrade: Float) {
        _oldCalculatedGrade.value =
            gradesCalculator.autoCalculateGrade(targetGrade)
        _oldChangeToGrade.value = _oldCalculatedGrade.value?.avg()

    }

}