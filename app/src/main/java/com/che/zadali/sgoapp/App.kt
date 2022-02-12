package com.che.zadali.sgoapp

import android.app.Application
import com.che.zadali.sgoapp.data.diary.DiaryService
import com.che.zadali.sgoapp.data.schools.SchoolService

class App : Application() {
    val schoolsService = SchoolService()
    val diaryService = DiaryService()
}