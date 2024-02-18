package com.mezhendosina.sgo.domain

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.mezhendosina.sgo.data.emoji.EmojiRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonEmojiUseCase
    @Inject
    constructor(
        val emojiRepository: EmojiRepository,
        @ApplicationContext val context: Context,
    ) {
        fun setupImageView(
            imageView: ImageView,
            lessonName: String,
            lessonId: Int,
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                val lessonImage = emojiRepository.getEmojiLessonByName(lessonName, lessonId)?.emoji
                withContext(Dispatchers.Main) {
                    imageView.apply {
                        if (lessonImage != null) {
                            val bitmap =
                                AppCompatResources.getDrawable(context, lessonImage)?.toBitmap()
                            val defaultColor = TypedValue()
                            context.theme.resolveAttribute(
                                com.google.android.material.R.attr.colorPrimaryContainer,
                                defaultColor,
                                true,
                            )
                            if (bitmap != null) {
                                setImageBitmap(bitmap)
                                val palette = Palette.from(bitmap).generate()
                                val vibrantColor = palette.getVibrantColor(defaultColor.data)
                                background.setTint(vibrantColor)
                                background.alpha = 28
                            }
                        } else {
                            visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
