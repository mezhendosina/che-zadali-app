package com.mezhendosina.sgo.app.model.journal.entities

import com.mezhendosina.sgo.data.requests.sgo.diary.entities.Mark
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.PastMandatoryEntity
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.TextAnswer
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.AttachmentsResponseEntity

data class DiaryUiEntity(
    val weekDays: List<WeekDayUiEntity>,
    val weekEnd: String,
    val weekStart: String,
    val pastMandatory: List<PastMandatoryEntity>
)

data class WeekDayUiEntity(
    val date: String,
    val lessons: List<LessonUiEntity>
)

data class LessonUiEntity(
    val assignments: List<AssignmentUiEntity>?,
    val homework: AssignmentUiEntity?,
    val classmeetingId: Int,
    val day: String,
    val endTime: String,
    val isEaLesson: Boolean,
    val number: Int,
    val relay: Int,
    val startTime: String,
    val subjectName: String
)

data class AssignmentUiEntity(
    val assignmentName: String,
    val classMeetingId: Int,
    val dueDate: String,
    val id: Int,
    val mark: Mark?,
    val textAnswer: TextAnswer?,
    val typeId: Int,
    val weight: Int,
    val attachments: List<AttachmentsResponseEntity>
)