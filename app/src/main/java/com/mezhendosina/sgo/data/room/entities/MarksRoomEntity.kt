package com.mezhendosina.sgo.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mezhendosina.sgo.data.requests.sgo.diary.entities.Mark

@Entity(tableName = "marks_database")
data class MarksRoomEntity(
    @PrimaryKey val assignmentId: Int,
    @ColumnInfo val studentId: Int,
    @ColumnInfo val mark: Int,
    @ColumnInfo val resultScore: String?,
    @ColumnInfo val dutyMark: Boolean
) {
    fun toMarkEntity(): Mark = TODO()
}
