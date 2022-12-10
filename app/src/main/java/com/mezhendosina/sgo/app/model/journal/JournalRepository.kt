package com.mezhendosina.sgo.app.model.journal

import com.mezhendosina.sgo.app.model.homework.HomeworkSource
import com.mezhendosina.sgo.app.model.journal.entities.AssignmentUiEntity
import com.mezhendosina.sgo.app.model.journal.entities.DiaryUiEntity
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.model.journal.entities.WeekDayUiEntity
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.WeekStartEndEntity
import com.mezhendosina.sgo.data.dateToRussian
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.Assignment
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.DiaryResponseEntity
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.Lesson
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.PastMandatoryEntity
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.AttachmentsRequestEntity
import com.mezhendosina.sgo.data.requests.sgo.homework.entities.AttachmentsResponseEntity

class JournalRepository(
    private val homeworkSource: HomeworkSource,
    private val diarySource: DiarySource
) {

    suspend fun getWeek(
        studentId: Int,
        weekStartEndEntity: WeekStartEndEntity,
        yearId: Int
    ): DiaryUiEntity {
        diarySource.diaryInit()

        val diaryRequestEntity = DiaryModelRequestEntity(
            studentId,
            weekStartEndEntity.weekStart,
            weekStartEndEntity.weekEnd,
            yearId
        )

        val diary = diarySource.diary(diaryRequestEntity)
        val attachmentsId = diary.getAttachmentIDs()

        val attachments = homeworkSource.getAttachments(
            studentId,
            AttachmentsRequestEntity(attachmentsId)
        )
        val pastMandatory = diarySource.getPastMandatory(diaryRequestEntity)

        return mapDiary(
            attachments,
            diary,
            pastMandatory
        )
    }

    private fun mapDiary(
        attachments: List<AttachmentsResponseEntity>,
        diary: DiaryResponseEntity,
        pastMandatory: List<PastMandatoryEntity>
    ): DiaryUiEntity = DiaryUiEntity(
        diary.weekDays.map {
            WeekDayUiEntity(
                dateToRussian(it.date),
                mapLessons(it.lessons, attachments)
            )
        },
        DateManipulation(diary.weekEnd).journalDate(),
        DateManipulation(diary.weekStart).journalDate(),
        pastMandatory
    )

    private fun mapLessons(
        lessons: List<Lesson>,
        attachments: List<AttachmentsResponseEntity>
    ): List<LessonUiEntity> = lessons.map { lesson ->
        val assignments = mapAssignments(lesson.assignments, attachments)
        LessonUiEntity(
            assignments,
            assignments?.find { it.typeId == 3 },
            lesson.classmeetingId,
            lesson.day,
            lesson.endTime,
            lesson.isEaLesson,
            lesson.number,
            lesson.relay,
            lesson.startTime,
            lesson.subjectName
        )
    }

    private fun mapAssignments(
        assignments: List<Assignment>?,
        attachments: List<AttachmentsResponseEntity>
    ): List<AssignmentUiEntity>? =
        assignments?.map { assignment ->
            AssignmentUiEntity(
                assignment.assignmentName,
                assignment.classMeetingId,
                assignment.dueDate,
                assignment.id,
                assignment.mark,
                assignment.textAnswer,
                assignment.typeId,
                assignment.weight,
                attachments.filter { it.assignmentId == assignment.id }
            )
        }
}