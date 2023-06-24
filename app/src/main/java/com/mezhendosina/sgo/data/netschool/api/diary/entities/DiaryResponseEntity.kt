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

package com.mezhendosina.sgo.data.netschool.api.diary.entities


import com.google.gson.annotations.SerializedName
import com.mezhendosina.sgo.app.uiEntities.MarkUiEntity

//TODO to ui entity
data class DiaryResponseEntity(
    @SerializedName("className")
    val className: String?,
    @SerializedName("laAssigns")
    val laAssigns: List<Any>?,
    @SerializedName("termName")
    val termName: String?,
    @SerializedName("weekDays")
    val weekDays: List<WeekDay>,
    @SerializedName("weekEnd")
    val weekEnd: String,
    @SerializedName("weekStart")
    val weekStart: String,
) {
    fun getAttachmentIDs(): List<Int> {
        val listAttachmentsId = mutableListOf<Int>()
        this.weekDays.forEach { weekDay ->
            weekDay.lessons.forEach { lesson ->
                lesson.assignments?.forEach {
                    listAttachmentsId.add(it.id)
                }
            }
        }
        return listAttachmentsId
    }
}

data class WeekDay(
    @SerializedName("date")
    val date: String,
    @SerializedName("lessons")
    val lessons: List<Lesson>
)

data class Lesson(
    @SerializedName("assignments")
    val assignments: List<Assignment>?,
    @SerializedName("classmeetingId")
    val classmeetingId: Int,
    @SerializedName("day")
    val day: String,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("isEaLesson")
    val isEaLesson: Boolean,
    @SerializedName("number")
    val number: Int,
    @SerializedName("relay")
    val relay: Int,
    @SerializedName("room")
    val room: Any?,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("subjectName")
    val subjectName: String
)

data class Assignment(
    @SerializedName("assignmentName")
    val assignmentName: String,
    @SerializedName("classMeetingId")
    val classMeetingId: Int,
    @SerializedName("dueDate")
    val dueDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("mark")
    val mark: Mark?,
    @SerializedName("markComment")
    val markComment: MarkComment?,
    @SerializedName("textAnswer")
    val textAnswer: TextAnswer?,
    @SerializedName("typeId")
    val typeId: Int,
    @SerializedName("weight")
    val weight: Int
)

data class MarkComment(
    val name: String
)

data class Mark(
    @SerializedName("assignmentId")
    val assignmentId: Int,
    @SerializedName("dutyMark")
    val dutyMark: Boolean,
    @SerializedName("mark")
    val mark: Int,
    @SerializedName("resultScore")
    val resultScore: Any?,
    @SerializedName("studentId")
    val studentId: Int
) {
    fun toUiEntity(markComment: String?) = MarkUiEntity(
        assignmentId,
        mark,
        markComment,
        dutyMark,
        resultScore
    )
}

data class TextAnswer(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("answerDate")
    val answerDate: String
)