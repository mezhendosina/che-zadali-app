/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.app.ui.changeControlQuestion

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
}