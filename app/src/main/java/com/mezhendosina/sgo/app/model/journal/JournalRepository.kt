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

package com.mezhendosina.sgo.app.model.journal

import com.mezhendosina.sgo.app.model.journal.entities.AssignmentUiEntity
import com.mezhendosina.sgo.app.model.journal.entities.DiaryUiEntity
import com.mezhendosina.sgo.app.model.journal.entities.LessonUiEntity
import com.mezhendosina.sgo.app.model.journal.entities.WeekDayUiEntity
import com.mezhendosina.sgo.data.DateManipulation
import com.mezhendosina.sgo.data.WeekStartEndEntity
import com.mezhendosina.sgo.data.dateToRussian
import com.mezhendosina.sgo.data.netschool.api.attachments.AttachmentsSource
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.AttachmentsRequestEntity
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.AttachmentsResponseEntity
import com.mezhendosina.sgo.data.netschool.api.diary.entities.Assignment
import com.mezhendosina.sgo.data.netschool.api.diary.entities.DiaryResponseEntity
import com.mezhendosina.sgo.data.netschool.api.diary.entities.Lesson
import com.mezhendosina.sgo.data.netschool.api.diary.entities.PastMandatoryEntity
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class JournalRepository @Inject constructor(
    private val attachmentsSource: AttachmentsSource,
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

        val attachments = attachmentsSource.getAttachments(
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
                assignment.assignmentName
                    .dropWhile { it.isWhitespace() }
                    .dropLastWhile { it.isWhitespace() },
                assignment.classMeetingId,
                assignment.dueDate,
                assignment.id,
                assignment.mark?.toUiEntity(assignment.markComment?.name),
                assignment.textAnswer,
                assignment.typeId,
                assignment.weight,
                attachments.filter { it.assignmentId == assignment.id }
            )
        }
}