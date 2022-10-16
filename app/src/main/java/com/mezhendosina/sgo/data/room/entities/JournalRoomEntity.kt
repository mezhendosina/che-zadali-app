package com.mezhendosina.sgo.data.room.entities

import androidx.room.*
import com.google.gson.Gson
import com.mezhendosina.sgo.app.model.journal.entities.DiaryUiEntity

@Entity(
    tableName = "journal"
)
data class JournalRoomEntity(
    @PrimaryKey val time: Int,
    val diaryResponseEntity: DiaryUiEntity,
    val lastUpdate: Long
)

class Converters {

    @TypeConverter
    fun fromStatesHolder(sh: DiaryUiEntity): String {
        return Gson().toJson(sh)
    }

    @TypeConverter
    fun toStatesHolder(sh: String): DiaryUiEntity {
        return Gson().fromJson(sh, DiaryUiEntity::class.java)
    }
}

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