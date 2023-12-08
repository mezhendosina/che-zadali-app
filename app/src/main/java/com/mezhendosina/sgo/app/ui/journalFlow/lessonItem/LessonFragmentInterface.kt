package com.mezhendosina.sgo.app.ui.journalFlow.lessonItem

interface LessonFragmentInterface {

    fun bindLesson()

    fun observeErrors()
    fun observeOnAddAnswerClick()

    fun observeOnEditAnswerClick()
    fun observeOnCopyHomeworkClick()
    fun observeOnAnswerUpdated()
}