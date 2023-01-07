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

package com.mezhendosina.sgo.data.room.entities
//
//import androidx.room.Embedded
//import androidx.room.Relation
//import androidx.room.TypeConverter
//import com.google.gson.Gson
//import com.mezhendosina.sgo.app.model.journal.entities.DiaryUiEntity
//
//data class JournalRoomEntity(
//    @Embedded val lessonRoomEntity: LessonRoomEntity,
//
//    @Relation(
//        parentColumn = "classMeetingId",
//        entityColumn = "classMeetingId"
//    ) val assignmentsRoomEntity: List<AssignmentsWithMarkAndAttachments>?
//)
//
//
//class Converters {
//
//    @TypeConverter
//    fun fromStatesHolder(sh: DiaryUiEntity): String {
//        return Gson().toJson(sh)
//    }
//
//    @TypeConverter
//    fun toStatesHolder(sh: String): DiaryUiEntity {
//        return Gson().fromJson(sh, DiaryUiEntity::class.java)
//    }
//}

//@Entity(
//    tableName = "lessons"
//)
//data class LessonsRoomEntity(
//    @PrimaryKey val time: Long,
//    val day: String,
//    val number: Int,
//    @ColumnInfo(name = "lesson_name") val lessonName: String,
//    @ColumnInfo(name = "start_time") val startTime: String,
//    @ColumnInfo(name = "end_time") val endTime: String,
//    @ColumnInfo(name = "class_meeting_id") val classMeetingId: Int,
//    @ColumnInfo(name = "is_ea_lesson") val isEaLesson: Boolean
//)
//
//@Entity(
//    tableName = "assignments"
//)
//data class AssignmentsRoomEntity(
//    @PrimaryKey val classMeetingId: Int,
//    val id: Int,
//    @ColumnInfo(name = "type_id") val typeId: Int,
//    @ColumnInfo(name = "assignment_name") val assignmentName: String,
//    val weight: Int,
//    @ColumnInfo(name = "due_date") val dueDate: String,
//    val mark: Int,
//    @ColumnInfo(name = "duty_mark") val dutyMark: Boolean
//)