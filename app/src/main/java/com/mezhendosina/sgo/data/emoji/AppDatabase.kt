package com.mezhendosina.sgo.data.emoji

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EmojiEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getEmoji(): EmojiDao
}
