package com.mezhendosina.sgo

import android.annotation.SuppressLint
import com.mezhendosina.sgo.data.Requests
import com.mezhendosina.sgo.data.SettingsLoginData
import com.mezhendosina.sgo.data.announcements.AnnouncementsResponseItem
import com.mezhendosina.sgo.data.diary.Diary
import com.mezhendosina.sgo.data.diary.diary.DiaryResponse
import com.mezhendosina.sgo.data.schools.SchoolItem
import java.text.SimpleDateFormat
import java.util.*

object Singleton {

    val requests = Requests()

    var at: String = ""
    var todayHomework: Diary =
        Diary(DiaryResponse("", emptyList(), "", emptyList(), "", ""), emptyList())
    var announcements: List<AnnouncementsResponseItem> = emptyList()

    var currentWeek = 0
    var currentYearId: Int = 0
    var currentYear: String = ""
    var diary: Diary =
        Diary(DiaryResponse("", emptyList(), "", emptyList(), "", ""), emptyList())
    var schools = mutableListOf<SchoolItem>()

    suspend fun login(loginData: SettingsLoginData) {
        requests.login(loginData)
        val currentYearResponse = requests.yearList(at).first { !it.name.contains("(*) ") }
        currentYearId = currentYearResponse.id
    }
}
