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

package com.mezhendosina.sgo.app.model.journal.entities

import com.mezhendosina.sgo.app.uiEntities.MarkUiEntity
import com.mezhendosina.sgo.data.netschool.api.attachments.entities.AttachmentsResponseEntity
import com.mezhendosina.sgo.data.netschool.api.diary.entities.PastMandatoryEntity
import com.mezhendosina.sgo.data.netschool.api.diary.entities.TextAnswer

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
) {
    fun addHomework(homework: AssignmentUiEntity) = LessonUiEntity(
        assignments,
        homework,
        classmeetingId,
        day,
        endTime,
        isEaLesson,
        number,
        relay,
        startTime,
        subjectName
    )

    fun addWhyGrades(marks: List<MarkUiEntity>): LessonUiEntity {
        val outAssignments =
            mutableListOf<AssignmentUiEntity>()
        assignments?.forEach { assign ->
            val mark = marks.firstOrNull { it.id == assign.id }
            if (mark != null) {
                outAssignments.add(
                    AssignmentUiEntity(
                        assign.assignmentName,
                        assign.classMeetingId,
                        assign.dueDate,
                        assign.id,
                        mark,
                        assign.textAnswer,
                        assign.typeId,
                        assign.weight,
                        assign.attachments
                    )
                )
            } else {
                outAssignments.add(assign)
            }
        }
        return LessonUiEntity(
            outAssignments,
            homework,
            classmeetingId,
            day,
            endTime,
            isEaLesson,
            number,
            relay,
            startTime,
            subjectName
        )
    }
}

data class AssignmentUiEntity(
    val assignmentName: String,
    val classMeetingId: Int,
    val dueDate: String,
    val id: Int,
    val mark: MarkUiEntity?,
    val textAnswer: TextAnswer?,
    val typeId: Int,
    val weight: Int,
    val attachments: List<AttachmentsResponseEntity>
)
//
//data class MarkUiEntity(
//    val assignId: Int,
//    val assignName: String,
//    val comment: String?,
//    val mark: Int,
//    val dutyMark: Boolean,
//    val resultScore: Int,
//    val typeId: Int,
//)