package com.che.zadali.sgoapp.screens.viewModels.mainActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.che.zadali.sgo_app.data.diary.Lesson
import com.che.zadali.sgoapp.data.diary.DiaryService
import com.che.zadali.sgoapp.data.diary.TodayActionListener

class MainScreenViewModel(private val DiaryService: DiaryService) : ViewModel() {
    private var _lessons = MutableLiveData<List<Lesson>>()
    var lessons = _lessons



    private val listener: TodayActionListener = {
        _lessons.value = it
    }

    init {
        loadTodayLessons()
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