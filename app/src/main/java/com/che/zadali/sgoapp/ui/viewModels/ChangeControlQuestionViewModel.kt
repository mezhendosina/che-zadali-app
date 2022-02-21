package com.che.zadali.sgoapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChangeControlQuestionViewModel : ViewModel() {
    private var _question = MutableLiveData<String>()
    var question: LiveData<String> = _question

    private var _answer = MutableLiveData<String>()
    var answer: LiveData<String> = _answer

    init {
        _question.value = "Девичья фамилия вашей матери"
    }



}