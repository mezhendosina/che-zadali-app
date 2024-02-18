package com.mezhendosina.sgo.data.emoji

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EmojiDao {
    @Query("SELECT * FROM emoji")
    fun getAll(): Flow<List<EmojiEntity>>

    @Query("SELECT * FROM emoji WHERE lessonId=:lessonId")
    suspend fun getEmoji(lessonId: Int): EmojiEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmoji(vararg emojiEntity: EmojiEntity)
}
