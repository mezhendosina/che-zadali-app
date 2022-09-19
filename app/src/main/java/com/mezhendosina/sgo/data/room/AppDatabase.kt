package com.mezhendosina.sgo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mezhendosina.sgo.data.room.entities.Converters
import com.mezhendosina.sgo.data.room.entities.JournalRoomEntity


@Database(
    version = 1,
    entities = [
        JournalRoomEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getJournalDao(): JournalDao

}