package com.mezhendosina.sgo.data.netschool.repo

import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.app.model.attachments.ANSWERS
import com.mezhendosina.sgo.app.model.attachments.HOMEWORK
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.uiEntities.AboutLessonUiEntity
import com.mezhendosina.sgo.app.uiEntities.WhyGradeEntity
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.AttachmentsRequestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface LessonRepositoryInterface {

    fun getAnswerText(): String
    fun editAnswerText(text: String)

    fun getLesson(): AboutLessonUiEntity?

    suspend fun getAboutLesson(
        lessonUiEntity: LessonUiEntity,
        studentId: Int
    )

    fun editAnswers(files: List<FileUiEntity>?)


    fun addListener(listener: LessonActionListener)

    fun removeListener(listener: LessonActionListener)

}