package com.che.zadali.sgoapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.che.zadali.sgo_app.data.diary.WeekDay
import com.che.zadali.sgoapp.data.services.DiaryActionListener
import com.che.zadali.sgoapp.data.services.DiaryService

class JournalViewModel(private val diaryService: DiaryService) : ViewModel() {

    private var _weekDays = MutableLiveData<List<WeekDay>>()
    var weekDays: LiveData<List<WeekDay>> = _weekDays

    private var _week = MutableLiveData<String>()
    var week: LiveData<String> = _week

    private val listener: DiaryActionListener = {
        _weekDays.value = it
        _week.value = diaryService.week
    }

    init {
        diaryService.addListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        diaryService.removeListener(listener)
    }
}