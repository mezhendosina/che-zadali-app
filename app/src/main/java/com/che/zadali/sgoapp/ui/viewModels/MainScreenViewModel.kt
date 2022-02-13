package com.che.zadali.sgoapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.che.zadali.sgo_app.data.diary.Lesson
import com.che.zadali.sgoapp.data.services.DiaryService
import com.che.zadali.sgoapp.data.services.TodayActionListener

class MainScreenViewModel(private val DiaryService: DiaryService) : ViewModel() {
    private var _lessons = MutableLiveData<List<Lesson>>()
    var lessons: LiveData<List<Lesson>> = _lessons

    private var _expandAll = MutableLiveData<Boolean>(false)
    var expandAll: LiveData<Boolean> = _expandAll

    private val listener: TodayActionListener = {
        _lessons.value = it
    }

    init {
        loadTodayLessons()
    }

    fun changeExpand(){
        _expandAll.value = _expandAll.value?.not()
    }

    fun loadTodayLessons() {
        DiaryService.addTodayListener(listener)
        DiaryService.loadToday()
    }

    override fun onCleared() {
        super.onCleared()
        DiaryService.removeTodayListener(listener)
    }
}