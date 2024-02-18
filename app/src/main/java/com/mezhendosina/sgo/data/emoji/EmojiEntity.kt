package com.mezhendosina.sgo.data.emoji

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emoji")
data class EmojiEntity(
    @PrimaryKey val lessonId: Int,
    @ColumnInfo val emoji: String?,
    @ColumnInfo val name: String,
)
