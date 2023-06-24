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

import com.mezhendosina.sgo.app.uiEntities.LessonEmojiUiEntity

fun getEmojiLesson(lessonName: String): String {
    val lesson = LessonsInEmoji.list.firstOrNull { lessonName.contains(it.name, true) }
    return if (lesson != null) {
        "${lesson.emoji} $lessonName"
    } else lessonName
}

object LessonsInEmoji {
    val list = listOf(
        LessonEmojiUiEntity("русский", "🇷🇺"),
        LessonEmojiUiEntity("математика", "🧮"),
        LessonEmojiUiEntity("информатика", "🖥"),
        LessonEmojiUiEntity("литература", "📖"),
        LessonEmojiUiEntity("английский", "🇬🇧"),
        LessonEmojiUiEntity("иностранный", "🌐"),
        LessonEmojiUiEntity("немецкий", "🇩🇪"),
        LessonEmojiUiEntity("алгебра", "🧮"),
        LessonEmojiUiEntity("геометрия", "\uD83D\uDCD0"),
        LessonEmojiUiEntity("основы безопасности жизнедеятельности", "🛡"),
        LessonEmojiUiEntity("география", "🗺"),
        LessonEmojiUiEntity("обществознание", "👪"),
        LessonEmojiUiEntity("история", "📜"),
        LessonEmojiUiEntity("изобразительное искусство", "🎨"),
        LessonEmojiUiEntity("физическая культура", "🏃"),
        LessonEmojiUiEntity("музыка", "🎵"),
        LessonEmojiUiEntity("химия", "\uD83D\uDC68\u200D\uD83D\uDD2C"),
        LessonEmojiUiEntity("физика", "⚛"),
        LessonEmojiUiEntity("астрономия", "🛰"),
        LessonEmojiUiEntity("биология", "🧬"),
        LessonEmojiUiEntity("технология", "⚒"),
        LessonEmojiUiEntity("окружающий мир", "🌍"),
        LessonEmojiUiEntity("проект", "💼"),
    )
}