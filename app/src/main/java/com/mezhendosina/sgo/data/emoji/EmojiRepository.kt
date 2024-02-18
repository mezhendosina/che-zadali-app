package com.mezhendosina.sgo.data.emoji

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mezhendosina.sgo.app.uiEntities.LessonNameUiEntity
import com.mezhendosina.sgo.app.utils.DefaultLessons
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmojiRepository
    @Inject
    constructor(
        val emojiDao: EmojiDao,
        @ApplicationContext val context: Context,
    ) {
        private val _emojiList = MutableLiveData<List<EmojiEntity>>()
        val emojiList: LiveData<List<EmojiEntity>> = _emojiList

        init {
            CoroutineScope(Dispatchers.IO).launch {
                emojiDao.getAll().collect {
                    withContext(Dispatchers.Main) {
                        _emojiList.value = it
                    }
                }
            }
        }

        suspend fun getEmojiLessonByName(
            lessonName: String,
            lessonId: Int,
        ): LessonNameUiEntity? {
            val findInDb = _emojiList.value?.find { it.name.contains(lessonName) }
            return if (findInDb == null) {
                val defaultEmoji =
                    DefaultLessons.list.firstOrNull { lessonName.contains(it.nameId, true) }
                defaultEmoji?.let {
                    emojiDao.insertEmoji(
                        EmojiEntity(
                            lessonId,
                            context.resources.getResourceName(defaultEmoji.emoji),
                            lessonName,
                        ),
                    )
                }
                defaultEmoji
            } else {
                LessonNameUiEntity(
                    findInDb.name,
                    context.resources.getIdentifier(findInDb.emoji, "drawable", context.packageName),
                    findInDb.name,
                )
            }
        }

        suspend fun editLessonName(
            lessonId: Int,
            lessonName: String,
        ) {
            val getEmoji = emojiDao.getEmoji(lessonId)
            if (getEmoji != null) {
                emojiDao.insertEmoji(
                    EmojiEntity(lessonId, getEmoji.emoji, lessonName),
                )
            }
        }

        suspend fun editEmoji(
            lessonId: Int,
            emoji: String,
        ) {
            val getEmoji = emojiDao.getEmoji(lessonId)
            if (getEmoji != null) {
                emojiDao.insertEmoji(
                    EmojiEntity(
                        lessonId,
                        emoji,
                        getEmoji.name,
                    ),
                )
            }
        }
    }
