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

package com.mezhendosina.sgo.app.ui.journalFlow.containerLesson

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.model.answer.AnswerRepository
import com.mezhendosina.sgo.app.netschool.base.toDescription
import com.mezhendosina.sgo.data.SettingsDataStore
import com.mezhendosina.sgo.data.getValue
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.repo.LessonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class LessonContainerViewModel(
    private val answerRepository: AnswerRepository = NetSchoolSingleton.answerRepository,
    private val lessonRepository: LessonRepository = NetSchoolSingleton.lessonRepository
) : ViewModel() {

    val lesson = if (Singleton.lesson != null) {
        Singleton.lesson!!
    } else if (Singleton.pastMandatoryItem != null) {
        Singleton.pastMandatoryItem!!.toLessonEntity()
    } else null


    suspend fun sendAnswers(context: Context) {
        try {
            val currentUserId = SettingsDataStore.CURRENT_USER_ID.getValue(context, -1).first()
            val assignId = lesson?.homework?.id ?: -1
            answerRepository.sendTextAnswer(
                assignId,
                lessonRepository.answerText,
                currentUserId
            )
            val files = answerRepository.sendFiles(
                context,
                lessonRepository.answerFiles,
                lesson?.homework?.id ?: -1
            ) // TODO null exception handler
            withContext(Dispatchers.Main) {
                lessonRepository.editAnswers(files)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    R.string.answer_sended,
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.toDescription(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}