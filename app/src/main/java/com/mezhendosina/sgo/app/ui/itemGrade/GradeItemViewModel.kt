package com.mezhendosina.sgo.app.ui.itemGrade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.data.grades.ChangeGradeItem
import com.mezhendosina.sgo.data.grades.GradesCalculator
import com.mezhendosina.sgo.data.requests.grades.entities.GradesItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GradeItemViewModel : ViewModel() {

    lateinit var gradesCalculator: GradesCalculator
    private val _changeGradeItem = MutableLiveData<ChangeGradeItem>()
    val changeGradeItem: LiveData<ChangeGradeItem> = _changeGradeItem

    private val _tooManyGrades = MutableLiveData(false)
    val tooManyGrades: LiveData<Boolean> = _tooManyGrades

    private val _changeToGrade = MutableLiveData<Double>()
    val changeToGrade: LiveData<Double> = _changeToGrade

    fun initCalculator(gradeItem: GradesItem) {
        gradesCalculator = GradesCalculator(gradeItem)
        CoroutineScope(Dispatchers.Main).launch {
            if (gradeItem.avg != null) {
                val avgGrade = gradeItem.avg.replace(",", ".").toFloat()
                if (avgGrade < 2.5) {
                    _changeGradeItem.value =
                        gradesCalculator.calculateGrade(2.5f)
                } else if (avgGrade < 3.5) {
                    _changeGradeItem.value =
                        gradesCalculator.calculateGrade(3.5f)
                } else if (avgGrade < 4.5) {
                    _changeGradeItem.value =
                        gradesCalculator.calculateGrade(4.5f)
                }
                _changeToGrade.value = _changeGradeItem.value?.avg

            }
        }
    }

    fun calculateGrade(targetGrade: Float) {
        _tooManyGrades.value = false
        _changeGradeItem.value =
            gradesCalculator.calculateGrade(targetGrade)
        _changeToGrade.value = _changeGradeItem.value?.avg

    }

}