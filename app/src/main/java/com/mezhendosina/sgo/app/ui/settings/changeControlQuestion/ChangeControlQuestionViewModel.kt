package com.mezhendosina.sgo.app.ui.settings.changeControlQuestion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChangeControlQuestionViewModel : ViewModel() {
    private val _questions = MutableLiveData<Array<String>>()
    val questions: LiveData<Array<String>> = _questions

    init {
        _questions.value = arrayOf(
            "Не выбрано",
            "Девичья фамилия вашей матери",
            "Кличка домашнего животного",
            "Любимое блюдо",
            "Почтовый индекс родителей",
            "Дата рождения бабушки",
            "Номер паспорта",
            "Ваш любимый номер телефона",
            "Собственный вопрос"
        )
    }

    fun sendControlQuestion(){

    }

}