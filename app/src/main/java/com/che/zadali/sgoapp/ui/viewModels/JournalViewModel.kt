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

    private val listener: DiaryActionListener = {
        _weekDays.value = it
    }

    init {
        diaryService.addListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        diaryService.removeListener(listener)
    }
}