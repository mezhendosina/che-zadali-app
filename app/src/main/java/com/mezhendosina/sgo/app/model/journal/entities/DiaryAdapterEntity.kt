package com.mezhendosina.sgo.app.model.journal.entities

import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.data.requests.diary.entities.Mark
import com.mezhendosina.sgo.data.requests.diary.entities.PastMandatoryEntity
import com.mezhendosina.sgo.data.requests.diary.entities.TextAnswer
import com.mezhendosina.sgo.data.requests.homework.entities.AttachmentsResponseEntity

data class DiaryAdapterEntity(
    val className: String?,
    val laAssigns: List<Any>?,
    val termName: String?,
    val weekDays: List<AdapterWeekDay>,
    val weekEnd: String,
    val weekStart: String,
    val pastMandatory: List<PastMandatoryEntity>
)

data class AdapterWeekDay(
    val date: String,
    val lessons: List<LessonAdapter>
)

data class LessonAdapter(
    val assignments: List<AdapterAssignment>?,
    val homework: AdapterAssignment?,
    val classmeetingId: Int,
    val day: String,
    val endTime: String,
    val isEaLesson: Boolean,
    val number: Int,
    val relay: Int,
    val room: Any?,
    val startTime: String,
    val subjectName: String
)

data class AdapterAssignment(
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