package com.mezhendosina.sgo.data.room

import com.mezhendosina.sgo.data.room.entities.AssignmentsWithMarkAndAttachments

data class HomeworkTuple(
    val day: Long,
    val date: String,
    val number: Int,
    val name: String,
    val classMeetingId: Int,
    val isEaLesson: Boolean,
    val assignments: List<AssignmentsWithMarkAndAttachments>
)