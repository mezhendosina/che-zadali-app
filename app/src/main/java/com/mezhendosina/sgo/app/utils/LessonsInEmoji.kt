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
import android.view.View
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.ItemLessonNameBinding
import com.mezhendosina.sgo.app.uiEntities.LessonEmojiUiEntity

fun getEmojiLesson(lessonName: String): Int? {
    return LessonsInEmoji.list.firstOrNull { lessonName.contains(it.name, true) }?.emoji
}

fun ItemLessonNameBinding.setup(context: Context, lessonName: String) {
    val lessonImage = getEmojiLesson(lessonName)
    name.text = lessonName

    if (lessonImage != null) {
        emoji.visibility = View.VISIBLE
        emoji.setImageResource(lessonImage)
    } else emoji.visibility = View.GONE
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