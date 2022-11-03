package com.mezhendosina.sgo.data.room

import androidx.room.Dao
import androidx.room.Query
import com.mezhendosina.sgo.data.room.entities.LessonRoomEntity

@Dao
interface LessonsDao {

    @Query("SELECT * FROM lessons_database WHERE day < :weekEnd AND day > :weekStart")
    fun getWeek(weekStart: Long, weekEnd: Long): List<LessonRoomEntity>

}