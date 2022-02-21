package com.che.zadali.sgoapp

import android.app.Application
import com.che.zadali.sgoapp.data.services.SchoolService
import com.che.zadali.sgoapp.data.services.DiaryService

class App : Application() {
    val schoolsService = SchoolService()
    val diaryService = DiaryService()
}