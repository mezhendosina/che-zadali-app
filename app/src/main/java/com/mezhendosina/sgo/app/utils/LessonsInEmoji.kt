/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.app.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.uiEntities.LessonEmojiUiEntity

enum class LessonNameFrom {
    JOURNAL,
    GRADES
}

fun getEmojiLesson(lessonName: String): Int? {
    return LessonsInEmoji.list.firstOrNull { lessonName.contains(it.name, true) }?.emoji
}

fun TextView.setLessonEmoji(context: Context, lessonName: String, lessonNameFrom: LessonNameFrom) {
    val lessonImage = getEmojiLesson(lessonName)
    text = lessonName

    if (lessonImage != null) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            lessonImage, 0, 0, 0
        )
        compoundDrawablePadding = context.resources.getDimensionPixelSize(
            when (lessonNameFrom) {
                LessonNameFrom.JOURNAL -> R.dimen.journal_lesson_name_drawable_margin
                LessonNameFrom.GRADES -> R.dimen.grades_lesson_name_drawable_margin
            }
        )
    } else setCompoundDrawables(null, null, null, null)
}

fun ImageView.setupAsLessonEmoji(context: Context, lessonName: String) {
    val lessonImage = getEmojiLesson(lessonName)
    if (lessonImage != null) {
        val bitmap = AppCompatResources.getDrawable(context, lessonImage)?.toBitmap()
        val defaultColor = TypedValue()
        context.theme.resolveAttribute(
            com.google.android.material.R.attr.colorPrimaryContainer,
            defaultColor,
            true
        )
        if (bitmap != null) {
            setImageBitmap(bitmap)
            val palette = Palette.from(bitmap).generate()
            background.setTint(palette.getVibrantColor(defaultColor.data))
            background.alpha = 64
        }
    } else {
        visibility = View.GONE
    }
}

object LessonsInEmoji {
    val list = listOf(
        LessonEmojiUiEntity("русский", R.drawable.lesson_russia),
        LessonEmojiUiEntity("математика", R.drawable.lesson_math),
        LessonEmojiUiEntity("информатика", R.drawable.lesson_inf),
        LessonEmojiUiEntity("литература", R.drawable.lesson_lit),
        LessonEmojiUiEntity("английский", R.drawable.lesson_eng),
        LessonEmojiUiEntity("иностранный", R.drawable.lesson_languages),
        LessonEmojiUiEntity("немецкий", R.drawable.lesson_german),
        LessonEmojiUiEntity("алгебра", R.drawable.lesson_math),
        LessonEmojiUiEntity("геометрия", R.drawable.lesson_geometry),
        LessonEmojiUiEntity("основы безопасности жизнедеятельности", R.drawable.lesson_obz),
        LessonEmojiUiEntity("география", R.drawable.lesson_geo),
        LessonEmojiUiEntity("обществознание", R.drawable.lesson_social),
        LessonEmojiUiEntity("история", R.drawable.lesson_history),
        LessonEmojiUiEntity("изобразительное искусство", R.drawable.lesson_paint),
        LessonEmojiUiEntity("физическая культура", R.drawable.lesson_run),
        LessonEmojiUiEntity("музыка", R.drawable.lesson_music),
        LessonEmojiUiEntity("химия", R.drawable.lesson_chemistry),
        LessonEmojiUiEntity("физика", R.drawable.lesson_phys),
        LessonEmojiUiEntity("астрономия", R.drawable.lesson_astronomy),
        LessonEmojiUiEntity("биология", R.drawable.lesson_bio),
        LessonEmojiUiEntity("технология", R.drawable.lesson_tech),
        LessonEmojiUiEntity("окружающий мир", R.drawable.lesson_natural_science),
        LessonEmojiUiEntity("проект", R.drawable.lesson_project),
    )
}