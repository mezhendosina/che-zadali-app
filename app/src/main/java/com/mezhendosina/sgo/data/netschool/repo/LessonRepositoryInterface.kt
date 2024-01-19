package com.mezhendosina.sgo.data.netschool.repo

import com.mezhendosina.sgo.app.model.answer.FileUiEntity
import com.mezhendosina.sgo.app.model.attachments.ANSWERS
import com.mezhendosina.sgo.app.model.attachments.HOMEWORK
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.uiEntities.AboutLessonUiEntity
import com.mezhendosina.sgo.app.uiEntities.WhyGradeEntity
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.AttachmentsRequestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

interface LessonRepositoryInterface {

    val lesson: StateFlow<AboutLessonUiEntity?>

    fun getAnswerText(): String
    fun editAnswerText(text: String)

    suspend fun getAboutLesson(
        lessonUiEntity: LessonUiEntity,
        studentId: Int
    )

    fun editAnswers(files: List<FileUiEntity>?)
}