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

package com.mezhendosina.sgo.data.netschool.repo

import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.app.model.attachments.ANSWERS
import com.mezhendosina.sgo.app.model.attachments.HOMEWORK
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.uiEntities.AboutLessonUiEntity
import com.mezhendosina.sgo.app.uiEntities.AssignTypeUiEntity
import com.mezhendosina.sgo.app.uiEntities.WhyGradeEntity
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import com.mezhendosina.sgo.data.netschool.api.attachments.AttachmentsSource
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.AttachmentsRequestEntity
import com.mezhendosina.sgo.data.netschool.api.homework.HomeworkSource
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

typealias LessonActionListener = (lesson: AboutLessonUiEntity?) -> Unit

class LessonRepository @Inject constructor(
    private val homeworkSource: HomeworkSource,
    private val attachmentsSource: AttachmentsSource,
) : LessonRepositoryInterface {
    private var assignTypes: List<AssignTypeUiEntity>? = null


    private val _lesson = MutableStateFlow<AboutLessonUiEntity?>(null)
    override val lesson: StateFlow<AboutLessonUiEntity?> = _lesson


    var answerFiles: List<FileUiEntity> = emptyList()
    private var answerText: String = ""

    override fun getAnswerText(): String = answerText

    override fun editAnswerText(text: String) {
        answerText = text
    }

    override suspend fun getAboutLesson(
        lessonUiEntity: LessonUiEntity,
        studentId: Int
    ) {
        val attachmentsR = attachmentsSource.getAttachments(
            studentId,
            AttachmentsRequestEntity(lessonUiEntity.assignments?.map { it.id } ?: emptyList())
        )
        val answerFilesResponse =
            attachmentsR.firstOrNull()?.answerFiles?.map {
                it.attachment.toUiEntity(
                    ANSWERS,
                    lessonUiEntity.classmeetingId
                )
            }
        val attachments = attachmentsR.firstOrNull()?.attachments?.map {
            it.toUiEntity(
                HOMEWORK,
                lessonUiEntity.classmeetingId
            )
        }
        val aboutAssign =
            lessonUiEntity.homework?.id?.let { homeworkSource.getAboutAssign(it, studentId) }
        val answerTextResponse =
            lessonUiEntity.assignments?.firstOrNull { it.textAnswer != null }?.textAnswer?.answer

        answerFiles = answerFilesResponse ?: emptyList()
        answerText = answerTextResponse ?: ""

        val out = AboutLessonUiEntity(
            lessonUiEntity.classmeetingId,
            lessonUiEntity.subjectName,
            lessonUiEntity.homework?.assignmentName ?: "",
            aboutAssign?.description,
            attachments,
            loadGrades(lessonUiEntity),
            answerTextResponse,
            answerFilesResponse
        )

        withContext(Dispatchers.Main) {
            _lesson.value = out
        }
    }

    override fun editAnswers(files: List<FileUiEntity>?) {
        _lesson.value = _lesson.value?.editAnswers(answerText, files)
    }

    private suspend fun loadGrades(lessonUiEntity: LessonUiEntity): List<WhyGradeEntity> {
        val gradesList = mutableListOf<WhyGradeEntity>()
        if (assignTypes.isNullOrEmpty()) assignTypes =
            homeworkSource.assignmentTypes().map { it.toUiEntity() }

        lessonUiEntity.assignments?.forEach { assign ->
            if (assign.mark != null) {
                gradesList.add(
                    WhyGradeEntity(
                        assign.assignmentName,
                        assign.mark,
                        assignTypes?.firstOrNull { it.id == assign.typeId }?.name
                    )
                )
            }
        }
        return gradesList
    }
}